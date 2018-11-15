package com.apollo.stomp;

/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-15 16:15
 */
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.camel.AsyncCallback;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.fusesource.hawtbuf.AsciiBuffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Task;
import org.fusesource.stomp.client.Callback;
import org.fusesource.stomp.client.CallbackConnection;
import org.fusesource.stomp.client.Constants;
import org.fusesource.stomp.client.Promise;
import org.fusesource.stomp.client.Stomp;
import org.fusesource.stomp.codec.StompFrame;

@UriEndpoint(
        scheme = "stomp",
        title = "Stomp",
        syntax = "stomp:destination",
        consumerClass = StompConsumer.class,
        label = "messaging"
)
public class StompEndpoint extends DefaultEndpoint {
    @UriPath
    @Metadata(
            required = "true"
    )
    private String destination;
    @UriParam
    private StompConfiguration configuration;
    private CallbackConnection connection;
    private Stomp stomp;
    private final List<StompConsumer> consumers = new CopyOnWriteArrayList();

    public StompEndpoint(String uri, StompComponent component, StompConfiguration configuration, String destination) {
        super(uri, component);
        this.configuration = configuration;
        this.destination = destination;
    }

    public Producer createProducer() throws Exception {
        return new StompProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new StompConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    protected void doStart() throws Exception {
        Promise<CallbackConnection> promise = new Promise();
        this.stomp = new Stomp(this.configuration.getBrokerURL());
        this.stomp.setLogin(this.configuration.getLogin());
        this.stomp.setPasscode(this.configuration.getPasscode());
        this.stomp.connectCallback(promise);
        if (this.configuration.getHost() != null && !this.configuration.getHost().isEmpty()) {
            this.stomp.setHost(this.configuration.getHost());
        }

        this.connection = (CallbackConnection)promise.await();
        this.connection.getDispatchQueue().execute(new Task() {
            public void run() {
                StompEndpoint.this.connection.receive(new Callback<StompFrame>() {
                    public void onFailure(Throwable value) {
                        if (StompEndpoint.this.started.get()) {
                            StompEndpoint.this.connection.close((Runnable)null);
                        }

                    }

                    public void onSuccess(StompFrame value) {
                        if (!StompEndpoint.this.consumers.isEmpty()) {
                            Exchange exchange = StompEndpoint.this.createExchange();
                            exchange.getIn().setBody(value.content());
                            Iterator var3 = StompEndpoint.this.consumers.iterator();

                            while(var3.hasNext()) {
                                StompConsumer consumer = (StompConsumer)var3.next();
                                consumer.processExchange(exchange);
                            }
                        }

                    }
                });
                StompEndpoint.this.connection.resume();
            }
        });
    }

    protected void doStop() throws Exception {
        this.connection.getDispatchQueue().execute(new Task() {
            public void run() {
                StompFrame frame = new StompFrame(Constants.DISCONNECT);
                StompEndpoint.this.connection.send(frame, (Callback)null);
            }
        });
        this.connection.close((Runnable)null);
    }

    protected void send(final Exchange exchange, final AsyncCallback callback) {
        final StompFrame frame = new StompFrame(Constants.SEND);
        String actualDestination = this.destination;
        if (StompComponent.ANY_DESTINATION.equals(actualDestination)) {
            actualDestination = (String)exchange.getIn().getHeader(StompComponent.STOMP_DESTINATION);
        }

        frame.addHeader(Constants.DESTINATION, StompFrame.encodeHeader(actualDestination));
        frame.content(UTF8Buffer.utf8(exchange.getIn().getBody().toString()));
        this.connection.getDispatchQueue().execute(new Task() {
            public void run() {
                StompEndpoint.this.connection.send(frame, new Callback<Void>() {
                    public void onFailure(Throwable e) {
                        exchange.setException(e);
                        callback.done(false);
                    }

                    public void onSuccess(Void v) {
                        callback.done(false);
                    }
                });
            }
        });
    }

    void addConsumer(final StompConsumer consumer) {
        this.connection.getDispatchQueue().execute(new Task() {
            public void run() {
                StompFrame frame = new StompFrame(Constants.SUBSCRIBE);
                frame.addHeader(Constants.DESTINATION, StompFrame.encodeHeader(StompEndpoint.this.destination));
                frame.addHeader(Constants.ID, consumer.id);
                StompEndpoint.this.connection.send(frame, (Callback)null);
            }
        });
        this.consumers.add(consumer);
    }

    void removeConsumer(final StompConsumer consumer) {
        this.connection.getDispatchQueue().execute(new Task() {
            public void run() {
                StompFrame frame = new StompFrame(Constants.UNSUBSCRIBE);
                frame.addHeader(Constants.DESTINATION, StompFrame.encodeHeader(StompEndpoint.this.destination));
                frame.addHeader(Constants.ID, consumer.id);
                StompEndpoint.this.connection.send(frame, (Callback)null);
            }
        });
        this.consumers.remove(consumer);
    }

    AsciiBuffer getNextId() {
        return this.connection.nextId();
    }
}

