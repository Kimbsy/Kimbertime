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

public class Level extends BasicSprite {

  //////////////////////
  // BASIC PROPERTIES //
  //////////////////////
  
  int width;
  int height;

  ////////////////////////////
  // BASIC ACCESSOR METHODS //
  ////////////////////////////
  
  public int getWidth() {return width;}
  public int getHeight() {return height;}

  ///////////////////////////
  // BASIC MUTATOR METHODS //
  ///////////////////////////

  public void setWidth(int width) {this.width = width;}
  public void setHeight(int height) {this.height = height;}

  ///////////////////
  // LEVEL METHODS //
  ///////////////////

  public void paint(Graphics2D g2d) {

    // @TODO grid is temporary

    g2d.setColor(Color.BLUE);
    
    int numSqX = getWidth() / 30;
    int numSqY = getHeight() / 30;

    for (int i = 0; i < numSqX; i++) {
      for (int j = 0; j < numSqY; j++) {
        g2d.drawRect(((i + 1) * 30), ((j + 1) * 30), 30, 30);
      }
    }
  }

  /////////////////////////
  // DEFAULT CONSTRUCTOR //
  /////////////////////////

  Level() {
    setWidth(0);
    setHeight(0);
  }
}
