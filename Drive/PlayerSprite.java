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
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;

public class PlayerSprite extends BasicSprite {

  ///////////////
  // CONSTANTS //
  ///////////////

  double ACC = 0.1;
  double BRAKING = 3;
  double MAX_SPEED = 15;
  int WIDTH = 30;
  int HEIGHT = 45;

  Random rand = new Random();

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
  public void incVel(double i) {this.vel += i;}
  public void setVelX(double velX) {this.velX = velX;}
  public void incVelX(double i) {this.velX += i;}
  public void setVelY(double velY) {this.velY = velY;}
  public void incVelY(double i) {this.velY += i;}
  public void setMoveAngle(double moveAngle) {this.moveAngle = moveAngle;}
  public void incMoveAngle(double i) {
    this.moveAngle = (this.moveAngle + i);
    if (this.moveAngle < 0) {
      // beth noticed this is bad (what if -370?)
      this.moveAngle = 360 + this.moveAngle;
    }
  }
  public void setFaceAngle(double faceAngle) {this.faceAngle = faceAngle;}
  public void incFaceAngle(double i) {
    this.faceAngle = (this.faceAngle + i);
    if (this.faceAngle < 0) {
      // beth noticed this is bad (what if -370?)
      this.faceAngle = 360 + this.faceAngle;
    }
  }
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
  private int lightsKey;
  public int getLightsKey() {return lightsKey;}
  public void setLightsKey(int lightsKey) {this.lightsKey = lightsKey;}


  private int turn;
  public int getTurn() {return turn;}
  public void setTurn(int turn) {this.turn = turn;}

  private int acc;
  public int getAcc() {return acc;}
  public void setAcc(int acc) {this.acc = acc;}

  private int screechTime;
  public int getScreechTime() {return screechTime;}
  public void setScreechTime(int screechTime) {this.screechTime = screechTime;}
  public void incScreechTime(int i) {this.screechTime += i;}

  /////////////
  // STYLING //
  /////////////
  
  private boolean lights;
  public boolean getLights() {return lights;}
  public void setLights(boolean lights) {this.lights = lights;}
  public void toggleLights() {this.lights = !this.lights;}

  ////////////////
  // COLLISIONS //
  ////////////////

  private Shape bounds;
  public Area getArea() {
    // create a rectangle in the right place
    Shape rect = new Rectangle2D.Double(getX(), getY(), WIDTH, HEIGHT);

    // Create a transform. Rotate it and translate it accoring to faceAngle
    AffineTransform t = new AffineTransform();
    t.rotate(getFaceAngle(), getX(), getY());
    t.translate(-(WIDTH / 2), -(HEIGHT / 2));

    // create an area on this shape (with which to check .intersects())
    Area area = new Area(rect);

    // return transformed area
    return area.createTransformedArea(t);
  }
  public void setBounds(Shape bounds) {this.bounds = bounds;}
  
  private int colTime;
  public int getColTime() {return colTime;}
  public void setColTime(int colTime) {this.colTime = colTime;}
  public void incColTime(int i) {this.colTime += i;}

  private double slide;
  public double getSlide() {return slide;}
  public void setSlide(double slide) {this.slide = slide;}
  public void incSlide(double i) {this.slide += i;}

  private boolean sliding;
  public boolean isSliding() {return sliding;}
  public void setSliding(boolean sliding) {this.sliding = sliding;}
  public void toggleSliding() {this.sliding = !isSliding();}

  /////////////
  // SCORING //
  /////////////

  private int score;
  public int getScore() {return score;}
  public void setScore(int score) {this.score = score;}
  public void incScore(int i) {this.score += i;}

  //////////////////////////
  // PLAYERSPRITE METHODS //
  //////////////////////////

  public void paint(Graphics2D g2d) {
    g2d.translate(getX(), getY());
    g2d.rotate(Math.toRadians(getFaceAngle() + 90));
    
    // offset x and y for face angle
    g2d.translate(-(WIDTH / 2), -(HEIGHT / 2));

    // draw some tyres
    g2d.setColor(Color.BLACK);
    g2d.fillRect(-3, 1, 4, 10);
    g2d.fillRect(29, 1, 4, 10);
    g2d.fillRect(-3, 34, 4, 10);
    g2d.fillRect(29, 34, 4, 10);

    // draw the body
    g2d.setColor(getColor());
    g2d.fillRect(0, 0, WIDTH, HEIGHT);

    // highlight and outline the body
    g2d.setColor(Color.BLACK);
    g2d.drawRect(0, 18, 30, 15);
    g2d.drawRect(0, 0, WIDTH, HEIGHT);
  }

