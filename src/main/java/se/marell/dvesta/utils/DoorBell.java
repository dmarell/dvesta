/*
 * Created by Daniel Marell 2011-08-18 20:44
 */
package se.marell.dvesta.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.marell.dcommons.sound.AudioException;
import se.marell.dcommons.sound.SoundClip;
import se.marell.dcommons.sound.SoundPlayerDevice;
import se.marell.dvesta.ioscan.BitInput;
import se.marell.dvesta.tickengine.TickConsumer;

import java.io.File;

public class DoorBell implements TickConsumer {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private SoundClip player;
    private BitInput doorBellPushButton;
    private boolean prevDoorBellState;
    private SoundPlayerDevice soundPlayerDevice;
    private File soundFile;
    private float volume;

    public DoorBell(final BitInput doorBellPushButton, String soundPlaybackDevicePattern, float volume, File soundFile) {
        this.doorBellPushButton = doorBellPushButton;
        this.soundFile = soundFile;

        player = new SoundClip(soundFile, new SoundClip.Listener() {
            @Override
            public void ready() {
                log.info("SoundClipPlayer " + doorBellPushButton.getName() + " ready:" + soundPlayerDevice);
            }
        });
        if (volume != Float.NaN) {
            this.volume = volume;
        }

        soundPlayerDevice = SoundPlayerDevice.createSoundPlayerDevice(soundPlaybackDevicePattern);
        if (soundPlayerDevice != null) {
            log.info("DoorBell: Matched sound player device " + soundPlaybackDevicePattern);
        } else {
            log.info("Could not find specified sound playback device matching pattern \"" + soundPlaybackDevicePattern + "\". Using default");
        }
    }

    @Override
    public String getName() {
        return "doorbell";
    }

    @Override
    public void executeTick() {
        boolean doorBellState = doorBellPushButton.getInputStatus();
        if (doorBellState) {
            if (!prevDoorBellState) {
                // Button was pushed
                log.info("doorBellPushButton pressed, playing " + soundFile.getName() + " on device " + soundPlayerDevice + ",volume " + volume);

                try {
                    player.play(soundPlayerDevice, volume);
                } catch (AudioException e) {
                    log.info("Play failed:" + soundPlayerDevice, e);
                }
            }
        }
        prevDoorBellState = doorBellState;
    }
}
