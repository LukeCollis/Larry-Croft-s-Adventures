package Renderer;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30]; //stores the audio
    public Sound(){
        soundURL[0]=getClass().getResource("/Resources/level1.wav");
        soundURL[1]=getClass().getResource("/Resources/level2.wav");
        soundURL[2]=getClass().getResource("/Resources/treasure.wav");
        soundURL[3]=getClass().getResource("/Resources/treasure2.wav");
        soundURL[4]=getClass().getResource("/Resources/bomb.wav");
        soundURL[5]=getClass().getResource("/Resources/vent.wav");
        soundURL[6]=getClass().getResource("/Resources/blank.wav");
    }

    /**
     *
     * @param i takes int i that determine which clip to be played
     *          setfile which file to be played
     */
    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // these methe help play the sound track
    public void play(){clip.start();}  //play audio once
    public void loop(){clip.loop(Clip.LOOP_CONTINUOUSLY);} //set the current playing audio to loop
    public void stop(){clip.stop();} // stop current playing

    /**
     * Adjust the volume of the currently playing clip.
     *
     * @param value the volume level in decibels (-80.0f to 6.0f)
     *              -80.0f is effectively mute, and 6.0f is the max volume.
     */
    public void setVolume(float value) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(value);
        }
    }

    /**
     *
     * @param increment
     * takes an float and will increase audio by the increment
     */
    public void increaseVolume(float increment) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float newVolume = gainControl.getValue() + increment;
            gainControl.setValue(Math.min(newVolume, gainControl.getMaximum())); // Ensure volume doesn't exceed the max limit
        }
    }

    /**
     *
     * @param decrement
     * decrease audio by the float that it takes
     */
    public void decreaseVolume(float decrement) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float newVolume = gainControl.getValue() - decrement;
            gainControl.setValue(Math.max(newVolume, gainControl.getMinimum())); // Ensure volume doesn't go below the minimum limit
        }
    }
}


