package tbs.utils.AOP.controller;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tbs.utils.Async.AsyncMethod;
import tbs.utils.Async.ThreadUtil;
import tbs.utils.Results.NetResult;
import tbs.utils.error.NetError;

@Configuration
public class ControllerDefaultConfig {
    @Bean
    @ConditionalOnMissingBean(ApiProxy.class)
    ApiProxy proxy() {
        return new ApiProxy() {
            @Override
            public NetResult method(IAction action, NetResult result) {
                long beg = System.currentTimeMillis();
                try {
                    if (result.getMethodType() == NetResult.MethodType.AsynchronousDelay) {
                        ThreadUtil threadUtil = SpringUtil.getBean(ThreadUtil.class);
                        AsyncMethod method = SpringUtil.getBean(AsyncMethod.class);
                        result = method.process(result, threadUtil, action);
                    } else {
                        result.setData(action.action(result));
                        result.setMessage(action.msg());
                    }
                } catch (NetError netError) {
                    result.setCode(netError.getCode());
                    result.setMessage(netError.getMessage());
                    netError.printStackTrace();
                } catch (Throwable throwable) {
                    result.setMessage(throwable.getMessage());
                    result.setCode(NetResult.Unchecked_Exception);
                    throwable.printStackTrace();
                } finally {
                    long end = System.currentTimeMillis();
                    result.setCost(end - beg);
                }
                return result;
            }
        };
    }
}
