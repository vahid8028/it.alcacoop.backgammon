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

package it.alcacoop.backgammon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import it.alcacoop.backgammon.actors.Board;
import it.alcacoop.backgammon.fsm.BaseFSM;
import it.alcacoop.backgammon.fsm.GameFSM;
import it.alcacoop.backgammon.fsm.MenuFSM;
import it.alcacoop.backgammon.fsm.SimulationFSM;
import it.alcacoop.backgammon.layers.AppearanceScreen;
import it.alcacoop.backgammon.layers.GameScreen;
import it.alcacoop.backgammon.layers.MainMenuScreen;
import it.alcacoop.backgammon.layers.MatchOptionsScreen;
import it.alcacoop.backgammon.layers.OptionsScreen;
import it.alcacoop.backgammon.layers.SplashScreen;
import it.alcacoop.backgammon.layers.WelcomeScreen;
import it.alcacoop.backgammon.utils.JSONProperties;
import it.alcacoop.backgammon.utils.MatchRecorder;
import bsh.Interpreter;
import bsh.util.JConsole;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.buckosoft.fibs.BuckoFIBS.CommandDispatcherImpl;
import com.buckosoft.fibs.BuckoFIBS.CommandDispatcher.Command;


public class GnuBackgammon extends Game implements ApplicationListener {
  
  private static GameScreen gameScreen;
  private static MatchOptionsScreen matchOptionsScreen;
  private static MainMenuScreen menuScreen;
  private static OptionsScreen optionsScreen;
  private static WelcomeScreen welcomeScreen;
  private static AppearanceScreen appearanceScreen;
  private int resolutions[][] = {
    {1280,740},
    {800,480},
    {480,320}
  };
  public static int ss;
  private static String[] resname = {"hdpi", "mdpi", "ldpi"};
  
  private GameFSM gameFSM;
  private SimulationFSM simulationFSM;
  private MenuFSM menuFSM;
  
  public static BitmapFont font;
  public static TextureAtlas atlas;
  public static Skin skin;
  public static int resolution[];
  public static GnuBackgammon Instance;
  public static BaseFSM fsm;
  public Board board;
  public Screen currentScreen;
  public JSONProperties jp;
  public Preferences prefs, appearancePrefs;
  public SoundManager snd;
  public NativeFunctions myRequestHandler;
  public boolean isGNU = false;
  
  public MatchRecorder rec;
  public static String fname;
  
  /* NEW CODE */
  static Interpreter bsh;
  static JConsole mScriptConsole;
  static BeanShellEditor mScriptEditor;
  static CommandDispatcherImpl commandDispatcher;
  
  public GnuBackgammon(NativeFunctions n) {
    myRequestHandler = n;
  }
  
  public GnuBackgammon(NativeFunctions n, boolean _isGNU) {
    myRequestHandler = n;
    this.isGNU = _isGNU;
  }
  
