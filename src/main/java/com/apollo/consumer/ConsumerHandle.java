package com.apollo.consumer;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-14 15:22
 */
@Component
public class ConsumerHandle implements Processor {

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
    }
}
