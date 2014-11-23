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
  int w = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
  int h = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;

  // create page ArrayList
  List<Page> pages = Collections.synchronizedList(new ArrayList<Page>());

  // create level ArrayList
  List<Level> levels = Collections.synchronizedList(new ArrayList<Level>());
  int levelID = 0;

  // create sprite ArrayList
  List<PlayerSprite> sprites = Collections.synchronizedList(new ArrayList<PlayerSprite>());
  int PLAYERS = 4;

  // create identity transform
  AffineTransform identity = new AffineTransform();

  // create random number generator
  Random rand = new Random();

  // MouseListener variables
  int mouseX, mouseY;
  int mouseButton;

  // fonts for writing things
  Font font = new Font("Courier", Font.PLAIN, 50);
  Font smallFont = new Font("Courier", Font.PLAIN, 20);

  // framerate counters and other iming variables
  int frameRate = 0, frameCount = 0;
  long startTime = System.currentTimeMillis();

  // boolean toggles
  boolean started = false;

  // main
  public static void main(String[] args) {
    new Drive();
  }

  // default constructor
  public Drive() {
    super("Drive");
    setSize(w, (h)); // 32 is for JFrame top bar
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameLoop = new Thread(this);
    gameLoop.start();
    init();
  }

  // application init event
  public void init() {
    // create pages
    initPages();

    // create players
    initSprites();

    // create levels
    initLevels();

    // create the backBuffer for some smooth-ass graphics
    backBuffer = new BufferedImage((w * 2), (h * 2), BufferedImage.TYPE_INT_RGB);
    g2d = backBuffer.createGraphics();

    // listen for mice (they're around here somewhere)
    addMouseListener(this);
    // get ready for some keypresses
    addKeyListener(this);
  }

  public void initPages() {

    // create buttons
    Button play_button = new Button();
    play_button.setDescription("Play");
    play_button.setAction("play");

    Button settings_button = new Button();
    settings_button.setDescription("Settings");
    settings_button.setAction("settings");

    Button credits_button = new Button();
    credits_button.setDescription("Credits");
    credits_button.setAction("credits");

    Button legal_button = new Button();
    legal_button.setDescription("Legal");
    legal_button.setAction("legal");

    Button back_button = new Button();
    back_button.setDescription("Back");
    back_button.setAction("main");
    back_button.setX(w / 10);
    back_button.setY(h - (h / 6));
    back_button.setPositioned(true);

    // create main page
    Page main = new Page();
    main.setName("main");
    main.setTitle("Drive: a Kimbertime game.");
    main.setVisible(true);
    main.setWidth(w);
    main.setHeight(h);
    main.setFont(font);

    main.addButton(play_button);
    main.addButton(settings_button);
    main.addButton(credits_button);
    main.addButton(legal_button);

    pages.add(main);

    // create settings page
    Page settings = new Page();
    settings.setName("settings");
    settings.setTitle("Settings:");
    settings.setVisible(false);
    settings.setWidth(w);
    settings.setHeight(h);
    settings.setFont(font);

    settings.addButton(back_button);

    pages.add(settings);

    // create credits page
    Page credits = new Page();
    credits.setName("credits");
    credits.setTitle("Credits:");
    credits.setVisible(false);
    credits.setWidth(w);
    credits.setHeight(h);
    credits.setFont(font);
    credits.setContentFont(smallFont);
    credits.setContent("Lead Programmer: Dave Kimber\nConcept: Dave Kimber\nDesign: Dave Kimber\nArtwork: Dave Kimber\nSound: (@TODO put in sound)\nProducer: Dave Kimber\nArtificial Intelligence: (@TODO create AI)");
    credits.setHasContent(true);

    credits.addButton(back_button);

    pages.add(credits);

    // create legal page
    Page legal = new Page();
    legal.setName("legal");
    legal.setTitle("Legal:");
    legal.setVisible(false);
    legal.setWidth(w);
    legal.setHeight(h);
    legal.setFont(font);
    legal.setContentFont(smallFont);
    legal.setContent("/***************************************************************************\n* Copyright 2014 Dave Kimber\n* \n* Licensed under the Apache License, Version 2.0 (the \"License\");\n* you may not use this file except in compliance with the License.\n* You may obtain a copy of the License at\n* \n*     http://www.apache.org/licenses/LICENSE-2.0\n* \n* Unless required by applicable law or agreed to in writing, software\n* distributed under the License is distributed on an \"AS IS\" BASIS,\n* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n* See the License for the specific language governing permissions and\n* limitations under the License.\n***************************************************************************/");
    legal.setHasContent(true);

    legal.addButton(back_button);

    pages.add(legal);

    // create pause page
    Page pause = new Page();
    pause.setName("pause");
    pause.setTitle("Game Paused");
    pause.setVisible(false);
    pause.setWidth(w);
    pause.setHeight(h);
    pause.setFont(font);

    pause.addButton(play_button);

    pages.add(pause);
  }

  public void initSprites() {
    for (int i = 0; i < PLAYERS; i++) {
      PlayerSprite s = new PlayerSprite();
      s.setX((w / 2) + (100 * i));
      s.setY((h / 2) + (100));

      // set up controls
      if (i < 4) {
        setStandardControls(s, i);
      }

      sprites.add(s);
    }
  }

  public void setStandardControls(PlayerSprite s, int i) {
    switch (i) {
      case 0:
        s.setColor(Color.RED);
        s.setUpKey(KeyEvent.VK_UP);
        s.setDownKey(KeyEvent.VK_DOWN);
        s.setLeftKey(KeyEvent.VK_LEFT);
        s.setRightKey(KeyEvent.VK_RIGHT);
        s.setLightsKey(KeyEvent.VK_CONTROL);
        break;
      case 1:
        s.setColor(Color.GREEN);
        s.setUpKey(KeyEvent.VK_W);
        s.setDownKey(KeyEvent.VK_S);
        s.setLeftKey(KeyEvent.VK_A);
        s.setRightKey(KeyEvent.VK_D);
        s.setLightsKey(KeyEvent.VK_SHIFT);
        break;
      case 2:
        s.setColor(Color.WHITE);
        s.setUpKey(KeyEvent.VK_U);
        s.setDownKey(KeyEvent.VK_J);
        s.setLeftKey(KeyEvent.VK_H);
        s.setRightKey(KeyEvent.VK_K);
        s.setLightsKey(KeyEvent.VK_L);
        break;
      case 3:
        s.setColor(Color.BLUE);
        s.setUpKey(KeyEvent.VK_NUMPAD8);
        s.setDownKey(KeyEvent.VK_NUMPAD5);
        s.setLeftKey(KeyEvent.VK_NUMPAD4);
        s.setRightKey(KeyEvent.VK_NUMPAD6);
        s.setLightsKey(KeyEvent.VK_NUMPAD0);
        break;
    }
  }

  public void initLevels() {
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

    // check if started
    checkStarted();

    if (started) {
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
    else {
      // draw the backBuffer to the window (no offsets for pages)
      g.drawImage(backBuffer, 0, 0, this);

      drawPages();
    }
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

  public void drawPages() {

    // loop through pages
    for (int i = 0; i < pages.size(); i++) {
      // load the page
      Page p = pages.get(i);

      g2d.setTransform(identity);
      // check if page is being shown
      if (p.getVisible()) {
        p.paint(g2d, identity);
      }
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
    // see if the game is running
    checkStarted();

    // if it is, do all the things
    if (started) {
      updateSprites();
      updateLevel();
      checkCollisions();
      checkVictoryConditions();
    }
  }

  public void checkStarted() {
    boolean anyVisible = false;

    for (int i = 0; i < pages.size(); i++) {
      Page p = pages.get(i);
      if (p.getVisible()) {
        anyVisible = true;
      }
    }

    if (anyVisible) {
      started = false;
    }
    else {
      started = true;
    }
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

    for (int i = 0; i < sprites.size(); i++) {
      PlayerSprite s1 = sprites.get(i);
      for (int j = 0; j < sprites.size(); j++) {
        PlayerSprite s2 = sprites.get(j);

        if (i != j && (s1.getColTime() < 0) && (s2.getColTime() < 0)) {
          if (s1.getArea().intersects(s2.getArea().getBounds())) {

            // @TODO sprites can sit on top of each other and then can't be hit by anything :/

            // // move them apart so they dont clip
            // double dx = Math.abs(s1.getX() - s2.getX());
            // double dy = Math.abs(s1.getY() - s2.getY());

            // if (s1.getX() < s2.getX()) {
            //   s1.incX(-((17 - dx) / 2));
            //   s2.incX((17 - dx) / 2);
            // }
            // else if (s1.getX() > s2.getX()) {
            //   s1.incX((17 - dx) / 2);
            //   s2.incX(-((17 - dx) / 2));
            // }
            // if (s1.getY() < s2.getY()) {
            //   s1.incY(-((17 - dy) / 2));
            //   s2.incY((17 - dy) / 2);
            // }
            // else if (s1.getY() > s2.getY()) {
            //   s1.incY(-((17 - dy) / 2));
            //   s2.incY((17 - dy) / 2);
            // }

            // temporary values so they can switch velocitities and directions
            double tempVel = s1.getVel();
            double tempX = s1.getVelX();
            double tempY = s1.getVelY();
            double tempFace = s1.getFaceAngle();

            // set s1 to have s2's values
            s1.setVel(s2.getVel());
            s1.setVelX(s2.getVelX());
            s1.setVelY(s2.getVelY());
            s1.setFaceAngle(s2.getFaceAngle());

            // set s2 to have s1's values
            s2.setVel(tempVel);
            s2.setVelX(tempX);
            s2.setVelY(tempY);
            s2.setFaceAngle(tempFace);

            // update so positions change to avoid clipping
            updateSprites();

            // collision delay
            s1.setColTime(30);
            s2.setColTime(30);
          }
        }
      }
    }
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

  public void mouseClicked(MouseEvent e) {

    // set mouseButton
    checkButton(e);

    String goTo = "";

    // decide what to do
    switch (mouseButton) {
      case 1:
        // loop through the pages
        for (int i = 0; i < pages.size(); i++) {
          // load the page
          Page p = pages.get(i);

          // if visible, get the buttons
          if (p.getVisible()) {
            ArrayList<Button> buttons = p.getButtons();

            for (int j = 0; j < buttons.size(); j++) {
              Button b = buttons.get(j);

              // if button was clicked (i.e. didn't return "NO")
              if (b.checkClick(e.getX(), e.getY()) != "NO") {
                // perform corresponding action
                goTo = b.checkClick(e.getX(), e.getY());
              }
            }
          }
        }
        break;
      default:
        break;
    }

    switch (goTo) {
      case "":
        // do nothing
        break;
      case "NO":
        // do nothing
        break;
      case "Play":
        setAllNotVisible();
        break;
      default:
        openPage(goTo);
        break;

    }
  }
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}

  public void setAllNotVisible() {
    // loop through pages
    for (int i = 0; i < pages.size(); i++) {
      // load page
      Page p = pages.get(i);
      // hide it
      p.setVisible(false);
    }
  }

  public void openPage(String name) {
    // loop through pages
    for (int i = 0; i < pages.size(); i++) {
      // load page
      Page p = pages.get(i);
      // if the one (strings should use .equals() instead of ==)
      if (p.getName().equals(name)) {
        // show it
        p.setVisible(true);
      }
      else {
        // hide it
        p.setVisible(false);
      }
    }
  }

  /////////////////////////
  // KEYLISTENER METHODS //
  /////////////////////////

  public void keyPressed(KeyEvent e) {
    // controls only work when game is started
    int key = e.getKeyCode();
    if (started) {

      // pause on esc
      if (key == KeyEvent.VK_ESCAPE) {
        // loop through pages
        for (int i = 0; i < pages.size(); i++) {
          // load page
          Page p = pages.get(i);

          // if pause (such a bad way of doing it, must learn hash maps)
          if (p.getName() == "pause") {
            p.setVisible(true);
          }
        }
      }
      else {
        // check sprites for controls
        for (int i = 0; i < sprites.size(); i++) {

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
    }
  }
  public void keyReleased(KeyEvent e) {
    // controls only work when game is started
    if (started) {
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
  }
  public void keyTyped(KeyEvent e) {}
}
