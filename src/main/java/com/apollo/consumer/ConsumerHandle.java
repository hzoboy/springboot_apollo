package com.apollo.consumer;

import com.apollo.stomp.StompComponent;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-14 15:22
 */
@Component
public class ConsumerHandle implements Processor {
    @Value("${apollo.stomp.topic.name}")
    private String stomp;
    private static final ThreadLocal<Exchange> localExchange = new ThreadLocal<Exchange>();
    @Override
    public void process(Exchange exchange) throws Exception {
        setLocalExchange(exchange);

        try{
            doProcess(exchange.getIn());
        }finally{
            removeLocalExchange();
        }
    }
    public void setLocalExchange(Exchange exchange) {
        localExchange.set(exchange);
    }

    public void removeLocalExchange() {
        localExchange.remove();
    }

    public  void doProcess(Message message) throws Exception{
        // message 就是发送方获取的消息
        System.out.println(message.getBody().toString());
        StringBuffer urlBuf = new StringBuffer("/topic/").append(stomp);
        message.setHeader(StompComponent.STOMP_DESTINATION, urlBuf.toString());
    }
}
