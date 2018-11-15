package com.apollo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-14 15:00
 */
@Configuration
public class ActivemqCamelAutoConfiguration {

    @Value("${apollo.url}")
    private String url;
    @Value("${apollo.password}")
    private String psw;
    @Value("${apollo.user}")
    private String user;


    @Bean
    PooledConnectionFactory mqConnectionFactory(){

        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(new ActiveMQConnectionFactory(user,psw,url));

        return pooledConnectionFactory;
    }

}
