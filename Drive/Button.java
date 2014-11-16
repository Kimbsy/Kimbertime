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

import java.awt.Image;

public class Button extends BasicSprite {

  //////////////////////
  // BASIC PROPERTIES //
  //////////////////////

  private String description;
  private Image image;

  //////////////////////
  // ACCESSOR METHODS //
  //////////////////////

  public String getDescription() {return description;}
  public Image getImage() {return image;}

  /////////////////////
  // MUTATOR METHODS //
  /////////////////////

  public void setDescription(String description) {this.description = description;}
  public void setImage(Image image) {this.image = image;}

  ////////////////////////
  // DEFAULT CONSTUCTOR //
  ////////////////////////

  Button() {

  }
}