  public void drawLights(Graphics2D g2d) {
    if (getLights()) {
      // translate and rotate
      g2d.translate(getX(), getY());
      g2d.rotate(Math.toRadians(getFaceAngle() + 90));
      
      // offset x and y for face angle
      g2d.translate(-(WIDTH / 2), -(HEIGHT / 2));

      // 50% opacity
      Color c = new Color(1, 1, 1, 0.5f);
      g2d.setColor(c);

      // draw left beam
      int[] x1Coords = new int[] {2, 6, 18, -10};
      int[] y1Coords = new int[] {1, 1, -40, -40};
      g2d.fillPolygon(x1Coords, y1Coords, 4);
      // draw right beam
      int[] x2Coords = new int[] {24, 28, 40, 12};
      int[] y2Coords = new int[] {1, 1, -40, -40};
      g2d.fillPolygon(x2Coords, y2Coords, 4);
    }
  }

  public void updateVel() {
    if (!isSliding()) {
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
              // play sound
              // switch (rand.nextInt(3)) {
              //   case 0:
              //     Sound.SCREECH1.play();
              //     break;
              //   case 1:
              //     Sound.SCREECH2.play();
              //     break;
              //   case 2:
              //     Sound.SCREECH3.play();
              //     break;
              // }
            }
            else {
              incVel(-ACC);
            }
          }
          break;
      }
    }
    else {
      if (getVel() > 0) {
        incVel(-ACC);
      }
      else if (getVel() < 0) {
        incVel(ACC);
      }
    }

    // get rid of rounding errors
    if (Math.abs(getVel()) < 0.1) {
      setVel(0);
    }

    // calculate actual X and Y velocities
    if (getSlide() < 1) {

      // if just finished sliding
      if (isSliding()) {
        // make sure vel is pointing in the right direcion

        if (getVelX() != 0 && getVelY() != 0) {
          double targetAngle = Math.toDegrees(Math.atan(getVelY() / getVelX()));
          // mod doean't work :/
          if (targetAngle < 0) {
            targetAngle = 360 + targetAngle;
          }

          // horrible way to check face angle against true move angle
          if (getVelY() > 0 && targetAngle > 180) {
            targetAngle -= 180;
          }
          else if (getVelY() > 180 && targetAngle < 180) {
            targetAngle += 180;
          }

          if (getVelX() > 0) {
            if (targetAngle > 90 && targetAngle < 270) {
              targetAngle -= 180;
            }
          }
          else if (getVelX() < 0) {
            if (targetAngle < 90 || targetAngle > 270) {
              targetAngle -= 180;
            }
          }

          if (targetAngle < 0) {
            targetAngle = 360 + targetAngle;
          }
          
          // flip velocity if facing wrong way
          if (Math.abs(targetAngle - getFaceAngle()) > 90) {
            setVel(-getVel());
          }

          System.out.println((int)getVelX() + ", " + (int)getVelY() + " / " + getFaceAngle() + " / " + targetAngle);
        }

        // have now stopped sliding
        toggleSliding();
      }

      setVelX(getVel() * (Math.cos(Math.toRadians(getFaceAngle()))));
      setVelY(getVel() * (Math.sin(Math.toRadians(getFaceAngle()))));
    }
    // if sliding, turn car to face velocity vector (either forwards or backwards, whichever is closest)
    else {

      // calculate target faceAngle
      double targetAngle = Math.toDegrees(Math.atan(getVelY() / getVelX()));

      // need to increase or decrease faceAngle
      boolean increase = (targetAngle % 360) > (getFaceAngle() % 360);

      if (increase) {
        incFaceAngle(1);
      }
      else {
        incFaceAngle(-1);
      }
    }
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
    setTurn(0);
    setAcc(0);
    setLights(false);
    setColTime(0);
    setSlide(0);
    setSliding(false);
    setScore(0);
  }
}
