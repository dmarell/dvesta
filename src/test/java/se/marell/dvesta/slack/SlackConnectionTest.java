/*
 * Created by Daniel Marell 10/01/16.
 */
package se.marell.dvesta.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SlackConnection.class})
@TestPropertySource(properties = {
        "dvesta.slack.channel: bot-corner",
        "dvesta.slack.authtoken: xoxb-17984717669-M6TVno5Js3U5ESUjNNkbWOgo"})
public class SlackConnectionTest {
    @Autowired
    private SlackConnection connection;

    @Ignore
    @Test
    public void test() throws Exception {
        connection.sendMessage("SlackConnectionTest text 1");
        connection.getSlackSession().addMessagePostedListener(
                new SlackMessagePostedListener() {
                    @Override
                    public void onEvent(SlackMessagePosted event, SlackSession session) {
                        System.out.println("getMessageContent(): " + event.getMessageContent());
                        System.out.println("getChannel().getName(): " + event.getChannel().getName());
                        System.out.println("getChannel().isDirect(): " + event.getChannel().isDirect());
                        System.out.println("getTimestamp(): " + event.getTimestamp());
                        System.out.println("getSender(): " + event.getSender().toString());
                    }
                });
        System.out.println("Waiting for messages...");
        Thread.sleep(1000000);
    }
}
