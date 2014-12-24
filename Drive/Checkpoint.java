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
 * Checkpoint class for deciding leader, respawn points and victory conditions
 */

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;

public class Checkpoint extends Wall {

  // checkpoints get crossed in order
  private int delta;
  public int getDelta() {return delta;}
  public void setDelta(int delta) {this.delta = delta;}

  // spawn points
  private int[] spawnXs = new int[4];
  public int[] getSpawnXs() {return spawnXs;}

  private int[] spawnYs = new int[4];
  public int[] getSpawnYs() {return spawnYs;}

  public void setSpawns() {
    // set spawn points
    int midX = (int)(getX() + (getWidth() / 2));
    int midY = (int)(getY() + (getHeight() / 2));

    spawnXs[0] = midX + 50;
    spawnXs[1] = midX + 50;
    spawnXs[2] = midX - 50;
    spawnXs[3] = midX - 50;

    spawnYs[0] = midY - 50;
    spawnYs[1] = midY + 50;
    spawnYs[2] = midY - 50;
    spawnYs[3] = midY + 50;
  }

  private int mostRecentSpawn;
  public int getMostRecentSpawn() {return mostRecentSpawn;}
  public void setMostRecentSpawn(int mostRecentSpawn) {this.mostRecentSpawn = mostRecentSpawn;}
  public void incMostRecentSpawn(int i) {this.mostRecentSpawn = (mostRecentSpawn + i) % 4;}

  // orientation
  private String direction;
  public String getDirection() {return direction;}
  public void setDirection(String direction) {this.direction = direction;}


  /////////////////////////
  // DEFAULT CONSTRUCTOR //
  /////////////////////////

  Checkpoint() {
    setColor(Color.BLUE);
    setLethal(false);
    setDelta(0);
    setMostRecentSpawn(0);
    setDirection("");
  }
}
