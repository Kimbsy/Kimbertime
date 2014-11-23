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
 * Button class.
 */

import java.awt.*;
import java.awt.Image;

public class Button extends BasicSprite {

  //////////////////////
  // BASIC PROPERTIES //
  //////////////////////

  int width;
  int height;
  private String description;
  private String action;
  private Image image;

  //////////////////////
  // ACCESSOR METHODS //
  //////////////////////

  public int getWidth() {return width;}
  public int getHeight() {return height;}
  public String getDescription() {return description;}
  public String getAction() {return action;}
  public Image getImage() {return image;}

  /////////////////////
  // MUTATOR METHODS //
  /////////////////////

  public void setWidth(int width) {this.width = width;}
  public void setHeight(int height) {this.height = height;}
  public void setDescription(String description) {this.description = description;}
  public void setAction(String action) {this.action = action;}
  public void setImage(Image image) {this.image = image;}

  ////////////////////
  // BUTTON METHODS //
  ////////////////////

  public void paint(Graphics2D g2d) {
    // move into position
    g2d.translate(getX(), getY());

    // draw the button
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, getWidth(), getHeight());

    // write the description
    g2d.setColor(Color.BLACK);
    g2d.drawString(getDescription(), 20, 50);

    // move back
    g2d.translate(-getX(), -getY());
  }

  public String checkClick(int x, int y) {
    // check x coord
    if ((x > getX()) && x < (getX() + 200)) {
      // check y coord
      if ((y > getY()) && y < (getY() + 80)) {
        return getAction();
      }
    }

    return "NO";
  }

  ////////////////////////
  // DEFAULT CONSTUCTOR //
  ////////////////////////

  Button() {
    setWidth(200);
    setHeight(80);
    setDescription("");
    setAction("");
    setImage(null);
  }
}