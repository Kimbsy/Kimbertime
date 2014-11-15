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

import java.awt.*;
import java.awt.event.*;

public class BasicSprite {

  //////////////////////
  // BASIC PROPERTIES //
  //////////////////////

  private double x, y;
  private double velX, velY;
  private double moveAngle, faceAngle;

  ////////////////////////////
  // BASIC ACCESSOR METHODS //
  ////////////////////////////

  public double getX() {return x;}
  public double getY() {return y;}
  public double getVelX() {return velX;}
  public double getVelY() {return velY;}
  public double getMoveAngle() {return moveAngle;}
  public double getFaceAngle() {return faceAngle;}

  ///////////////////////////
  // BASIC MUTATOR METHODS //
  ///////////////////////////

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

  //////////////
  // CONTROLS //
  //////////////

  private int upKey;
  public int getUpKey() {return upKey;}
  public void setUpKey(int upKey) {this.upKey = upKey;}
  private int downKey;
  public int getDownKey() {return downKey;}
  public void setDownKey(int downKey) {this.downKey = downKey;}
  private int leftKey;
  public int getLeftKey() {return leftKey;}
  public void setLeftKey(int leftKey) {this.leftKey = leftKey;}
  private int rightKey;
  public int getRightKey() {return rightKey;}
  public void setRightKey(int rightKey) {this.rightKey = rightKey;}

  private int turn;
  public int getTurn() {return turn;}
  public void setTurn(int turn) {this.turn = turn;}

  private int accel;
  public int getAccel() {return accel;}
  public void setAccel(int accel) {this.accel = accel;}
  


  ////////////////////
  // SPRITE METHODS //
  ////////////////////

  public void paint(Graphics2D g2d) {
    g2d.translate(x, y);
    g2d.rotate(Math.toRadians(faceAngle));
    g2d.setColor(Color.RED);

    //***************************************************
    // NEEDS TO OFSET FOR FACE ANGLE
    //***************************************************

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
    setTurn(0);
    setUpKey(KeyEvent.VK_UP);
    setDownKey(KeyEvent.VK_DOWN);
    setLeftKey(KeyEvent.VK_LEFT);
    setRightKey(KeyEvent.VK_RIGHT);
  }
}