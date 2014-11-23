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
 * Basic sprite class.
 */

public class BasicSprite {

  //////////////////////
  // BASIC PROPERTIES //
  //////////////////////

  private double x, y;

  ////////////////////////////
  // BASIC ACCESSOR METHODS //
  ////////////////////////////
  
  public double getX() {return x;}
  public double getY() {return y;}

  ///////////////////////////
  // BASIC MUTATOR METHODS //
  ///////////////////////////

  public void setX(double x) {this.x = x;}
  public void incX(double x) {this.x += x;}
  public void setY(double y) {this.y = y;}
  public void incY(double y) {this.y += y;}

  ////////////////////////
  // DEFAULT CONSTUCTOR //
  ////////////////////////

  BasicSprite() {
    setX(0.0);
    setY(0.0);
  }
}
