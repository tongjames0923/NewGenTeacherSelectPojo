package tbs.utils.AOP.controller;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tbs.utils.Async.AsyncMethod;
import tbs.utils.Async.ThreadUtil;
import tbs.utils.Results.NetResult;
import tbs.utils.error.NetError;

@Configuration
@Slf4j
public class ControllerDefaultConfig {
    @Bean
    @ConditionalOnMissingBean(ApiProxy.class)
    ApiProxy proxy() {
        return new ApiProxy() {
            @Override
            public NetResult method(IAction action, NetResult result) throws Throwable {
                if (result.getMethodType() == NetResult.MethodType.AsynchronousDelay) {
                    ThreadUtil threadUtil = SpringUtil.getBean(ThreadUtil.class);
                    AsyncMethod method = SpringUtil.getBean(AsyncMethod.class);
                    result = method.process(result, threadUtil, action);
                } else {
                    result.setData(action.action(result));
                    result.setMessage(action.msg());
                }
                return result;
            }
        };
    }
}
