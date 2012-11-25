package it.alcacoop.gnubackgammon.layers;

import it.alcacoop.gnubackgammon.GnuBackgammon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class MatchOptionsScreen implements Screen {

  private Stage stage;
  
  public MatchOptionsScreen(){
    stage = new Stage(1005, 752, true);
    
    Label titleLabel = new Label("MATCH SETTINGS", GnuBackgammon.skin);
    Label difficultyLabel = new Label("Difficulty", GnuBackgammon.skin);
    Label playToLabel = new Label("Play to", GnuBackgammon.skin);
    Label doublingLabel = new Label("Doubling Cube", GnuBackgammon.skin);
    
    TextButton supremo = new TextButton("Supremo", GnuBackgammon.skin);
    TextButton grandMaster = new TextButton("GrandMaster", GnuBackgammon.skin);
    TextButton play1 = new TextButton("1", GnuBackgammon.skin);
    TextButton play3 = new TextButton("3", GnuBackgammon.skin);
    TextButton play5 = new TextButton("5", GnuBackgammon.skin);
    TextButton doublingYes = new TextButton("YES", GnuBackgammon.skin);
    final TextButton doublingNo = new TextButton("NO", GnuBackgammon.skin);
    
    supremo.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
      }
    });
    
    grandMaster.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
      }
    });
    
    ButtonGroup bg = new ButtonGroup();
    bg.add(supremo);
    bg.add(grandMaster);
    bg.setMinCheckCount(1);
    bg.setMaxCheckCount(1);
    
    Table table = new Table();
    table.debug();
    table.add(titleLabel).colspan(4).expand().fillY();
    table.row();
    table.add(difficultyLabel);
    table.add(supremo).expand().width(180).height(80);
    table.add(grandMaster).expand().width(180).height(80);
    table.row();
    table.add(playToLabel);
    table.add(play1).expand().width(90).height(80);
    table.add(play3).expand().width(90).height(80);
    table.add(play5).expand().width(90).height(80);
    table.row();
    table.add(doublingLabel);
    table.add(doublingYes).expand().width(100).height(80);
    table.add(doublingNo).expand().width(100).height(80);
    table.setFillParent(true);
    
    stage.addActor(table);
  }


  @Override
  public void render(float delta) {
    
    Gdx.gl.glClearColor(0, 0, 0, 0);
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    
    stage.act(delta);
    stage.draw();
    Table.drawDebug(stage);
  }


  @Override
  public void resize(int width, int height) {
  }

  
  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
//    stage.addAction(Actions.sequence(Actions.delay(2.0f), Actions.fadeIn(0.7f)));
  }

  @Override
  public void hide() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void dispose() {
  }
}