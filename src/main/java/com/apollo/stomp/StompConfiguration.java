package com.apollo.stomp;

/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-15 16:15
 */
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;

@UriParams
public class StompConfiguration implements Cloneable {
    @UriParam(
            defaultValue = "tcp://localhost:61613"
    )
    private String brokerURL = "tcp://localhost:61613";
    @UriParam
    private String login;
    @UriParam
    private String passcode;
    @UriParam
    private String host;

    public StompConfiguration() {
    }

    public StompConfiguration copy() {
        try {
            StompConfiguration copy = (StompConfiguration)this.clone();
            return copy;
        } catch (CloneNotSupportedException var2) {
            throw new RuntimeCamelException(var2);
        }
    }

    public String getBrokerURL() {
        return this.brokerURL;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasscode() {
        return this.passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }
}
