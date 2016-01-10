/*
 * Created by Daniel Marell 14-02-16 22:35
 */
package se.marell.dvesta.chat.chatstub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import se.marell.dvesta.chat.chatapi.TextChatConnection;

@Service
@Qualifier("stub")
public class ChatStub implements TextChatConnection {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void addListener(MessageListener listener) {
    }

    @Override
    public void removeListener(MessageListener listener) {
    }

    @Override
    public void sendMessage(String message) {
        log.info("chatstub.sendMessage:" + message);
    }

    @Override
    public String getParticipant() {
        return "log";
    }
}
