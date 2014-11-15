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

// main game class
public class Drive extends JFrame implements Runnable, MouseListener, KeyListener {

  // main thread used for the game loop
  Thread gameLoop;
  // backbuffer
  BufferedImage backBuffer;
  // main drawing object for backbuffer
  Graphics2D g2d;

  // frame dimensions
  int w = 900;
  int h = 600;

  




}