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
 * Driving game inspired by 'Micro Machines' (I believe it was on the SEGA Megadrive).
 *
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
      s.setX(50 * (i + 1));
      s.setY(50);

      // @TODO this is temporory
      if (i > 0) {
        s.setColor(Color.GREEN);
        s.setUpKey(KeyEvent.VK_COMMA);
        s.setDownKey(KeyEvent.VK_O);
        s.setLeftKey(KeyEvent.VK_A);
        s.setRightKey(KeyEvent.VK_E);
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
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, (w * 2), (h * 2));

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
    // loop through the sprites
    for (int i = 0; i < sprites.size(); i++) {
      // load the sprite
      PlayerSprite s = sprites.get(i);

      // always draw from identity transform
      g2d.setTransform(identity);

      // draw it
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
    }
  }

  public void updateLevel() {
    // @TODO this (ever gonna need this?)
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