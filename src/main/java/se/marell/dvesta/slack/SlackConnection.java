/*
 * Created by Daniel Marell 2016-01-07 17:32
 */
package se.marell.dvesta.slack;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Service
public class SlackConnection {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment environment;

    private SlackSession session;
    private SlackChannel slackChannel;
    private String slackChannelName;

    @PostConstruct
    private void activate() {
        session = SlackSessionFactory.createWebSocketSlackSession(environment.getProperty("dvesta.slack.authtoken"));
        slackChannelName = environment.getProperty("dvesta.slack.channel");
    }

    @PreDestroy
    protected void deactivate() {
        try {
            session.disconnect();
            log.info("deactivated SlackConnection");
        } catch (IOException e) {
            log.info("deactivate failed: {}", e.getMessage());
        }
    }

    public void sendMessage(String message) {
        try {
            sendMessage(message, null);
        } catch (IOException e) {
            log.error("Failed to send slack message: {}", e.getMessage());
        }
    }

    public void sendMessage(String message, SlackAttachment attachment) throws IOException {
        initSession();
        if (slackChannel != null) {
            getSlackSession().sendMessage(slackChannel, String.format("%s", message), attachment);
        } else {
            log.error("slack sendMessage failed, no slack channel: {}", slackChannelName);
        }
    }

    public SlackSession getSlackSession() {
        initSession();
        return session;
    }

    private void initSession() {
        if (!session.isConnected()) {
            log.info("Connecting slack client, channel {}", slackChannelName);
            try {
                session.connect();
                if (slackChannelName != null) {
                    slackChannel = session.findChannelByName(slackChannelName);
                }
                if (slackChannel != null) {
                    log.info("Started SlackConnection on channel {}", slackChannelName);
                } else {
                    log.error("Started SlackConnection, channel {} not found", slackChannelName);
                }
            } catch (IOException e) {
                log.error("Failed to connect to slack: {}", e.getMessage());
            }
        }
    }
}
