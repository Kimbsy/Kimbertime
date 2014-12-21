/***************************************************************************
* Copyright 2014 Dave Kimber
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
***************************************************************************/

/**
 * Sound class.
 */

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

    // This one doesn't work, too big?

    public static final AudioClip GAME = Applet.newAudioClip(Sound.class.getResource("sound/music/theMetal.wav"));
}
