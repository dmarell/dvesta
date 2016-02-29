/*
 * Created by Daniel Marell 07/01/16.
 */
package se.marell.dvesta.chat.slack;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.marell.dvesta.chat.chatapi.TextChatConnection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SlackChatConnection.class})
@TestPropertySource(properties = {
        "dvesta.slack.channel: bot-corner",
        "dvesta.slack.authtoken: xxxxxxxxxxxx"})
public class SlackChatConnectionTest {
    @Autowired
    private SlackChatConnection connection;

    @Ignore
    @Test
    public void shouldSendMessage() throws Exception {
        connection.sendMessage("Message text 1");
        connection.addListener(new TextChatConnection.MessageListener() {
            @Override
            public void messageReceived(String message) {
                System.out.println("Mottagit: " + message);
            }
        });
        System.out.println("Waiting for messages...");
        Thread.sleep(1000000);
    }
}
