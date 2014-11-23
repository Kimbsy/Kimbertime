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
import java.awt.geom.*;
import java.util.ArrayList;

public class Page extends BasicSprite {

  /////////////////////
  // PAGE PROPERTIES //
  /////////////////////

  private String name;
  private String title;
  private String content;
  private ArrayList<Button> buttons = new ArrayList<Button>();
  private boolean visible;
  private boolean hasContent;
  private int width;
  private int height;
  private Font font;
  private Font contentFont;

  //////////////////////
  // ACCESSOR METHODS //
  //////////////////////

  public String getName() {return name;}
  public String getTitle() {return title;}
  public String getContent() {return content;}
  public ArrayList<Button> getButtons() {return buttons;}
  public boolean getVisible() {return visible;}
  public boolean hasContent() {return hasContent;}
  public int getWidth() {return width;}
  public int getHeight() {return height;}
  public Font getFont() {return font;}
  public Font getContentFont() {return contentFont;}

  /////////////////////
  // MUTATOR METHODS //
  /////////////////////

  public void setName(String name) {this.name = name;}
  public void setTitle(String title) {this.title = title;}
  public void setContent(String content) {this.content = content;}
  public void addButton(Button button) {this.buttons.add(button);}
  public void setVisible(boolean visible) {this.visible = visible;}
  public void setHasContent(boolean hasContent) {this.hasContent = hasContent;}
  public void setWidth(int width) {this.width = width;}
  public void setHeight(int height) {this.height = height;}
  public void setFont(Font font) {this.font = font;}
  public void setContentFont(Font contentFont) {this.contentFont = contentFont;}

  //////////////////
  // PAGE METHODS //
  //////////////////
  
  public void paint(Graphics2D g2d, AffineTransform identity) {
    // draw the page
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, getWidth(), getHeight());

    // draw the title
    g2d.translate(40, 60);
    g2d.setColor(Color.WHITE);
    g2d.setFont(getFont());
    g2d.drawString(getTitle(), 0, 0);

    // reset the transform
    g2d.setTransform(identity);

    // write content
    if (hasContent()) {
      String[] cont = getContent().split("\n");
      g2d.setFont(getContentFont());
      g2d.translate(40, 120);
      for (int i = 0; i < cont.length; i++) {
        g2d.drawString(cont[i], 0, 0);
        g2d.translate(0, 30);
      }
      g2d.setTransform(identity);
      g2d.setFont(getFont());
    }

    // loop through and draw the buttons
    for (int i = 0; i < getButtons().size(); i++) {
      // load the button
      Button b = getButtons().get(i);

      if (!b.isPositioned()) {
        // move to a suitable position
        b.setX(getWidth() / 3);
        b.setY((getHeight() / 3) + (i * 90));
      }

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
    setContent("");
    setVisible(false);
    setHasContent(false);
    setWidth(0);
    setHeight(0);
    setFont(null);
    setContentFont(null);
  }
}
