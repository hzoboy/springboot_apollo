package com.apollo.stomp;

/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-15 16:15
 */
import java.util.Map;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

public class StompComponent extends UriEndpointComponent {
    public static String ANY_DESTINATION = "/*";
    public static String STOMP_DESTINATION = "DESTINATION";
    private StompConfiguration configuration = new StompConfiguration();

    public StompComponent() {
        super(StompEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        String destination = "/" + remaining.replaceAll(":", "/");
        StompConfiguration config = this.getConfiguration().copy();
        this.setProperties(config, parameters);
        StompEndpoint endpoint = new StompEndpoint(uri, this, config, destination);
        this.setProperties(endpoint, parameters);
        return endpoint;
    }

    public StompConfiguration getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(StompConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setBrokerURL(String brokerURL) {
        this.getConfiguration().setBrokerURL(brokerURL);
    }

    public void setLogin(String login) {
        this.getConfiguration().setLogin(login);
    }

    public void setPasscode(String passcode) {
        this.getConfiguration().setPasscode(passcode);
    }

    public void setHost(String host) {
        this.getConfiguration().setHost(host);
    }
}
