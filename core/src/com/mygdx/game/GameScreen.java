package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {

  private final SpriteBatch batch;
  private final OrthographicCamera camera;
  private final Player player;

  public GameScreen() {
    batch = new SpriteBatch();
    camera = new OrthographicCamera();
    camera.setToOrtho(false, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

    player = new Player();
  }

  @Override
  public void render(float delta) {
    // update
    camera.update();
    player.update();

    ScreenUtils.clear(1, 0, 0, 1);
    batch.begin();
    player.draw(batch);
    batch.end();
  }

  @Override
  public void dispose() {
    player.dispose();
  }


}
