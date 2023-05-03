package tbs.utils.AOP.authorize.impls;

import io.netty.util.internal.StringUtil;
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
import tbs.utils.AOP.authorize.model.BaseRoleModel;
import tbs.utils.error.NetError;
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
public class Access_AOP_Config {


    @Bean
    @ConditionalOnMissingBean(IAccess.class)
    IAccess defaultAccessBean() {
        return new DefaultAccessImpl();
    }


    @Pointcut("execution(public tbs.utils.Results.NetResult tbs.utils.AOP.controller.ApiProxy.method(..))")
    public void result() {

    }

    ThreadLocal<String> sessionToken = new ThreadLocal<>();

    @Around("result()")
    NetResult handleResult(ProceedingJoinPoint joinPoint) throws Throwable {
        NetResult result=null;
        Object[] objs = joinPoint.getArgs();
        if (objs[1] == null) {
            result = new NetResult();
        }
        else
            result= (NetResult) objs[1];
        result.setInvokeToken(sessionToken.get());
        objs[1] = result;
        result = (NetResult) joinPoint.proceed(objs);

        return result;

    }


    @Pointcut("execution(public tbs.utils.Results.NetResult *.*(..))&&@annotation(tbs.utils.AOP.authorize.annotations.AccessRequire)")
    public void access() {
    }

    @Around("access()")
    NetResult handleSafe(ProceedingJoinPoint joinPoint) {
        NetResult result = new NetResult();
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            BaseRoleModel roleModel = accessCheck(signature);
            Object[] params = joinPoint.getArgs();
            if (roleModel != null) {
                params = appendUserRole(roleModel, signature, params);
            }
            result = (NetResult) joinPoint.proceed(params);
        } catch (AuthorizationFailureException authorizationFailureException) {
            result.setCode(NetResult.LIMITED_ACCESS);
            result.setMessage(authorizationFailureException.getMessage());
            result.setCost(-1);
        } catch (Throwable throwable) {
            result.setCost(-1);
            result.setCode(NetResult.Unchecked_Exception);
            result.setMessage(throwable.getMessage());
        }
        return result;
    }

    Object[] appendUserRole(BaseRoleModel roleModel, MethodSignature signature, Object[] params) {
        int index = 0;
        boolean flag = false;
        for (; index < signature.getParameterTypes().length; index++) {
            if (signature.getParameterTypes()[index].equals(BaseRoleModel.class)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            params[index] = roleModel;
        }
        return params;
    }

    @Resource
    HttpServletRequest request;

    @Resource
    IAccess access;

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
        sessionToken.set(token);
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

    private BaseRoleModel accessCheck(MethodSignature signature) throws AuthorizationFailureException {
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
            BaseRoleModel userrole = access.readRole(tk);
            if (userrole == null) {
                throw new AuthorizationFailureException(String.format("无效密钥信息"));
            }
            if (!roleMap.containsKey(userrole.getRoleCode())) {
                throw new AuthorizationFailureException(String.format("您的权限%s,不支持此操作", userrole.getRoleName()));
            }
            return userrole;
        }
        return null;
    }


}
