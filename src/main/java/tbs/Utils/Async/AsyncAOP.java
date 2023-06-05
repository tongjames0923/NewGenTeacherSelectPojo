package tbs.utils.Async;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import tbs.utils.Async.annotations.LockIt;
import tbs.utils.Async.interfaces.ILockProxy;
import tbs.utils.Async.interfaces.ILocker;
import tbs.utils.Results.NetResult;

import javax.annotation.Resource;
import java.util.function.Function;

@Aspect
@Configuration
@Slf4j
public class AsyncAOP {

    @Resource
    ILockProxy lockProxy;


    @Pointcut("@annotation(tbs.utils.Async.annotations.LockIt)")
    public void needlock() {
    }

    @Around("needlock()")
    Object handleLock(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockIt lockIt= signature.getMethod().getAnnotation(LockIt.class);
        ILocker locker= SpringUtil.getBean(lockIt.lockType());

        String lockname= lockIt.value();
        if(StringUtils.isEmpty(lockname))
        {
            lockname= signature.getDeclaringTypeName()+"."+signature.getMethod().getName();
        }
        return lockProxy.run(new Function<ILocker, Object>() {
            @Override
            public Object apply(ILocker iLocker) {
                try {
                    return joinPoint.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }, lockname);
    }
}
