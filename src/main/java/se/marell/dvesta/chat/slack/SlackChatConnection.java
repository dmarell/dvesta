/*
 * Created by Daniel Marell 2016-01-07 17:32
 */
package se.marell.dvesta.chat.slack;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import se.marell.dvesta.chat.chatapi.TextChatConnection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("slack")
public class SlackChatConnection implements TextChatConnection {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment environment;

    private SlackSession session;
    private List<MessageListener> listeners = new ArrayList<>();
    private SlackChannel slackChannel;
    private String slackChannelName;

    @PostConstruct
    private void activate() {
        session = SlackSessionFactory.createWebSocketSlackSession(environment.getProperty("dvesta.slack.authtoken"));
        slackChannelName = environment.getProperty("dvesta.slack.channel");

        session.addMessagePostedListener((event, session1) -> {
            for (MessageListener listener : listeners) {
                listener.messageReceived(event.getMessageContent());
            }
        });
    }

    @PreDestroy
    protected void deactivate() {
        try {
            session.disconnect();
            log.info("deactivated " + getName());
        } catch (IOException e) {
            log.info("deactivate {} failed: {}", getName(), e.getMessage());
        }
    }

    @Override
    public void sendMessage(String message) {
        if (!session.isConnected()) {
            log.info("Connecting slack client");
            try {
                session.connect();
                if (slackChannelName != null) {
                    slackChannel = session.findChannelByName(slackChannelName);
                }
                if (slackChannel != null) {
                    log.info("Started {} on channel {}", getName(), slackChannelName);
                } else {
                    log.info("Started {}, channel {} not found", getName(), slackChannelName);
                }
            } catch (IOException e) {
                log.info("slack sendMessage failed, cannot connect: {}", e.getMessage());
            }
        }
        if (slackChannel != null) {
            session.sendMessage(slackChannel, String.format("%s", message), null);
        } else {
            log.info("slack sendMessage failed, no slack channel: {}", slackChannelName);
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
        return "slack";
    }

    private String getName() {
        return "SlackChatConnection";
    }
}
