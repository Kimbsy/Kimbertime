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

import java.util.List;

public class Page extends BasicSprite {

  /////////////////////
  // PAGE PROPERTIES //
  /////////////////////

  private String title;
  private List<Button> buttons;

  //////////////////////
  // ACCESSOR METHODS //
  //////////////////////

  public String getTitle() {return title;}
  public List<Button> getButtons() {return buttons;}

  /////////////////////
  // MUTATOR METHODS //
  /////////////////////

  public void setTitle(String title) {this.title = title;}
  public void addButton(Button button) {this.buttons.add(button);}

  /////////////////////////
  // DEFAULT CONSTRUCTOR //
  /////////////////////////

  Page() {

  }
}