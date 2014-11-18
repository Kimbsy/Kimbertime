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
 * Page class.
 */

import java.awt.*;
import java.util.ArrayList;

public class Page extends BasicSprite {

  /////////////////////
  // PAGE PROPERTIES //
  /////////////////////

  private String name;
  private String title;
  private ArrayList<Button> buttons = new ArrayList<Button>();
  private boolean visible;
  private int width;
  private int height;
  private Font font;

  //////////////////////
  // ACCESSOR METHODS //
  //////////////////////

  public String getName() {return name;}
  public String getTitle() {return title;}
  public ArrayList<Button> getButtons() {return buttons;}
  public boolean getVisible() {return visible;}
  public int getWidth() {return width;}
  public int getHeight() {return height;}
  public Font getFont() {return font;}

  /////////////////////
  // MUTATOR METHODS //
  /////////////////////

  public void setName(String name) {this.name = name;}
  public void setTitle(String title) {this.title = title;}
  public void addButton(Button button) {this.buttons.add(button);}
  public void setVisible(boolean visible) {this.visible = visible;}
  public void setWidth(int width) {this.width = width;}
  public void setHeight(int height) {this.height = height;}
  public void setFont(Font font) {this.font = font;}

  //////////////////
  // PAGE METHODS //
  //////////////////
  
  public void paint(Graphics2D g2d) {
    // draw the page
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, getWidth(), getHeight());

    // draw the title
    g2d.translate(40, 60);
    g2d.setColor(Color.WHITE);
    g2d.setFont(getFont());
    g2d.drawString(getTitle(), 0, 0);

    // reset the transform
    g2d.translate(-40, -60);

    // loop through and draw the buttons
    for (int i = 0; i < getButtons().size(); i++) {
      // load the button
      Button b = getButtons().get(i);

      // move to a suitable position
      b.setX(getWidth() / 3);
      b.setY(getHeight() / 3);
      // g2d.translate((getWidth() / 3), (getHeight() / 3));

      // draw it
      b.paint(g2d);
    }
  }

  /////////////////////////
  // DEFAULT CONSTRUCTOR //
  /////////////////////////

  Page() {
    setName("");
    setTitle("");
    setVisible(false);
    setWidth(0);
    setHeight(0);
    setFont(null);
  }
}