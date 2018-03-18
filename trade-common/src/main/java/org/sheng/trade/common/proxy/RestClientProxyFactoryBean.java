package org.sheng.trade.common.proxy;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.sheng.trade.common.constant.TradeEnums.ServerConfigEnum;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Proxy;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Setter
@Slf4j
public class RestClientProxyFactoryBean implements FactoryBean {
    private Class serviceInterface;
    private ServerConfigEnum serverConfigEnum;
    private RestTemplate restTemplate;

    @Override
    public Object getObject() {
        Object result;
        result = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{serviceInterface}, (proxy, method, args) -> {
            Object result1;
            result1 = restTemplate.postForObject(serverConfigEnum.getServerPath() + method.getName(), args[0], method.getReturnType());
            return result1;
        });
        log.info("{} 代理对象生成", serviceInterface.getName());
        return result;
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
