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
  private Image image;

  //////////////////////
  // ACCESSOR METHODS //
  //////////////////////

  public int getWidth() {return width;}
  public int getHeight() {return height;}
  public String getDescription() {return description;}
  public Image getImage() {return image;}

  /////////////////////
  // MUTATOR METHODS //
  /////////////////////

  public void setWidth(int width) {this.width = width;}
  public void setHeight(int height) {this.height = height;}
  public void setDescription(String description) {this.description = description;}
  public void setImage(Image image) {this.image = image;}

  ////////////////////
  // BUTTON METHODS //
  ////////////////////

  public void paint(Graphics2D g2d) {

  }

  ////////////////////////
  // DEFAULT CONSTUCTOR //
  ////////////////////////

  Button() {
    setWidth(0);
    setHeight(0);
    setDescription("");
    setImage(null);
  }
}