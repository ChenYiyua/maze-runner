package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.enums.Direction;
import de.tum.cit.fop.maze.interfaces.Renderable;
import de.tum.cit.fop.maze.utils.AnimationUtils;

public class Character extends InputAdapter implements Renderable {
    private float walkAnimationStateTime;
    private final Vector2 position = new Vector2();
    private float speed = 200.0f;
    private final Vector2 velocity = new Vector2();
    private Direction direction = Direction.DOWN;
    private final Animation<TextureRegion> downAnimation, rightAnimation, upAnimation, leftAnimation;
    private boolean typedDown = false, typedLeft = false, typedUp = false, typedRight = false;

    public Character(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        downAnimation = AnimationUtils.getAnimation(AssetsLoader.characterTexture, 16, 32, 0, 0, 4, 0.2f);
        rightAnimation = AnimationUtils.getAnimation(AssetsLoader.characterTexture, 16, 32, 1, 0, 4, 0.2f);
        upAnimation = AnimationUtils.getAnimation(AssetsLoader.characterTexture, 16, 32, 2, 0, 4, 0.2f);
        leftAnimation = AnimationUtils.getAnimation(AssetsLoader.characterTexture, 16, 32, 3, 0, 4, 0.2f);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        velocity.set(0, 0);
        if (typedLeft) velocity.x -= speed;
        if (typedRight) velocity.x += speed;
        if (typedUp) velocity.y += speed;
        if (typedDown) velocity.y -= speed;
        if (velocity.y > 0) direction = Direction.UP;
        if (velocity.y < 0) direction = Direction.DOWN;
        if (velocity.x > 0) direction = Direction.RIGHT;
        if (velocity.x < 0) direction = Direction.LEFT;
        position.add(velocity.scl(delta));
        walkAnimationStateTime += delta * speed / 200.0f;
        TextureRegion currentFrame = getAnimation().getKeyFrame(velocity.isZero() ? 0 : walkAnimationStateTime, true);
        batch.draw(currentFrame, position.x, position.y, 64, 128);
    }

    private Animation<TextureRegion> getAnimation() {
        return switch (direction) {
            case UP -> upAnimation;
            case DOWN -> downAnimation;
            case LEFT -> leftAnimation;
            case RIGHT -> rightAnimation;
        };
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void typeShift(boolean isType) {
        speed = isType ? 400.0f : 200.0f;
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) typeShift(true);
        if (keycode == Input.Keys.UP) typedUp = true;
        if (keycode == Input.Keys.DOWN) typedDown = true;
        if (keycode == Input.Keys.LEFT) typedLeft = true;
        if (keycode == Input.Keys.RIGHT) typedRight = true;
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) typeShift(false);
        if (keycode == Input.Keys.UP) typedUp = false;
        if (keycode == Input.Keys.DOWN) typedDown = false;
        if (keycode == Input.Keys.RIGHT) typedRight = false;
        if (keycode == Input.Keys.LEFT) typedLeft = false;
        return super.keyUp(keycode);
    }
}
