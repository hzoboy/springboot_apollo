package com.apollo.route;

import com.apollo.consumer.ConsumerHandle;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-14 15:23
 */
@Component
public class MtqqMessageRouter extends RouteBuilder {
    @Autowired
    ConsumerHandle consumerHandle;

    @Value("${apollo.queue.name}")
    private String queue;
    @Value("${apollo.direct.name}")
    private String direct;
    @Override
    public void configure() throws Exception {
//        from(queue).streamCaching().process(consumerHandle);
        from(direct).process(consumerHandle).to("stompa:*");
    }
}
