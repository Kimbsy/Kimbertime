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
 * Wall/collision-object class.
 */

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;

public class Wall extends BasicSprite {

  ////////////////
  // DIMENSIONS //
  ////////////////
  
  private int width;
  public int getWidth() {return width;}
  public void setWidth(int width) {this.width = width;}

  private int height;
  public int getHeight() {return height;}
  public void setHeight(int height) {this.height = height;}

  ////////////////
  // VISIBILITY //
  ////////////////

  private boolean visible;
  public boolean isVisible() {return visible;}
  public void setVisible(boolean visible) {this.visible = visible;}

  public void paint(Graphics2D g2d) {
    if (isVisible()) {
      // move to position
      g2d.translate(getX(), getY());

      // draw wall
      g2d.setColor(Color.RED);
      g2d.drawRect(0, 0, getWidth(), getHeight());

      // move back
      g2d.translate(-getX(), -getY());
    }
  }

  ///////////////
  // COLISIONS //
  ///////////////

  private Shape bounds;
  public Area getArea() {
    // create a rectangle in the right place
    Shape rect = new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());

    // create an area on this shape (with which to check .intersects())
    Area area = new Area(rect);

    // return area
    return area;
  }
  public void setBounds(Shape bounds) {this.bounds = bounds;}

  /////////////////
  // ORIENTATION //
  /////////////////
  
  private boolean horizontal;
  public boolean isHorizontal() {return horizontal;}
  public void setHorizontal() {this.horizontal = getWidth() > getHeight();}

  Wall() {
    setWidth(0);
    setHeight(0);
    setVisible(false);
  }
}