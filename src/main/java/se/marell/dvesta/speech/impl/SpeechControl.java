/*
 * Created by Daniel Marell 2011-09-14 21:53
 */
package se.marell.dvesta.speech.impl;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.marell.dvesta.chat.chatapi.TextChatConnection;
import se.marell.dvesta.speech.TextToSpeech;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class SpeechControl implements TextToSpeech, ServletContextListener {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String VOICE_NAME = "kevin16";
    private Voice voice;

    @Autowired
    private TextChatConnection chatConnection;

    @Override
    public void speakText(String text, float volume) {
        if (voice != null) {
            log.info("speakText: " + text);
//      JavaClipAudioPlayer player = new JavaClipAudioPlayer();
//      voice.setAudioPlayer(player);
            voice.getAudioPlayer().setVolume(volume);
            voice.speak(text);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Starting " + getName());

        try {
            voice = VoiceManager.getInstance().getVoice(VOICE_NAME);
            if (voice == null) {
                log.error("Cannot find a voice named " + VOICE_NAME);
                return;
            }
        } catch (Throwable e) {
            log.error("Failed to load module " + getName(), e);
            return;
        }

        voice.allocate();

        // Speak messages starting with "tala " or "talk "
        chatConnection.addListener(new TextChatConnection.MessageListener() {
            @Override
            public void messageReceived(String message) {
                final String cmdPrefix = "tala";
                if (message.toLowerCase().startsWith(cmdPrefix)) {
                    speakText(message.substring(cmdPrefix.length()), 1.0f);
                }
                final String cmdPrefix2 = "talk";
                if (message.toLowerCase().startsWith(cmdPrefix2)) {
                    speakText(message.substring(cmdPrefix2.length()), 1.0f);
                }
            }
        });

        log.info("Started " + getName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("deactivating " + getName());
        voice.deallocate();
        log.info("deactivated " + getName());
    }

    public String getName() {
        return "speechcontrol";
    }
}
