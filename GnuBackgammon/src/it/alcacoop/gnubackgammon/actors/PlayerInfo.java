/*
 ##################################################################
 #                     GNU BACKGAMMON MOBILE                      #
 ##################################################################
 #                                                                #
 #  Authors: Domenico Martella - Davide Saurino                   #
 #  E-mail: info@alcacoop.it                                      #
 #  Date:   19/12/2012                                            #
 #                                                                #
 ##################################################################
 #                                                                #
 #  Copyright (C) 2012   Alca Societa' Cooperativa                #
 #                                                                #
 #  This file is part of GNU BACKGAMMON MOBILE.                   #
 #  GNU BACKGAMMON MOBILE is free software: you can redistribute  # 
 #  it and/or modify it under the terms of the GNU General        #
 #  Public License as published by the Free Software Foundation,  #
 #  either version 3 of the License, or (at your option)          #
 #  any later version.                                            #
 #                                                                #
 #  GNU BACKGAMMON MOBILE is distributed in the hope that it      #
 #  will be useful, but WITHOUT ANY WARRANTY; without even the    #
 #  implied warranty of MERCHANTABILITY or FITNESS FOR A          #
 #  PARTICULAR PURPOSE.  See the GNU General Public License       #
 #  for more details.                                             #
 #                                                                #
 #  You should have received a copy of the GNU General            #
 #  Public License v3 along with this program.                    #
 #  If not, see <http://http://www.gnu.org/licenses/>             #
 #                                                                #
 ##################################################################
*/

package it.alcacoop.gnubackgammon.actors;

import it.alcacoop.gnubackgammon.GnuBackgammon;
import it.alcacoop.gnubackgammon.logic.MatchState;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PlayerInfo extends Table {

  private Label name;
  private Label score;
  private Label pips;
  private int color;
  
  public PlayerInfo(String title, int color, float width) {
    this.color = color;
    name = new Label(title , GnuBackgammon.skin);
    score = new Label("0", GnuBackgammon.skin);
    pips = new Label("200", GnuBackgammon.skin);
    
    TextureRegion region;
    if (color==1)
      region = GnuBackgammon.atlas.findRegion("cw-small");
    else 
      region = GnuBackgammon.atlas.findRegion("cb-small");
    Image i =new Image(region);
    
    setWidth(width);
    
    float w = width*0.6f;
    row().height(0);
    add().minWidth(w*0.7f).height(0);
    add().width(w*0.1f).height(0);
    add().width(w*0.3f).height(0);
    add(i).width(i.getWidth()).height(i.getHeight()).padRight(2+(2-GnuBackgammon.ss)).height(0);
    add().height(0);
    row();
    add(name).right();
    add();
    add(score).left();
    add(i).center().height(i.getHeight()).padRight(2+(2-GnuBackgammon.ss));
    add(pips);
  }
  

  public void setName(String name) {
    this.name.setText(name);
  }
  
  public void setScore() {
    this.score.setText(String.valueOf(MatchState.anScore[color]));
  }

  public void setPIPS() {
    this.pips.setText(String.valueOf(GnuBackgammon.Instance.board.getPIPS(color)));
  } 
  
  public void update() {
    setScore();
    setPIPS();
  }
}
