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

import java.io.*;
import sun.audio.*;

public class Sound {

  private AudioStream audioStream;

  private String fileName;

  public Sound(String fileName) {

    this.fileName = fileName;

    try {
      InputStream inputStream = getClass().getResourceAsStream(fileName);
      audioStream = new AudioStream(inputStream);
    }
    catch (Exception e) { }
  }

  public void play() {
    AudioPlayer.player.start(audioStream);
  }

  public void stop() {
    AudioPlayer.player.stop(audioStream);
    rewind();
  }

  public void rewind() {
    try {
      InputStream inputStream = getClass().getResourceAsStream(fileName);
      audioStream = new AudioStream(inputStream);
    }
    catch (Exception e) { }
  }
}
