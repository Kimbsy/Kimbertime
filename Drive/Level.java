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
 * Level class.
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class Level extends BasicSprite {

  //////////////////////
  // BASIC PROPERTIES //
  //////////////////////
  
  private int width;
  private int height;
  private BufferedImage image;
  private int numLaps;

  ////////////////////////////
  // BASIC ACCESSOR METHODS //
  ////////////////////////////
  
  public int getWidth() {return width;}
  public int getHeight() {return height;}
  public BufferedImage getImage() {return image;}
  public int getNumLaps() {return numLaps;}

  ///////////////////////////
  // BASIC MUTATOR METHODS //
  ///////////////////////////

  public void setWidth(int width) {this.width = width;}
  public void setHeight(int height) {this.height = height;}
  public void setImage(BufferedImage image) {this.image = image;}
  public void setNumLaps(int numLaps) {this.numLaps = numLaps;}

  ///////////////////////
  // GAMEPLAY BOOLEANS //
  ///////////////////////

  public boolean damage;
  public boolean damageIsOn() {return damage;}
  public void setDamage(boolean damage) {this.damage = damage;}

  public boolean cameraMove;
  public boolean cameraShouldMove() {return cameraMove;}
  public void setCameraMove(boolean cameraMove) {this.cameraMove = cameraMove;}

  public boolean lapRace;
  public boolean isLapRace() {return lapRace;}
  public void setLapRace(boolean lapRace) {this.lapRace = lapRace;}

  ///////////////////
  // LEVEL METHODS //
  ///////////////////

  public void paint(Graphics2D g2d) {
    // draw level image
    g2d.drawImage(getImage(), 0, 0, null);
  }

  /////////////////////////
  // DEFAULT CONSTRUCTOR //
  /////////////////////////

  Level() {
    setWidth(0);
    setHeight(0);
    setNumLaps(3);
    setDamage(false);
    setCameraMove(true);
    setLapRace(true);
  }
}
