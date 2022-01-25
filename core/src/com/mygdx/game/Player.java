package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Player implements Disposable {

  private final Sprite playerSprite;
  private final Vector2 lookDirection;
  private int movementSpeed = 300;
  private int rotationSpeed = 300;

  public Player() {
    lookDirection = new Vector2(0, 1);
    playerSprite = new Sprite(new Texture("player.png"));
    playerSprite.setOrigin(playerSprite.getWidth() / 2, playerSprite.getHeight() / 3);
    playerSprite.setPosition((float) GameConfig.WORLD_WIDTH / 2 - playerSprite.getWidth() / 2, 0);
  }

  public void draw(Batch batch) {
    playerSprite.draw(batch);
  }

  public void update() {
    move();
  }

  @Override
  public void dispose() {
    playerSprite.getTexture().dispose();
  }

  private void move() {
    float lastPositionX = playerSprite.getX();
    float lastPositionY = playerSprite.getY();

    if (Gdx.input.isKeyPressed(Keys.W)) {
      float x = Gdx.graphics.getDeltaTime() * movementSpeed * lookDirection.x;
      float y = Gdx.graphics.getDeltaTime() * movementSpeed * lookDirection.y;
      playerSprite.translate(x, y);
    }

    if (Gdx.input.isKeyPressed(Keys.S)) {
      float x = Gdx.graphics.getDeltaTime() * movementSpeed * lookDirection.x;
      float y = Gdx.graphics.getDeltaTime() * movementSpeed * lookDirection.y;
      playerSprite.translate(-x, -y);
    }

    if (Gdx.input.isKeyPressed(Keys.A)) {
      lookDirection.rotateDeg(rotationSpeed * Gdx.graphics.getDeltaTime());
    }

    if (Gdx.input.isKeyPressed(Keys.D)) {
      lookDirection.rotateDeg(-rotationSpeed * Gdx.graphics.getDeltaTime());
    }

    playerSprite.setRotation(lookDirection.angleDeg() - 90);

    if (playerSprite.getX() < 0) {
      playerSprite.setX(lastPositionX);
    }
    if (playerSprite.getX() + playerSprite.getWidth() > GameConfig.WORLD_WIDTH) {
      playerSprite.setX(lastPositionX);
    }
    if (playerSprite.getY() < 0) {
      playerSprite.setY(lastPositionY);
    }
    if (playerSprite.getY() + playerSprite.getHeight() > GameConfig.WORLD_HEIGHT) {
      playerSprite.setY(lastPositionY);
    }
  }

}
