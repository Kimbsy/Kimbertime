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
 * Player sprite class.
 */

import java.awt.*;
import java.awt.event.*;

public class PlayerSprite extends BasicSprite {

  ///////////////
  // CONSTANTS //
  ///////////////

  double ACC = 0.1;
  double BRAKING = 3;
  double MAX_SPEED = 15;
  int WIDTH = 30;
  int HEIGHT = 30;

  //////////////////////
  // BASIC PROPERTIES //
  //////////////////////

  private double vel;
  private double velX, velY;
  private double moveAngle, faceAngle;
  private Color color;

  ////////////////////////////
  // BASIC ACCESSOR METHODS //
  ////////////////////////////

  public double getVel() {return vel;}
  public double getVelX() {return velX;}
  public double getVelY() {return velY;}
  public double getMoveAngle() {return moveAngle;}
  public double getFaceAngle() {return faceAngle;}
  public Color getColor() {return color;}

  ///////////////////////////
  // BASIC MUTATOR METHODS //
  ///////////////////////////

  public void setVel(double vel) {this.vel = vel;}
  public void incVel(double vel) {this.vel += vel;}
  public void setVelX(double velX) {this.velX = velX;}
  public void incVelX(double velX) {this.velX += velX;}
  public void setVelY(double velY) {this.velY = velY;}
  public void incVelY(double velY) {this.velY += velY;}
  public void setMoveAngle(double moveAngle) {this.moveAngle = moveAngle;}
  public void incMoveAngle(double moveAngle) {this.moveAngle += moveAngle; this.moveAngle = (this.moveAngle % 360);}
  public void setFaceAngle(double faceAngle) {this.faceAngle = faceAngle;}
  public void incFaceAngle(double faceAngle) {this.faceAngle += faceAngle; this.faceAngle = (this.faceAngle % 360);}
  public void setColor(Color color) {this.color = color;}

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

  private int acc;
  public int getAcc() {return acc;}
  public void setAcc(int acc) {this.acc = acc;}
  


  //////////////////////////
  // PLAYERSPRITE METHODS //
  //////////////////////////

  public void paint(Graphics2D g2d) {
    g2d.translate(getX(), getY());
    g2d.rotate(Math.toRadians(getFaceAngle()));
    g2d.setColor(getColor());
    
    // offset for face angle
    g2d.translate(-(WIDTH / 2), -(HEIGHT / 2));
    g2d.fillRect(0, 0, WIDTH, HEIGHT);
  }

  public void updateVel() {
    switch(acc) {
      // if neither held tend towards 0
      case 0:
        if (getVel() > 0) {
          incVel(-ACC);
        }
        else if (getVel() < 0) {
          incVel(ACC);
        }
        break;
      // if forward speed up
      case 1:
        if (getVel() < MAX_SPEED) {
          incVel(ACC);
        }
        break;
      // if backward slow down
      case -1:
        if (getVel() > (-MAX_SPEED)) {
          // if going forward, brake hard
          if (getVel() > 0) {
            incVel(-(ACC * BRAKING));
          }
          else {
            incVel(-ACC);
          }
        }
        break;
    }

    // get rid of rounding errors
    if (Math.abs(getVel()) < 0.1) {
      setVel(0);
    }

    // calculate actual X and Y velocities
    setVelX(getVel() * (Math.cos(Math.toRadians(getFaceAngle()))));
    setVelY(getVel() * (Math.sin(Math.toRadians(getFaceAngle()))));
  }

  public void updatePos() {
    incX(getVelX());
    incY(getVelY());
  }

  /////////////////////////
  // DEFAULT CONSTRUCTOR //
  /////////////////////////

  PlayerSprite() {
    setVelX(0.0);
    setVelY(0.0);
    setMoveAngle(0.0);
    setFaceAngle(0.0);
    setColor(Color.RED);
    setUpKey(KeyEvent.VK_UP);
    setDownKey(KeyEvent.VK_DOWN);
    setLeftKey(KeyEvent.VK_LEFT);
    setRightKey(KeyEvent.VK_RIGHT);
    setTurn(0);
    setAcc(0);
  }
}