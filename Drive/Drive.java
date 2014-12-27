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
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.*;
import sun.audio.*;

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
  // int w = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
  // int h = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;

  int w = 1500;
  int h = 1000;

  // create page ArrayList
  List<Page> pages = Collections.synchronizedList(new ArrayList<Page>());

  // create level ArrayList
  List<Level> levels = Collections.synchronizedList(new ArrayList<Level>());
  int levelID = 0;

  // create sprite ArrayList
  List<PlayerSprite> sprites = Collections.synchronizedList(new ArrayList<PlayerSprite>());
  int PLAYERS = 2;

  // create walls/collision-objects ArrayList
  List<Wall> lapWalls = Collections.synchronizedList(new ArrayList<Wall>());
  List<Wall> demoWalls = Collections.synchronizedList(new ArrayList<Wall>());

  // create checkpoints ArrayList
  List<Checkpoint> checkpoints = Collections.synchronizedList(new ArrayList<Checkpoint>());

  // create identity transform
  public AffineTransform identity = new AffineTransform();

  // create random number generator
  Random rand = new Random();

  // MouseListener variables
  int mouseX, mouseY;
  int mouseButton;

  // fonts for writing things
  Font font = new Font("Courier", Font.PLAIN, 50);
  Font smallFont = new Font("Courier", Font.PLAIN, 20);
  Font bigFont = new Font("Courier", Font.PLAIN, 80);

  // framerate counters and other iming variables
  int frameRate = 0, frameCount = 0;
  long startTime = System.currentTimeMillis();

  // boolean toggles
  boolean started = false;

  boolean victory = false;

  // winner id
  int winId = -1;

  // sounds
  Sound menuMusic;
  Sound gameMusic;

  Sound collision1;
  Sound collision2;
  Sound collision3;
  
  Sound tyres1;
  Sound tyres2;
  Sound tyres3;
  
  Sound engine1;
  Sound engine2;
  Sound engine3;

  Sound horn1;

  // main
  public static void main(String[] args) {
    new Drive();
  }

  // default constructor
  public Drive() {
    super("Drive");
    setExtendedState(JFrame.MAXIMIZED_BOTH);
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

    // create checkpoints
    initCheckpoints();

    // start sounds
    try {
      initSounds();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

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
    settings_button.setWidth(275);
    settings_button.setDescription("Settings");
    settings_button.setAction("settings");

    // game mode buttons on settings page
    Button derby_button = new Button();
    derby_button.setWidth(350);
    derby_button.setDescription("Demo Derby");
    derby_button.setAction("makedemoderby");

    Button lapRace_button = new Button();
    lapRace_button.setWidth(275);
    lapRace_button.setDescription("Lap Race");
    lapRace_button.setAction("makelaprace");

    Button credits_button = new Button();
    credits_button.setWidth(250);
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

    settings.addButton(derby_button);
    settings.addButton(lapRace_button);
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
    credits.setContent("Lead Programmer: Dave Kimber\nConcept: Dave Kimber\nDesign: Dave Kimber\nArtwork: Dave Kimber\nSound: Dave Kimber\nProducer: Dave Kimber\nArtificial Intelligence: (@TODO create AI)");
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

      // set sprite position
      s.setX((490));
      s.setY((475 + (i * 50)));
      
      // set up controls
      if (i < 4) {
        setStandardControls(s, i);
      }

      // set car number
      s.setNumber(i + 1);

      sprites.add(s);
    }
  }

  public void respawn(PlayerSprite s) {
    // find lastest checkpoint spawn ani increment
    Checkpoint c = checkpoints.get(s.getLatestCheck());
    int spawn = c.getMostRecentSpawn();
    c.incMostRecentSpawn(1);

    // zero all velocity
    s.setVel(0);
    s.setVelX(0);
    s.setVelY(0);

    // reset faceAngle based on checkpoint orientation
    switch (c.getDirection()) {
      case "UP":
        s.setFaceAngle(270);
        break;
      case "DOWN":
        s.setFaceAngle(90);
        break;
      case "LEFT":
        s.setFaceAngle(180);
        break;
      case "RIGHT":
        s.setFaceAngle(0);
        break;
    }

    // reposition
    s.setX(c.getSpawnXs()[spawn]);
    s.setY(c.getSpawnYs()[spawn]);
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

    Level l = new Level();
    l.setWidth(w * 2);
    l.setHeight(h * 2);

    // background image
    try {
      l.setImage(ImageIO.read(new File("img/presentTrack.jpg")));
    }
    catch (IOException e) {
      System.out.println("shit image");
      e.printStackTrace();
    }

    // add level to ArrayList
    levels.add(l);

    ////////////////////////////////////
    // CREATE LAP RACE OUTSIDE BORDER //
    ////////////////////////////////////

    Wall w1 = new Wall();
    w1.setX(345);
    w1.setY(375);
    w1.setWidth(1100);
    w1.setHeight(5);
    w1.setHorizontal();
    // w1.setVisible(true);
    lapWalls.add(w1);

    Wall w2 = new Wall();
    w2.setX(1440);
    w2.setY(285);
    w2.setWidth(5);
    w2.setHeight(95);
    w2.setHorizontal();
    // w2.setVisible(true);
    lapWalls.add(w2);

    Wall w3 = new Wall();
    w3.setX(1440);
    w3.setY(285);
    w3.setWidth(260);
    w3.setHeight(5);
    w3.setHorizontal();
    // w3.setVisible(true);
    lapWalls.add(w3);

    Wall w4 = new Wall();
    w4.setX(1695);
    w4.setY(285);
    w4.setWidth(5);
    w4.setHeight(80);
    w4.setHorizontal();
    // w4.setVisible(true);
    lapWalls.add(w4);

    Wall w5 = new Wall();
    w5.setX(1695);
    w5.setY(360);
    w5.setWidth(605);
    w5.setHeight(5);
    w5.setHorizontal();
    // w5.setVisible(true);
    lapWalls.add(w5);

    Wall w6 = new Wall();
    w6.setX(2295);
    w6.setY(360);
    w6.setWidth(5);
    w6.setHeight(320);
    w6.setHorizontal();
    // w6.setVisible(true);
    lapWalls.add(w6);

    Wall w7 = new Wall();
    w7.setX(2295);
    w7.setY(675);
    w7.setWidth(150);
    w7.setHeight(5);
    w7.setHorizontal();
    // w7.setVisible(true);
    lapWalls.add(w7);

    Wall w8 = new Wall();
    w8.setX(2440);
    w8.setY(675);
    w8.setWidth(5);
    w8.setHeight(710);
    w8.setHorizontal();
    // w8.setVisible(true);
    lapWalls.add(w8);

    
    Wall w9 = new Wall();
    w9.setX(2345);
    w9.setY(1380);
    w9.setWidth(100);
    w9.setHeight(5);
    w9.setHorizontal();
    // w9.setVisible(true);
    lapWalls.add(w9);

    Wall w10 = new Wall();
    w10.setX(2345);
    w10.setY(1380);
    w10.setWidth(5);
    w10.setHeight(270);
    w10.setHorizontal();
    // w10.setVisible(true);
    lapWalls.add(w10);

    Wall w11 = new Wall();
    w11.setX(440);
    w11.setY(1645);
    w11.setWidth(1905);
    w11.setHeight(5);
    w11.setHorizontal();
    // w11.setVisible(true);
    lapWalls.add(w11);

    Wall w12 = new Wall();
    w12.setX(440);
    w12.setY(1530);
    w12.setWidth(5);
    w12.setHeight(120);
    w12.setHorizontal();
    // w12.setVisible(true);
    lapWalls.add(w12);

    Wall w13 = new Wall();
    w13.setX(235);
    w13.setY(1530);
    w13.setWidth(210);
    w13.setHeight(5);
    w13.setHorizontal();
    // w13.setVisible(true);
    lapWalls.add(w13);

    Wall w14 = new Wall();
    w14.setX(235);
    w14.setY(1245);
    w14.setWidth(5);
    w14.setHeight(290);
    w14.setHorizontal();
    // w14.setVisible(true);
    lapWalls.add(w14);

    Wall w15 = new Wall();
    w15.setX(235);
    w15.setY(1245);
    w15.setWidth(70);
    w15.setHeight(5);
    w15.setHorizontal();
    // w15.setVisible(true);
    lapWalls.add(w15);

    Wall w16 = new Wall();
    w16.setX(300);
    w16.setY(700);
    w16.setWidth(5);
    w16.setHeight(545);
    w16.setHorizontal();
    // w16.setVisible(true);
    lapWalls.add(w16);

    Wall w17 = new Wall();
    w17.setX(300);
    w17.setY(700);
    w17.setWidth(50);
    w17.setHeight(5);
    w17.setHorizontal();
    // w17.setVisible(true);
    lapWalls.add(w17);

    Wall w18 = new Wall();
    w18.setX(345);
    w18.setY(375);
    w18.setWidth(5);
    w18.setHeight(330);
    w18.setHorizontal();
    // w18.setVisible(true);
    lapWalls.add(w18);

    ///////////////////////////////////////
    // CREATE LAP RACE INSIDE BORDER     //
    // WILL USE FOR DERBY OUTSIDE BORDER //
    ///////////////////////////////////////

    Wall w19 = new Wall();
    w19.setX(680);
    w19.setY(730);
    w19.setWidth(765);
    w19.setHeight(5);
    w19.setHorizontal();
    // w19.setVisible(true);
    lapWalls.add(w19);
    demoWalls.add(w19);

    Wall w20 = new Wall();
    w20.setX(1440);
    w20.setY(730);
    w20.setWidth(5);
    w20.setHeight(85);
    w20.setHorizontal();
    // w20.setVisible(true);
    lapWalls.add(w20);
    demoWalls.add(w20);

    Wall w21 = new Wall();
    w21.setX(1440);
    w21.setY(810);
    w21.setWidth(395);
    w21.setHeight(5);
    w21.setHorizontal();
    // w21.setVisible(true);
    lapWalls.add(w21);
    demoWalls.add(w21);

    Wall w22 = new Wall();
    w22.setX(1830);
    w22.setY(810);
    w22.setWidth(5);
    w22.setHeight(525);
    w22.setHorizontal();
    // w22.setVisible(true);
    lapWalls.add(w22);
    demoWalls.add(w22);

    Wall w23 = new Wall();
    w23.setX(1745);
    w23.setY(1330);
    w23.setWidth(90);
    w23.setHeight(5);
    w23.setHorizontal();
    // w23.setVisible(true);
    lapWalls.add(w23);
    demoWalls.add(w23);

    Wall w24 = new Wall();
    w24.setX(1745);
    w24.setY(1330);
    w24.setWidth(5);
    w24.setHeight(30);
    w24.setHorizontal();
    // w24.setVisible(true);
    lapWalls.add(w24);
    demoWalls.add(w24);

    Wall w25 = new Wall();
    w25.setX(700);
    w25.setY(1355);
    w25.setWidth(1050);
    w25.setHeight(5);
    w25.setHorizontal();
    // w25.setVisible(true);
    lapWalls.add(w25);
    demoWalls.add(w25);

    Wall w26 = new Wall();
    w26.setX(700);
    w26.setY(1265);
    w26.setWidth(5);
    w26.setHeight(95);
    w26.setHorizontal();
    // w26.setVisible(true);
    lapWalls.add(w26);
    demoWalls.add(w26);
    
    Wall w27 = new Wall();
    w27.setX(680);
    w27.setY(1265);
    w27.setWidth(25);
    w27.setHeight(5);
    w27.setHorizontal();
    // w27.setVisible(true);
    lapWalls.add(w27);
    demoWalls.add(w27);
    
    Wall w28 = new Wall();
    w28.setX(680);
    w28.setY(730);
    w28.setWidth(5);
    w28.setHeight(540);
    w28.setHorizontal();
    // w28.setVisible(true);
    lapWalls.add(w28);
    demoWalls.add(w28);
  }

  public void initCheckpoints() {
    Checkpoint c = new Checkpoint();
    c.setDelta(0);
    c.setX(750);
    c.setY(375);
    c.setWidth(5);
    c.setHeight(400);
    // c.setVisible(true);
    c.setDirection("RIGHT");
    c.setSpawns();
    checkpoints.add(c);

    c = new Checkpoint();
    c.setDelta(1);
    c.setX(1750);
    c.setY(300);
    c.setWidth(5);
    c.setHeight(530);
    // c.setVisible(true);
    c.setDirection("RIGHT");
    c.setSpawns();
    checkpoints.add(c);

    c = new Checkpoint();
    c.setDelta(2);
    c.setX(1800);
    c.setY(1120);
    c.setWidth(700);
    c.setHeight(5);
    // c.setVisible(true);
    c.setDirection("DOWN");
    c.setSpawns();
    checkpoints.add(c);

    c = new Checkpoint();
    c.setDelta(3);
    c.setX(1610);
    c.setY(1290);
    c.setWidth(5);
    c.setHeight(530);
    // c.setVisible(true);
    c.setDirection("LEFT");
    c.setSpawns();
    checkpoints.add(c);

    c = new Checkpoint();
    c.setDelta(4);
    c.setX(845);
    c.setY(1290);
    c.setWidth(5);
    c.setHeight(530);
    // c.setVisible(true);
    c.setDirection("LEFT");
    c.setSpawns();
    checkpoints.add(c);

    c = new Checkpoint();
    c.setDelta(5);
    c.setX(300);
    c.setY(975);
    c.setWidth(530);
    c.setHeight(5);
    // c.setVisible(true);
    c.setDirection("UP");
    c.setSpawns();
    checkpoints.add(c);
  }

  public void initSounds() throws Exception {

    menuMusic = new Sound("sound/music/elevatorMusic.wav");
    gameMusic = new Sound("sound/music/theMetal.wav");

    collision1 = new Sound("sound/collision/collision1.wav");
    collision2 = new Sound("sound/collision/collision2.wav");
    collision3 = new Sound("sound/collision/collision3.wav");

    tyres1 = new Sound("sound/tyres/tyres1.wav");
    tyres2 = new Sound("sound/tyres/tyres2.wav");
    tyres3 = new Sound("sound/tyres/tyres3.wav");

    engine1 = new Sound("sound/engine/engine1.wav");
    engine2 = new Sound("sound/engine/engine2.wav");
    engine3 = new Sound("sound/engine/engine3.wav");

    horn1 = new Sound("sound/horn/horn1.wav");

    menuMusic.play();
  }

  ///////////
  // LOOPS //
  ///////////

  // repaint event draws the backBuffer
  public void paint(Graphics g) {

    // check if started
    checkStarted();

    if (started) {
      // only ever one level
      Level l = levels.get(0);

      // draw the backBuffer to the window
      if (l.cameraShouldMove()) {
        g.drawImage(backBuffer, (w / 2) - (int)getAverageX(), (h / 2) - (int)getAverageY(), this);
      }
      else {
        g.drawImage(backBuffer, 0, 0, this);
      }

      // start all transforms at the identity
      try {
        g2d.setTransform(identity);
      }
      catch (NullPointerException e) { }

      // erase the background
      g2d.setColor(Color.GRAY);
      g2d.fillRect(-w, -h, (w * 4), (h * 4));

      // draw the level
      drawLevel();

      // draw the sprites
      drawSprites();

      // draw walls
      drawWalls();

      // draw checkpoints
      drawCheckpoints();

      // draw the scores
      drawScores();

      // draw winner
      if (victory) {
        drawWinner();
      }
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
    try {
      g2d.setTransform(identity);
    }
    catch (NullPointerException e) { }

    // draw the level
    l.paint(g2d);
  }

  public void drawSprites() {
    // loop through the sprites to draw the lights (want the under all sprites)
    for (int i = 0; i < sprites.size(); i++) {
      // load the sprite
      PlayerSprite s = sprites.get(i);

      // always draw from identity transform
      try {
        g2d.setTransform(identity);
      }
      catch (NullPointerException e) { }
      // draw lights
      s.drawLights(g2d);
    }
    // loop through again for rest of sprite
    for (int i = 0; i < sprites.size(); i++) {
      // load the sprite
      PlayerSprite s = sprites.get(i);

      // always draw from identity transform
      try {
        g2d.setTransform(identity);
      }
      catch (NullPointerException e) { }

      // draw the sprite
      s.paint(g2d);
    }
  }

  public void drawWalls() {
    Level l = levels.get(0);

    if (l.isLapRace()) {
      // draw the walls for the lap race
      for (int i = 0; i < lapWalls.size(); i++) {
        Wall w1 = lapWalls.get(i);

        // always set to the identity
        try {
          g2d.setTransform(identity);
        }
        catch (NullPointerException e) { }

        // draw the walls
        w1.paint(g2d);
      }
    }
    else {
      // draw the walls for the demo derby race
      for (int i = 0; i < demoWalls.size(); i++) {
        Wall w1 = demoWalls.get(i);

        // always set to the identity
        try {
          g2d.setTransform(identity);
        }
        catch (NullPointerException e) { }

        w1.paint(g2d);
      }
    }
  }

  public void drawCheckpoints() {
    for (int i = 0; i < checkpoints.size(); i++) {
      Checkpoint c = checkpoints.get(i);

      // always set to the identity
      try {
        g2d.setTransform(identity);
      }
      catch (NullPointerException e) { }

      // draw the checkpoint
      c.paint(g2d);
    }
  }

  public void drawScores() {
    // loop through the sprites
    for (int i = 0; i < sprites.size(); i++) {
      // load them
      PlayerSprite s = sprites.get(i);

      // always the identity
      try {
        g2d.setTransform(identity);
      }
      catch (NullPointerException e) { }

      // only one level
      Level l = levels.get(0);

      // want to stay with camera
      if (l.cameraShouldMove()) {
        g2d.translate((int)getAverageX() - (w / 2), (int)getAverageY() - (h / 2));
      }

      // set corresponing color
      g2d.setColor(s.getColor());
      // draw the scores
      g2d.setFont(font);
      g2d.drawString(Integer.toString(s.getScore()), (i * 100) + 20, 50);
    } 
  }

  public void drawWinner() {
    // always set to identity
    try {
      g2d.setTransform(identity);
    }
    catch (NullPointerException e) { }

    // get winner's color
    g2d.setColor(sprites.get(winId).getColor());

    g2d.setFont(bigFont);

    Level l = levels.get(0);
    
    if (l.isLapRace()) {
      g2d.drawString("Player " + (winId + 1) + " Wins!!!", 840, 480);
    }
    else {
      g2d.drawString("Player " + (winId + 1) + " Wins!!!", 820, 890);
    }
  }

  public void drawPages() {

    // loop through pages
    for (int i = 0; i < pages.size(); i++) {
      // load the page
      Page p = pages.get(i);

      try {
        g2d.setTransform(identity);
      }
      catch (NullPointerException e) { }
      // check if page is being shown
      if (p.getVisible()) {
        try {
          p.paint(g2d);
        }
        catch (NullPointerException e) { }
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

      Level l = levels.get(0);

      if (!l.isLapRace()) {
        s.setScore(s.getHealth());
      }
      else {
        getRacePositions();
      }

      // destroy if health is too low
      if (s.getHealth() < 1) {
        s.setWreck(true);
        s.setAcc(0);
        s.setTurn(0);
        s.setLights(false);
        s.setColor(Color.DARK_GRAY);
      }

      // update faceAngle
      s.incFaceAngle((s.getTurn() * 5));

      // update velocity
      s.updateVel(tyres1, tyres2, tyres3);

      // update position
      s.updatePos();

      // decay timers
      if (s.getColTime() >= 0) {      
        s.incColTime(-1);
      }
      if (s.getSlide() >= 0) {        
        s.incSlide(-1);
      }
      if (s.getScreechTime() >= 0) {        
        s.incScreechTime(-1);
      }

      // print out position
      // System.out.println((int)s.getX() + ", " + (int)(s.getY() - 32));
    }
  }

  public void getRacePositions() {
    // @TODO figure out who is winning
  }

  public void updateLevel() {
    // @TODO this (ever gonna need this?)
  }

  public void checkCollisions() {

    for (int i = 0; i < sprites.size(); i++) {
      PlayerSprite s1 = sprites.get(i);

      // only one level
      Level l = levels.get(0);

      // collisions with other sprites
      for (int j = 0; j < sprites.size(); j++) {
        PlayerSprite s2 = sprites.get(j);
        if (i != j && (s1.getColTime() < 0) && (s2.getColTime() < 0)) {
          if (s1.getArea().intersects(s2.getArea().getBounds())) {

            // play sound
            switch (rand.nextInt(3)) {
              case 0:              
                collision1.play();
                break;
              case 1:                
                collision2.play();
                break;
              case 2:                
                collision3.play();
                break;
            }

            // move to stop clipping
            while (s1.getArea().intersects(s2.getArea().getBounds())) {
              if (s1.getX() > s2.getX()) {
                s1.incX(1);
                s2.incX(-1);
              }
              else if (s1.getX() < s2.getX()) {
                s1.incX(-1);
                s2.incX(1);
              }
              if (s1.getY() > s2.getY()) {
                s1.incY(-1);
                s2.incY(1);
              }
              else if (s1.getY() < s2.getY()) {
                s1.incY(1);
                s2.incY(-1);
              }
            }

            if (l.damageIsOn()) {
              // reduce health
              s1.incHealth(-(int)Math.abs((s2.getVel() / 2)));
              s2.incHealth(-(int)Math.abs((s1.getVel() / 2)));
            }

            // temporary values so they can switch velocitities and directions
            double tempVel = (s1.getVel() * 0.9);
            double tempX = (s1.getVelX() * 0.9);
            double tempY = (s1.getVelY() * 0.9);

            // set s1 to have s2's values
            s1.setVel(Math.abs((s2.getVel() * 0.9)));
            s1.setVelX((s2.getVelX() * 0.9));
            s1.setVelY((s2.getVelY() * 0.9));

            // set s2 to have s1's values
            s2.setVel(Math.abs(tempVel));
            s2.setVelX(tempX);
            s2.setVelY(tempY);

            // collision delay
            s1.setColTime(5);
            s2.setColTime(5);

            // cars will slide
            s1.setSlide(4 * Math.abs(s1.getVel()));
            s2.setSlide(4 * Math.abs(s2.getVel()));
            s1.setSliding(true);
            s2.setSliding(true);

            // update so positions change to avoid clipping
            updateSprites();
          }
        }
      }

      // collisions with walls
      if (l.isLapRace()) {
        for (int j = 0; j < lapWalls.size(); j++) {
          Wall w1 = lapWalls.get(j);

          if (s1.getArea().intersects(w1.getArea().getBounds())) {

            if (w1.isLethal()) {
              // respawn at last checkpoint
              respawn(s1);
            }
            else {
              // play sound
              switch (rand.nextInt(3)) {
                case 0:              
                  collision1.play();
                  break;
                case 1:                
                  collision2.play();
                  break;
                case 2:                
                  collision3.play();
                  break;
              }

              // reduce health
              if (l.damageIsOn()) {
                s1.incHealth(-(int)(s1.getVel() / 2));
              }

              if (w1.isHorizontal()) {
                // flip velY (reduce for friction)
                s1.setVelY(-(s1.getVelY() * 0.9));
              }
              else {
                // flip velX (reduce for friction)
                s1.setVelX(-(s1.getVelX() * 0.9));
              }

              // angle of reflection from angle of incidence
              s1.setFaceAngle(calcReflection(s1.getFaceAngle(), w1.isHorizontal()));

              // move to stop clipping
              s1.updatePos();
              s1.updatePos();
              s1.updatePos();
            }
          }
        }

        // collisions with checkpoints
        for (int j = 0; j < checkpoints.size(); j++) {
          // increment player's lastestCheck if appropriate (passed in order)
          Checkpoint c = checkpoints.get(j);

          // if they collide
          if (s1.getArea().intersects(c.getArea().getBounds())) {

            // if it is the next one
            if ((s1.getLatestCheck() + 1 == (c.getDelta())) || (c.getDelta() == 0 && s1.getLatestCheck() == (checkpoints.size() - 1))) {
              s1.setLatestCheck(c.getDelta());

              // update player's lapcount
              if (l.isLapRace() && c.getDelta() == 0) {
                s1.incCompletedLaps(1);
                System.out.println("Player " + (i + 1) + " Completed lap: " + s1.getCompletedLaps());
              }
            }
          }
        }
      }
      else {
        for (int j = 0; j < demoWalls.size(); j++) {
          Wall w1 = demoWalls.get(j);

          if (s1.getArea().intersects(w1.getArea().getBounds())) {

            if (w1.isLethal()) {
              // respawn at last checkpoint
              respawn(s1);
            }
            else {
              // play sound
              switch (rand.nextInt(3)) {
                case 0:              
                  collision1.play();
                  break;
                case 1:                
                  collision2.play();
                  break;
                case 2:                
                  collision3.play();
                  break;
              }

              // reduce health
              if (l.damageIsOn()) {
                s1.incHealth(-(int)Math.abs(s1.getVel() / 2));
              }

              if (w1.isHorizontal()) {
                // flip velY (reduce for friction)
                s1.setVelY(-(s1.getVelY() * 0.9));
              }
              else {
                // flip velX (reduce for friction)
                s1.setVelX(-(s1.getVelX() * 0.9));
              }

              // angle of reflection from angle of incidence
              s1.setFaceAngle(calcReflection(s1.getFaceAngle(), w1.isHorizontal()));

              // move to stop clipping
              s1.updatePos();
              s1.updatePos();
              s1.updatePos();
            }
          }
        }
      }
    }
  }

  public double calcReflection(double incident, boolean isHorizontal) {
    // plane of reflection
    double plane = 180;

    // angle of incidence
    incident = incident % 360;

    if (!isHorizontal) {
      plane = 90;
    }

    double diff = plane - incident;

    // return angle of refraction
    return plane + diff;
  }

  public void checkVictoryConditions() {
    // get info on level
    Level l = levels.get(0);

    // if lap race
    if (l.isLapRace()) {
      for (int i = 0; i < sprites.size(); i++) {
        PlayerSprite s = sprites.get(i);

        if (s.getCompletedLaps() >= l.getNumLaps()) {

          /////////////////
          // PLAYER WINS //
          /////////////////

          // System.out.println("Winner: " + (i + 1));

          victory = true;

          winId = i;
        }
      }
    }
    else {
      if (l.damageIsOn()) {

        int survivors = 0;

        PlayerSprite winner;

        for (int i = 0; i < sprites.size(); i++) {
          PlayerSprite s = sprites.get(i);

          if (!s.isWreck()) {
            winner = s;
            winId = i;
            survivors++;
          }
        }

        if (survivors == 1) {

          /////////////////
          // PLAYER WINS //
          /////////////////

          // System.out.println("Winner: " + (winId + 1));

          victory = true;

        }
      }
    }
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
      case "makedemoderby":
        makeDemoDerby();
        openPage("main");
        break;
      case "makelaprace":
        makeLapRace();
        openPage("main");
        break;
      case "play":
        // get rid of menu
        setAllNotVisible();

        // switch tracks
        menuMusic.stop();
        gameMusic.play();

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

  public void makeDemoDerby() {
    Level l = levels.get(0);

    l.setDamage(true);
    l.setLapRace(false);
    l.setCameraMove(true);

    // put players inside the arena
    for (int i = 0; i < sprites.size(); i++) {
      PlayerSprite s = sprites.get(i);

      // random coords within range
      // X => 820 - 1740
      // Y => 890 - 1265
      s.setX(rand.nextInt(920) + 820);
      s.setY(rand.nextInt(375) + 890);

      // randomise direction
      s.setFaceAngle(90 * rand.nextInt(4));

      // turn on lights to show direction
      s.setLights(true);
    }
  }

  public void makeLapRace() {
    Level l = levels.get(0);

    l.setDamage(false);
    l.setLapRace(true);
    l.setCameraMove(true);

    // set sprite positions to lap race
    for (int i = 0; i < sprites.size(); i++) {
      PlayerSprite s = sprites.get(i);

      s.setX((490));
      s.setY((475 + (i * 50)));

      // make them face right
      s.setFaceAngle(0);

      // turn off the lights
      s.setLights(false);
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

            // switch tracks
            gameMusic.stop();
            menuMusic.play();
          }
        }
      }
      else {
        // check sprites for controls
        for (int i = 0; i < sprites.size(); i++) {

          PlayerSprite s = sprites.get(i);

          if (!s.isWreck() && !s.isSliding()) {
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
  }
  public void keyReleased(KeyEvent e) {
    // controls only work when game is started
    if (started) {
      // check sprites for controls
      for (int i = 0; i < sprites.size(); i++) {
        int key = e.getKeyCode();

        PlayerSprite s = sprites.get(i);

        if (!s.isWreck()) {
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
  }
  public void keyTyped(KeyEvent e) {}
}
