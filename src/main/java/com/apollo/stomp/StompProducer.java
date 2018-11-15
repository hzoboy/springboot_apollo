package com.apollo.stomp;

/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-15 16:17
 */
import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultAsyncProducer;

public class StompProducer extends DefaultAsyncProducer implements Processor {
    private final StompEndpoint stompEndpoint;

    public StompProducer(StompEndpoint stompEndpoint) {
        super(stompEndpoint);
        this.stompEndpoint = stompEndpoint;
    }

    public boolean process(Exchange exchange, AsyncCallback callback) {
        try {
            this.stompEndpoint.send(exchange, callback);
            return false;
        } catch (Exception var4) {
            exchange.setException(var4);
            callback.done(true);
            return true;
        }
    }
}

