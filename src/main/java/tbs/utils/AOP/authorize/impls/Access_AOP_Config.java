package tbs.utils.AOP.authorize.impls;

import com.alibaba.fastjson.JSON;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tbs.utils.AOP.authorize.annotations.AccessRequire;
import tbs.utils.AOP.authorize.error.AuthorizationFailureException;
import tbs.utils.AOP.authorize.interfaces.IAccess;
import tbs.utils.AOP.authorize.interfaces.IPermissionVerification;
import tbs.utils.AOP.authorize.model.BaseRoleModel;
import tbs.utils.AOP.authorize.model.SystemExecutionData;
import tbs.utils.AOP.controller.ApiProxy;
import tbs.utils.AOP.controller.IAction;
import tbs.utils.Async.annotations.AsyncReturnFunction;
import tbs.utils.Results.NetResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author abstergo
 */
@Aspect
@Component
@Slf4j
public class Access_AOP_Config {
    @Resource
    HttpServletRequest request;

    @Resource
    IAccess access;
    @Resource
    IPermissionVerification permissionVerification;


    @Bean
    @ConditionalOnMissingBean(IAccess.class)
    IAccess defaultAccessBean() {
        return new DefaultAccessImpl();
    }


    @Bean
    @ConditionalOnMissingBean(IPermissionVerification.class)
    IPermissionVerification permissionVerification() {
        return new IPermissionVerification() {
            @Override
            public VerificationConclusion accessCheck(BaseRoleModel user, Map<Integer, BaseRoleModel> needRoleMap) throws AuthorizationFailureException {
                if (needRoleMap.containsKey(user.getRoleCode())) {
                    return VerificationConclusion.EQUAL;
                } else {
                    return VerificationConclusion.UnAuthorized;
                }
            }
        };
    }

    @Resource
    ApiProxy apiProxy;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void access() {
    }


    @Around("access()")
    NetResult handleSafe(ProceedingJoinPoint joinPoint) {
        NetResult result = new NetResult();
        SystemExecutionData executionData = new SystemExecutionData();
        executionData.setRequestBeginTime(new Date());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        NetResult.MethodType type = signature.getMethod().getAnnotation(AsyncReturnFunction.class) != null ?
                NetResult.MethodType.AsynchronousDelay : NetResult.MethodType.Immediately;
        result.setMethodType(type);
        executionData.setMethodType(type);
        try {
            BaseRoleModel roleModel = accessCheck(signature, executionData);
            executionData.setInvokeRole(roleModel);
            result.setAuthType(executionData.getAuthType());
            result.setInvokeToken(executionData.getInvokeToken());
            Object[] params = joinPoint.getArgs();
            params = appendSystemData(executionData, signature, params);
            final Object[] fparams = params;
            result = apiProxy.method(new IAction() {
                @Override
                public Object action(NetResult result) throws Throwable {
                    return joinPoint.proceed(fparams);
                }
            }, result);
        } catch (AuthorizationFailureException authorizationFailureException) {
            result.setCode(NetResult.LIMITED_ACCESS);
            result.setMessage(authorizationFailureException.getMessage());
        } catch (Throwable throwable) {
            result.setCode(NetResult.Unchecked_Exception);
            result.setMessage(throwable.getMessage());
            log.error(throwable.getMessage(), throwable);
        } finally {
            executionData.setRequestEndTime(new Date());
            result.setCost(executionData.getRequestEndTime().getTime()-executionData.getRequestBeginTime().getTime());
        }
        return result;
    }

    Object[] appendSystemData(SystemExecutionData data, MethodSignature signature, Object[] params) {
        int index = 0;
        boolean flag = false;
        for (; index < signature.getParameterTypes().length; index++) {
            if (signature.getParameterTypes()[index].equals(SystemExecutionData.class)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            params[index] = data;
        }
        return params;
    }


    String takeToken(String f) {
        Enumeration<String> em = request.getHeaders(f);
        String token = null;
        while (em.hasMoreElements()) {
            String tmp = em.nextElement();
            if (!StringUtil.isNullOrEmpty(tmp)) {
                token = tmp;
            }
        }
        if (token != null) {
            if (token.trim().equals("null")) {
                token = null;
            }
        }
        return token;
    }

    private <T> void consumeElement(List<T> list, Consumer<T> f) {
        if (CollectionUtils.isEmpty(list) || f == null) {
            return;
        }
        for (T t : list) {
            f.accept(t);
        }
    }

    private BaseRoleModel accessCheck(MethodSignature signature, SystemExecutionData data) throws AuthorizationFailureException {
        log.info("invoke " + signature.getName());
        Method method = signature.getMethod();
        AccessRequire require = method.getAnnotation(AccessRequire.class);
        if (require != null) {
            String tk = takeToken(require.requireField());
            if (StringUtils.isEmpty(tk)) {
                throw new AuthorizationFailureException(String.format("空的密钥信息"));
            }
            Map<Integer, BaseRoleModel> roleMap = new HashMap<>();
            consumeElement(access.grandedManual(require.manual()), (e) -> {
                if (!roleMap.containsKey(e.getRoleCode())) {
                    roleMap.put(e.getRoleCode(), e);
                }
            });
            consumeElement(access.granded(method), (e) -> {
                if (!roleMap.containsKey(e.getRoleCode())) {
                    roleMap.put(e.getRoleCode(), e);
                }
            });
            log.info("require role:" + JSON.toJSONString(roleMap));
            BaseRoleModel userrole = access.readRole(tk);
            if (userrole == null) {
                throw new AuthorizationFailureException(String.format("无效密钥信息"));
            }
            IPermissionVerification.VerificationConclusion conclusion = permissionVerification.accessCheck(userrole, roleMap);
            data.setAuthType(conclusion);
            data.setInvokeToken(tk);
            if (conclusion == IPermissionVerification.VerificationConclusion.UnAuthorized) {
                throw new AuthorizationFailureException(String.format("您的权限%s级别，无法进行此操作", userrole.getRoleName()));
            }
            return userrole;
        }
        data.setAuthType(IPermissionVerification.VerificationConclusion.NO_NEED);
        data.setInvokeToken("匿名用户");
        return null;
    }


}
