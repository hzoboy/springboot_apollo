package com.apollo.stomp;

/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-15 16:16
 */
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.fusesource.hawtbuf.AsciiBuffer;

public class StompConsumer extends DefaultConsumer {
    AsciiBuffer id = this.getEndpoint().getNextId();

    public StompConsumer(Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    public StompEndpoint getEndpoint() {
        return (StompEndpoint)super.getEndpoint();
    }

    protected void doStart() throws Exception {
        this.getEndpoint().addConsumer(this);
        super.doStart();
    }

    protected void doStop() throws Exception {
        this.getEndpoint().removeConsumer(this);
        super.doStop();
    }

    void processExchange(Exchange exchange) {
        try {
            this.getProcessor().process(exchange);
        } catch (Throwable var3) {
            exchange.setException(var3);
        }

        if (exchange.getException() != null) {
            this.getExceptionHandler().handleException("Error processing exchange.", exchange, exchange.getException());
        }

    }
}