  @Override
  public void create() {
    Gdx.graphics.setContinuousRendering(false);
    Gdx.graphics.requestRendering();

    Instance = this;
    prefs = Gdx.app.getPreferences("GameOptions");
    appearancePrefs = Gdx.app.getPreferences("Appearance");
    snd = new SoundManager();
    rec = new MatchRecorder();
    
    
    /* NEW CODE */
    mScriptConsole = new JConsole();
    bsh = new Interpreter(mScriptConsole);
    mScriptEditor = new BeanShellEditor();
    commandDispatcher = new CommandDispatcherImpl();
    setBsh("devconsole", mScriptConsole);
    setBsh("deveditor", mScriptEditor);
    setBsh("disp", commandDispatcher);
    setBsh("bsh", bsh);
    //runBsh("libs/devtools.bsh");
    runBsh(Gdx.files.internal("data/devtools.bsh").path());
    
    new Thread(bsh).start();
    //INIT CONNECTION AND LOGIN
    commandDispatcher.dispatch(Command.CONNECT_TO_SERVER);
    
    
    
    
    //CHECK SCREEN DIM AND SELECT CORRECT ATLAS
    int pWidth = Gdx.graphics.getWidth();
    if (pWidth<=480) ss = 2;
    else if (pWidth<=800) ss = 1;
    else ss = 0;
    resolution = resolutions[ss];
    
    fname = myRequestHandler.getDataDir()+"/data/match.";

    GnuBackgammon.Instance.jp = new JSONProperties(Gdx.files.internal("data/"+GnuBackgammon.Instance.getResName()+"/pos.json"));
    skin = new Skin(Gdx.files.internal("data/"+resname[ss]+"/myskin.json"));
    atlas = new TextureAtlas(Gdx.files.internal("data/"+resname[ss]+"/pack.atlas"));
    font = new BitmapFont(Gdx.files.internal("data/"+resname[ss]+"/checker.fnt"), false);
    TextureRegion r = font.getRegion();
    r.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    
    BitmapFont f  = skin.getFont("default-font");
    f.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    f  = skin.getFont("default-font");
    f.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    
    GnuBackgammon.atlas.addRegion("board", atlas.findRegion("B1"));
    GnuBackgammon.atlas.addRegion("boardbg", atlas.findRegion("B1-BG"));
    GnuBackgammon.atlas.addRegion("cb", atlas.findRegion("CS1-B"));
    GnuBackgammon.atlas.addRegion("cw", atlas.findRegion("CS1-W"));
    GnuBackgammon.atlas.addRegion("ch", atlas.findRegion("CS1-H"));
    
    board = new Board();
    
    gameFSM = new GameFSM(board);
    simulationFSM = new SimulationFSM(board);
    menuFSM = new MenuFSM(board);
    
    fsm = simulationFSM;
    
    gameScreen = new GameScreen();
    matchOptionsScreen = new MatchOptionsScreen();
    menuScreen = new MainMenuScreen();
    optionsScreen = new OptionsScreen();
    welcomeScreen = new WelcomeScreen();
    appearanceScreen = new AppearanceScreen();
    setScreen(new SplashScreen());
  }

  public String getResName() {
    return resname[ss];
  }
  
  public void goToScreen(int s) {
    switch (s) {
      case 0:
        currentScreen = welcomeScreen;
        myRequestHandler.showAds(false);
        setScreen(welcomeScreen);
        break;
        
      case 1:
        currentScreen = optionsScreen;
        myRequestHandler.showAds(false);
        setScreen(optionsScreen);
        break;
      
      case 2:
        currentScreen = menuScreen;
        myRequestHandler.showAds(false);
        setScreen(menuScreen);
        break;
      
      case 3:
        currentScreen = matchOptionsScreen;
        myRequestHandler.showAds(false);
        setScreen(matchOptionsScreen);
        break;
        
      case 4:
        currentScreen = gameScreen;
        myRequestHandler.showAds(true);
        setScreen(gameScreen);
        break;
        
      case 6:
        currentScreen = welcomeScreen;
        setScreen(welcomeScreen);
        break;
        
      case 7:
        currentScreen = appearanceScreen;
        setScreen(appearanceScreen);
        break;
    }
  }

  public void setFSM(String type) {
    if (fsm!=null) fsm.stop();
    
    if (type == "SIMULATED_FSM")
      fsm = simulationFSM;
    else if (type == "MENU_FSM")
      fsm = menuFSM;
    else if (type == "GAME_FSM")
      fsm = gameFSM;
    
    fsm.start();
  }
  
  
  /* NEW CODE */
  public static void evalBsh(String cmds) {
    try {
      bsh.eval(cmds);
    }
    catch (bsh.EvalError e) {
      System.out.println("BSH ERROR: "+e.toString());
    }
  }

  public static void runBsh(String filename) {
    String bsh_text ="";
    try {
      bsh_text = getContent(filename);
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    
    try {
      bsh.eval(bsh_text);
    }
    catch (bsh.EvalError e) {
      System.out.println("BSH ERROR: "+e.toString());
    }
  }

  public static void setBsh(String where, Object what) {
    try {
      bsh.set(where,what);
    }
    catch (bsh.EvalError e) {
      System.out.println("BSH ERROR: "+e.toString());
    }
  }

  
  public static String getContent(String fname) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(fname));
    String text ="";
    try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }
        text = sb.toString();
        
    } catch (Exception e) {
    } finally {
        br.close();
    }
    return text;
  }
}
