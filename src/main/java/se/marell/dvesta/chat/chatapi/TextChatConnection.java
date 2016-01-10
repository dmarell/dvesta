/*
 * Created by Daniel Marell 14-01-28 21:38
 */
package se.marell.dvesta.chat.chatapi;

public interface TextChatConnection {
    interface MessageListener {
        void messageReceived(String message);
    }

    void addListener(MessageListener listener);

    void removeListener(MessageListener listener);

    void sendMessage(String message);

    String getParticipant();
}