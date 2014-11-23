import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
    public static final AudioClip MENU = Applet.newAudioClip(Sound.class.getResource("sound/music/elevatorMusic.wav"));

    public static final AudioClip BUMP1 = Applet.newAudioClip(Sound.class.getResource("sound/collision/bump1.wav"));
    public static final AudioClip BUMP2 = Applet.newAudioClip(Sound.class.getResource("sound/collision/bump2.wav"));
    public static final AudioClip BUMP3 = Applet.newAudioClip(Sound.class.getResource("sound/collision/bump3.wav"));

    public static final AudioClip ENGINE1 = Applet.newAudioClip(Sound.class.getResource("sound/engine/engine1.wav"));
    public static final AudioClip ENGINE2 = Applet.newAudioClip(Sound.class.getResource("sound/engine/engine2.wav"));
    public static final AudioClip ENGINE3 = Applet.newAudioClip(Sound.class.getResource("sound/engine/engine3.wav"));

    public static final AudioClip HONK1 = Applet.newAudioClip(Sound.class.getResource("sound/horn/honk1.wav"));

    public static final AudioClip SCREECH1 = Applet.newAudioClip(Sound.class.getResource("sound/tyres/screech1.wav"));
    public static final AudioClip SCREECH2 = Applet.newAudioClip(Sound.class.getResource("sound/tyres/screech2.wav"));
    public static final AudioClip SCREECH3 = Applet.newAudioClip(Sound.class.getResource("sound/tyres/screech3.wav"));

    // public static final AudioClip GAME = Applet.newAudioClip(Sound.class.getResource("sound/music/theMetal.wav"));
}