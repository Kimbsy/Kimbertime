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
 * basic sprite class
 */

import java.awt.*;

public class BasicSprite {

  ////////////////
  // properties //
  ////////////////

  private double x, y;
  private double velX, velY;
  private double moveAngle, faceAngle;

  //////////////////////
  // accessor methods //
  //////////////////////

  public double getX() {return x;}
  public double getY() {return y;}
  public double getVelX() {return velX;}
  public double getVelY() {return velY;}
  public double getMoveAngle() {return moveAngle;}
  public double getFaceAngle() {return faceAngle;}

  /////////////////////
  // mutator methods //
  /////////////////////

  public void setX(double x) {this.x = x;}
  public void incX(double x) {this.x += x;}
  public void setY(double y) {this.y = y;}
  public void incY(double y) {this.y += y;}
  public void setVelX(double velX) {this.velX = velX;}
  public void incVelX(double velX) {this.velX += velX;}
  public void setVelY(double velY) {this.velY = velY;}
  public void incVelY(double velY) {this.velY += velY;}
  public void setMoveAngle(double moveAngle) {this.moveAngle = moveAngle;}
  public void incMoveAngle(double moveAngle) {this.moveAngle += moveAngle;}
  public void setFaceAngle(double faceAngle) {this.faceAngle = faceAngle;}
  public void incFaceAngle(double faceAngle) {this.faceAngle += faceAngle;}

  ////////////////////
  // sprite methods //
  ////////////////////

  public void paint(Graphics2D g2d) {
    g2d.translate(x, y);
    g2d.rotate(Math.toRadians(faceAngle));
    g2d.setColor(Color.RED);
    g2d.fillRect(0, 0, 30, 30);
  }

  public void move() {

  }

  /////////////////////////
  // default constructor //
  /////////////////////////

  BasicSprite() {
    setX(0.0);
    setY(0.0);
    setVelX(0.0);
    setVelY(0.0);
    setMoveAngle(0.0);
    setFaceAngle(0.0);
  }
}