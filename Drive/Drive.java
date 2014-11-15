/***************************************************************************
* Copyright 2013 Dave Kimber
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
 * Driving game inspired by 'Micro Machines' (I believe it was on the SEGA Megadrive)
 */

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.util.*;

// main game class
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

  // boolean toggles
  /*****************************************************************/

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
    // create things
    /*****************************************************************/

    // create the backBuffer for some smooth-ass graphics
    backBuffer = new BufferedImage(w+200, h, BufferedImage.TYPE_INT_RGB);
    g2d = backBuffer.createGraphics();

    // listen for mice (they're around here somewhere)
    addMouseListener(this);
    // get ready for some keypresses
    addKeyListener(this);
  }

  ///////////
  // LOOPS //
  ///////////

  // repaint event draws the backBuffer
  public void paint(Graphics g) {
    // draw the backBuffer to the window
    g.drawImage(backBuffer, 0, 29, this);
    // start all transforms at the identity
    g2d.setTransform(identity);
    // erase the background
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, w, h);
    g2d.setColor(Color.WHITE);
    g2d.fillRect(w, 0, 0, h);

    // draw the things
    /*****************************************************************/
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
    /*****************************************************************/
  }


  ////////////////
  // MAIN LOGIC //
  ////////////////


















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

  public void mouseClicked(MouseEvent e) {

  }
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}

  /////////////////////////
  // KEYLISTENER METHODS //
  /////////////////////////

  public void keyPressed(KeyEvent e) {}
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}


}