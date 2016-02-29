/*
 * Created by Daniel Marell 14-01-28 21:27
 */
package se.marell.dvesta.chat.jabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import se.marell.dvesta.chat.chatapi.TextChatConnection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("jabber")
public class JabberChatConnection implements TextChatConnection {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment environment;

    private JabberClient jabberClient;
    private List<MessageListener> listeners = new ArrayList<>();
    private String masterJabberUsername;

    @PostConstruct
    private void activate() {
        String jabberHost = environment.getProperty("dvesta.jabber.host");
        int jabberPort = Integer.parseInt(environment.getProperty("dvesta.jabber.port"));
        String jabberUsername = environment.getProperty("dvesta.jabber.clientUsername");
        String jabberPassword = environment.getProperty("dvesta.jabber.clientPassword");
        masterJabberUsername = environment.getProperty("dvesta.jabber.masterUsername");
        jabberClient = new JabberClient(jabberHost, jabberPort, jabberUsername, jabberPassword, message -> {
            for (MessageListener listener : listeners) {
                listener.messageReceived(message);
            }
        });
        log.info("Started " + getName());
    }

    @PreDestroy
    protected void deactivate() {
        jabberClient.disconnect();
        log.info("deactivated " + getName());
    }

    @Override
    public void sendMessage(String message) {
        try {
            if (!jabberClient.isConnected()) {
                log.info("Connecting jabber client");
                jabberClient.connect(masterJabberUsername);
            }
            jabberClient.sendMessage(String.format("%s: %s", LocalTime.now(), message));
        } catch (JabberClient.JabberClientException e) {
            jabberClient.disconnect();
            log.error("Jabber client disconnected: " + e.getMessage());
        }
    }

    @Override
    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(MessageListener listener) {
        listeners.remove(listener);
    }

    @Override
    public String getParticipant() {
        return jabberClient.getParticipant();
    }

    private String getName() {
        return "JabberChatConnection";
    }
}
