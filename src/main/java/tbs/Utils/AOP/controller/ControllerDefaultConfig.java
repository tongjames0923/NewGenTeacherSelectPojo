package tbs.utils.AOP.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tbs.utils.AOP.authorize.error.AuthorizationFailureException;
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
                    result.setData(action.action(result));
                    result.setMessage(action.msg());
                } catch (AuthorizationFailureException authorizationFailureException) {
                    result = new NetResult();
                    result.setCode(NetResult.LIMITED_ACCESS);
                    result.setMessage(authorizationFailureException.getMessage());
                    authorizationFailureException.printStackTrace();
                } catch (NetError netError) {
                    result.setCode(netError.getCode());
                    result.setMessage(netError.getMessage());
                    netError.printStackTrace();
                } catch (Throwable throwable) {
                    result.setMessage(throwable.getMessage());
                    result.setCode(NetResult.Unchecked_Exception);
                    throwable.printStackTrace();
                }finally {
                    long end=System.currentTimeMillis();
                    result.setCost(end-beg);
                }
                return result;
            }
        };
    }
}
