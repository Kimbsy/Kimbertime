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
 * Main Game class.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Drive extends JFrame implements Runnable, MouseListener, KeyListener {

  ///////////
  // SETUP //
  ///////////

  // main thread used for the game loop
  Thread gameLoop;

  // backBuffer
  BufferedImage backBuffer;
  
  // main drawing object for backBuffer
  Graphics2D g2d;

  // frame dimensions
  int w = 900;
  int h = 600;

  // create sprite ArrayList
  List<PlayerSprite> sprites = Collections.synchronizedList(new ArrayList<PlayerSprite>());
  int PLAYERS = 2;

  // create level ArrayList
  List<Level> levels = Collections.synchronizedList(new ArrayList<Level>());
  int levelID = 0;

  // create identity transform
  AffineTransform identity = new AffineTransform();

  // create random number generator
  Random rand = new Random();

  // MouseListener variables
  int mouseX, mouseY;
  int mouseButton;

  // font for writing things
  Font font = new Font("Courier", Font.PLAIN, 12);

  // framerate counters and other iming variables
  int frameRate = 0, frameCount = 0;
  long startTime = System.currentTimeMillis();

  // main
  public static void main(String[] args) {
    new Drive();
  }

  // default constructor
  public Drive() {
    super("Drive");
    setSize(w, (h + 32)); // 32 is for JFrame top bar
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameLoop = new Thread(this);
    gameLoop.start();
    init();
  }

  // application init event
  public void init() {
    // main page
    // @TODO create the main page [PLAY, SETTINGS, CREDITS]

    // create things
    for (int i = 0; i < PLAYERS; i++) {
      PlayerSprite s = new PlayerSprite();
      s.setX((w / 2) + (100 * i));
      s.setY((h / 2) + (100));

      // @TODO this is temporory
      if (i > 0) {
        s.setColor(Color.GREEN);
        s.setUpKey(KeyEvent.VK_COMMA);
        s.setDownKey(KeyEvent.VK_O);
        s.setLeftKey(KeyEvent.VK_A);
        s.setRightKey(KeyEvent.VK_E);
        s.setLightsKey(KeyEvent.VK_SPACE);
      }
      sprites.add(s);
    }

    // create levels
    loadLevels();

    // create the backBuffer for some smooth-ass graphics
    backBuffer = new BufferedImage((w * 2), (h * 2), BufferedImage.TYPE_INT_RGB);
    g2d = backBuffer.createGraphics();

    // listen for mice (they're around here somewhere)
    addMouseListener(this);
    // get ready for some keypresses
    addKeyListener(this);
  }

  public void loadLevels() {
    // @TODO temporary grid need multiple levels
    Level l = new Level();
    l.setWidth(w * 2);
    l.setHeight(h * 2);
    levels.add(l);
  }

  ///////////
  // LOOPS //
  ///////////

  // repaint event draws the backBuffer
  public void paint(Graphics g) {
    // draw the backBuffer to the window
    g.drawImage(backBuffer, (w / 2) - (int)getAverageX(), (h / 2) - (int)getAverageY(), this);

    // start all transforms at the identity
    g2d.setTransform(identity);

    // erase the background
    g2d.setColor(Color.GRAY);
    g2d.fillRect(-w, -h, (w * 4), (h * 4));

    // draw the level
    drawLevel();

    // draw the sprites
    drawSprites();
  }

  public void drawLevel() {
    // get the level based on levelID
    Level l = levels.get(levelID);

    // translate background
    g2d.setTransform(identity);

    // draw the level
    l.paint(g2d);
  }

  public void drawSprites() {
    // loop through the sprites to draw the lights (want the under all sprites)
    for (int i = 0; i < sprites.size(); i++) {
      // load the sprite
      PlayerSprite s = sprites.get(i);

      // always draw from identity transform
      g2d.setTransform(identity);
      // draw lights
      s.drawLights(g2d);
    }
    // loop through again for rest of sprite
    for (int i = 0; i < sprites.size(); i++) {
      // load the sprite
      PlayerSprite s = sprites.get(i);

      // always draw from identity transform
      g2d.setTransform(identity);
      // draw the sprite
      s.paint(g2d);
    }
  }

  public double getAverageX() {
    // get average player position
    double sumX = 0;

    for (int i = 0; i < sprites.size(); i++) {
      PlayerSprite s = sprites.get(i);
      sumX += s.getX();
    }

    double averageX = sumX / sprites.size();

    return averageX;
  }

  public double getAverageY() {
    // get average player position
    double sumY = 0;

    for (int i = 0; i < sprites.size(); i++) {
      PlayerSprite s = sprites.get(i);
      sumY += s.getY();
    }

    double averageY = sumY / sprites.size();

    return averageY;
  }

  public void run() {
    // aquire the current thread
    Thread t = Thread.currentThread();

    // kep going as long as the thread is alive
    while (t == gameLoop) {
      try {
        gameUpdate();
        // target framerate is 50fps
        Thread.sleep(20);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      repaint();
    }
  }

  // stop thread event
  public void stop() {
    // kill the gameLoop thread
    gameLoop = null;
  }

  // move and animate objects in the game
  public void gameUpdate() {
    updateSprites();
    updateLevel();
    checkCollisions();
    checkVictoryConditions();
  }

  public void updateSprites() {
    // loop through sprites
    for (int i = 0; i < sprites.size(); i++) {
      // load the sprite
      PlayerSprite s = sprites.get(i);

      // update faceAngle
      s.incFaceAngle((s.getTurn() * 5));

      // update velocity
      s.updateVel();

      // update position
      s.updatePos();

      // decay collision timer
      s.incColTime(-1);
    }
  }

  public void updateLevel() {
    // @TODO this (ever gonna need this?)
  }

  public void checkCollisions() {
    // @TODO check for collisions between players, bump them if true
    
    for (int i = 0; i < sprites.size(); i++) {
      PlayerSprite s1 = sprites.get(i);
      for (int j = 0; j < sprites.size(); j++) {
        PlayerSprite s2 = sprites.get(j);

        if (i != j && (s1.getColTime() < 0) && (s2.getColTime() < 0)) {
          if (isCollision(s1.getX(), s2.getX(), s1.getY(), s2.getY(), 15)) {
            // temporary values so they can switch velocitities and directions
            double tempX = s1.getVelX();
            double tempY = s1.getVelY();
            double tempFace = s1.getFaceAngle();

            s1.setVelX(s2.getVelX());
            s1.setVelY(s2.getVelY());
            s1.setFaceAngle(s2.getFaceAngle());
            s2.setVelX(tempX);
            s2.setVelY(tempY);
            s2.setFaceAngle(tempFace);

            // update so positions change to avoid clipping
            updateSprites();

            // collision delay
            s1.setColTime(10);
            s2.setColTime(10);
          }
        }
      }
    }
  }

  public boolean isCollision(double x1, double x2, double y1, double y2, double r) {
    final double a = 2 * r;
    final double dx = x1 - x2;
    final double dy = y1 - y2;
    return a * a > (dx * dx + dy * dy);
}

  public void checkVictoryConditions() {
    // @TODO check if a player has won (too far ahead, crossed finish line)
  }

  ////////////
  // INUPUT //
  ////////////

  // custom method to get mouse button status
  public void checkButton(MouseEvent e) {
    switch(e.getButton()) {
      case MouseEvent.BUTTON1:
        mouseButton = 1;
        break;
      case MouseEvent.BUTTON2:
        mouseButton = 2;
        break;
      case MouseEvent.BUTTON3:
        mouseButton = 3;
        break;
      default:
        mouseButton = 0;
    }
  }

  ///////////////////////////
  // MOUSELISTENER METHODS //
  ///////////////////////////

  public void mouseClicked(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}

  /////////////////////////
  // KEYLISTENER METHODS //
  /////////////////////////

  public void keyPressed(KeyEvent e) {
    // check sprites for controls
    for (int i = 0; i < sprites.size(); i++) {
      int key = e.getKeyCode();

      PlayerSprite s = sprites.get(i);

      if (key == s.getUpKey()) {
        s.setAcc(1);
      }
      if (key == s.getDownKey()) {
        s.setAcc(-1);
      }
      if (key == s.getLeftKey()) {
        // set turning varaible
        s.setTurn(-1);
      }
      if (key == s.getRightKey()) {
        // set turning varaible
        s.setTurn(1);
      }
      if (key == s.getLightsKey()) {
        s.toggleLights();
      }
    }
  }
  public void keyReleased(KeyEvent e) {
    // check sprites for controls
    for (int i = 0; i < sprites.size(); i++) {
      int key = e.getKeyCode();

      PlayerSprite s = sprites.get(i);

      if (key == s.getUpKey()) {
        // unset acc variable if already acc this way
        if (s.getAcc() == 1) {
          s.setAcc(0);
        }
      }
      if (key == s.getDownKey()) {
        // unset acc variable if already acc this way
        if (s.getAcc() == -1) {
          s.setAcc(0);
        }
      }
      if (key == s.getLeftKey()) {
        // unset turning variable if already turning this way
        if (s.getTurn() == -1) {
          s.setTurn(0);
        }
      }
      if (key == s.getRightKey()) {
        // unset turning variable if already turning this way
        if (s.getTurn() == 1) {
          s.setTurn(0);
        }
      }
    }
  }
  public void keyTyped(KeyEvent e) {}


}