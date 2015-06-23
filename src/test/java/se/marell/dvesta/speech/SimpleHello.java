/*
 * Created by Daniel Marell 14-02-01 17:09
 */
package se.marell.dvesta.speech;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * Simple program to demonstrate the use of the FreeTTS speech
 * synthesizer.  This simple program shows how to use FreeTTS
 * without requiring the Java Speech API (JSAPI).
 */
public class SimpleHello {
    private static final String VOICE_NAME = "kevin16";

    public static void main(String[] args) {
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice helloVoice = voiceManager.getVoice(VOICE_NAME);
        if (helloVoice == null) {
            System.err.println("Cannot find a voice named " + VOICE_NAME + ".  Please specify a different voice.");
            return;
        }
        helloVoice.allocate();
        helloVoice.speak("Thank you for giving me a voice. I'm so glad to say hello to this world.");
        helloVoice.deallocate();
    }
}
