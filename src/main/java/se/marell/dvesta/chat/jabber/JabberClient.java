/*
 * Created by Daniel Marell 14-01-28 20:00
 */
package se.marell.dvesta.chat.jabber;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

/**
 * Jabber chat client. Sends and receives text messages over XMPP.
 */
public class JabberClient {

    public interface Listener {
        /**
         * Text message is received.
         *
         * @param message The test message
         */
        void messageReceived(String message);
    }

    public static class JabberClientException extends Exception {
        private JabberClientException(String s, Throwable throwable) {
            super(s, throwable);
        }
    }

    private ConnectionConfiguration config;
    private Chat chat;
    private XMPPConnection con;
    private String username;
    private String password;
    private Listener listener;

    /**
     * Create a client object to a jabber server connecting using the specified user information.
     *
     * @param jabberHost Jabber server, for example "jabber.se"
     * @param jabberPort Port on jabber server, for example 5222
     * @param username   User name on this jabber server, for example "mymachine"
     * @param password   Password for username
     * @param listener   Message listener. Can be null
     */
    public JabberClient(String jabberHost, int jabberPort, String username, String password,
                        Listener listener) {
        this.username = username;
        this.password = password;
        this.listener = listener;
        config = new ConnectionConfiguration(jabberHost, jabberPort);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        con = new XMPPTCPConnection(config);
    }

    /**
     * Create a chat connection to the specified user.
     *
     * @param toUser For example yourusername@jabber.se
     * @throws JabberClientException If connect or login failed
     */

    public void connect(String toUser) throws JabberClientException {
        if (con.isConnected()) {
            try {
                con.disconnect();
            } catch (SmackException.NotConnectedException ignore) {
            }
        }
        try {
            con.connect();
        } catch (SmackException | IOException | XMPPException e) {
            throw new JabberClientException("Could not connect", e);
        }
        if (!con.isAuthenticated()) {
            try {
                con.login(username, password);
            } catch (SmackException | IOException | XMPPException e) {
                throw new JabberClientException("Could not login", e);
            }
        }

        chat = ChatManager.getInstanceFor(con).createChat(toUser, new MessageListener() {
            public void processMessage(Chat chat, Message message) {
                if (listener != null) {
                    listener.messageReceived(message.getBody());
                }
            }
        });

        Presence presence = new Presence(Presence.Type.available);
        try {
            con.sendPacket(presence);
        } catch (SmackException.NotConnectedException e) {
            throw new JabberClientException("sendPacket failed", e);
        }
    }

    /**
     * Send a chat message.
     *
     * @param message The test to send
     * @throws JabberClientException If send failed
     */
    public void sendMessage(String message) throws JabberClientException {
        if (chat == null) {
            throw new IllegalStateException("Not connected. Call connect before calling sendMessage");
        }
        try {
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException | IllegalStateException | XMPPException e) {
            // According to api docs XMPPException should be thrown but current implementation throws IllegalStateException
            disconnect();
            throw new JabberClientException("Could not send message", e);
        }
    }

    /**
     * Disconnect chat connection.
     */
    public void disconnect() {
        chat = null;
        if (con != null) {
            try {
                con.disconnect();
            } catch (SmackException.NotConnectedException ignore) {
            }
        }
    }

    public boolean isConnected() {
        return chat != null;
    }

    public String getParticipant() {
        return chat.getParticipant();
    }
}
