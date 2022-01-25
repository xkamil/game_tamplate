package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {

  public enum Weapon {
    PISTOL(200),
    AUTOMATIC_RIFLE(250),
    SHOTGUN(300);

    public final int bulletSpeed;

    Weapon(int bulletSpeed) {
      this.bulletSpeed = bulletSpeed;
    }
  }

  public static class Bullet {

    public final Sprite sprite;
    public final Vector2 direction;
    public final int speed;

    public Bullet(float x, float y, Sprite sprite, Vector2 direction, int speed) {
      this.sprite = sprite;
      sprite.setOriginCenter();
      sprite.setPosition(x, y);
      this.direction = direction;
      this.speed = speed;
    }

    public void updatePosition() {
      sprite.translateX(direction.x * speed * Gdx.graphics.getDeltaTime());
      sprite.translateY(direction.y * speed * Gdx.graphics.getDeltaTime());
    }

    public void draw(Batch batch) {
      sprite.draw(batch);
    }
  }

  OrthographicCamera camera;
  SpriteBatch batch;
  Sprite bulletSprite;
  Sprite playerSprite;
  Texture dotTexture;
  Weapon currentWeapon = Weapon.PISTOL;

  private Array<Bullet> bullets = new Array<>();

  private int playerSpeed = 150;

  @Override
  public void create() {
    batch = new SpriteBatch();
    bulletSprite = new Sprite(new Texture("bullet.png"));
    playerSprite = new Sprite(new Texture("soldier.png"));
    dotTexture = new Texture("dot.png");
    playerSprite.setSize(70, 70);
    playerSprite.setOriginCenter();

    camera = new OrthographicCamera();
    camera.setToOrtho(false, 800, 480);
  }

  @Override
  public void render() {
    //updates
    selectWeapon();
    playerMovement();
    bullets.forEach(Bullet::updatePosition);

    //drawing
    ScreenUtils.clear(1, 1, 1, 1);
    batch.begin();
    bullets.forEach(dot -> dot.draw(batch));
    playerSprite.draw(batch);
    batch.end();
  }

  @Override
  public void dispose() {
    batch.dispose();
    bulletSprite.getTexture().dispose();
    playerSprite.getTexture().dispose();
  }

  private void playerMovement() {
    if (Gdx.input.isKeyPressed(Keys.UP)) {
      playerSprite.translateY(Gdx.graphics.getDeltaTime() * playerSpeed);
    }

    if (Gdx.input.isKeyPressed(Keys.DOWN)) {
      playerSprite.translateY(-Gdx.graphics.getDeltaTime() * playerSpeed);
    }

    if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
      playerSprite.translateX(Gdx.graphics.getDeltaTime() * playerSpeed);
    }

    if (Gdx.input.isKeyPressed(Keys.LEFT)) {
      playerSprite.translateX(-Gdx.graphics.getDeltaTime() * playerSpeed);
    }

    Vector2 playerLookDirection = getPlayerLookingDirection();
    playerSprite.setRotation(playerLookDirection.angleDeg());

    shoot();
  }

  private Vector2 getMousePosition() {
    int posX = Gdx.input.getX();
    int posY = Gdx.input.getY();

    Vector3 inWorldCoords = camera.unproject(new Vector3(posX, posY, 0));
    return new Vector2(inWorldCoords.x, inWorldCoords.y);
  }

  private Vector2 getPlayerLookingDirection() {
    Vector2 playerVector = new Vector2(playerSprite.getOriginX() + playerSprite.getX(),
        playerSprite.getOriginY() + playerSprite.getY());
    Vector2 mousePosition = getMousePosition();

    return new Vector2(mousePosition.x - playerVector.x, mousePosition.y - playerVector.y).nor();
  }

  private void selectWeapon(){
    if(Gdx.input.isKeyJustPressed(Keys.NUM_1)){
      currentWeapon = Weapon.PISTOL;
    }

    if(Gdx.input.isKeyJustPressed(Keys.NUM_2)){
      currentWeapon = Weapon.AUTOMATIC_RIFLE;
    }

    if(Gdx.input.isKeyJustPressed(Keys.NUM_3)){
      currentWeapon = Weapon.SHOTGUN;
    }
  }

  private void shoot() {
    Vector2 direction = getPlayerLookingDirection();
    float x = playerSprite.getX() + playerSprite.getOriginX();
    float y = playerSprite.getY() + playerSprite.getOriginY();

    if (currentWeapon == Weapon.PISTOL) {
      if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
        Bullet bullet = new Bullet(x, y, new Sprite(dotTexture), direction, currentWeapon.bulletSpeed);
        bullets.add(bullet);
      }
    }

    if (currentWeapon == Weapon.AUTOMATIC_RIFLE) {
      if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
        Bullet bullet = new Bullet(x, y, new Sprite(dotTexture), direction, currentWeapon.bulletSpeed);
        bullets.add(bullet);
      }
    }

    if (currentWeapon == Weapon.SHOTGUN) {
      if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
        Vector2 direction1 = direction.cpy().setAngleDeg(direction.angleDeg() + 10);
        Vector2 direction2 = direction.cpy().setAngleDeg(direction.angleDeg() - 10);

        Bullet bullet1 = new Bullet(x, y, new Sprite(dotTexture), direction1, currentWeapon.bulletSpeed);
        Bullet bullet2 = new Bullet(x, y, new Sprite(dotTexture), direction2, currentWeapon.bulletSpeed);
        Bullet bullet3 = new Bullet(x, y, new Sprite(dotTexture), direction, currentWeapon.bulletSpeed);
        bullets.add(bullet1, bullet2, bullet3);
      }
    }
  }
}
