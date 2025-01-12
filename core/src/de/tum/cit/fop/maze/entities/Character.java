package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.enums.Direction;
import de.tum.cit.fop.maze.interfaces.Renderable;
import de.tum.cit.fop.maze.utils.AnimationUtils;

public class Character extends BaseEntity implements Renderable {
    private float walkAnimationStateTime;
    private float speed = 200.0f;
    private int maxHearts = 3, currentHealth = maxHearts * 4;
    private final Vector2 velocity = new Vector2();
    private Direction direction = Direction.DOWN;
    private final Animation<TextureRegion> downAnimation, rightAnimation, upAnimation, leftAnimation;
    private boolean typedDown = false, typedLeft = false, typedUp = false, typedRight = false;
    private final InputAdapter inputAdapter;

    public Character(float x, float y) {
        super(x, y, 64, 128, Color.GREEN);
        collisionBox.set(x + 8, y + 24, size.x - 16, size.y - 96);
        downAnimation = AnimationUtils.getAnimation(AssetsLoader.characterTexture, 16, 32, 0, 0, 4, 0.2f);
        rightAnimation = AnimationUtils.getAnimation(AssetsLoader.characterTexture, 16, 32, 1, 0, 4, 0.2f);
        upAnimation = AnimationUtils.getAnimation(AssetsLoader.characterTexture, 16, 32, 2, 0, 4, 0.2f);
        leftAnimation = AnimationUtils.getAnimation(AssetsLoader.characterTexture, 16, 32, 3, 0, 4, 0.2f);
        inputAdapter = new InputAdapter() {
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
        };
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        velocity.set(0, 0);
        if (typedLeft) velocity.x -= speed;
        else if (typedRight) velocity.x += speed;
        else if (typedUp) velocity.y += speed;
        else if (typedDown) velocity.y -= speed;
        if (velocity.y > 0) direction = Direction.UP;
        if (velocity.y < 0) direction = Direction.DOWN;
        if (velocity.x > 0) direction = Direction.RIGHT;
        if (velocity.x < 0) direction = Direction.LEFT;
        position.add(velocity.scl(delta));
        collisionBox.setPosition(position.x + 8, position.y + 24);
        walkAnimationStateTime += delta * speed / 200.0f;
        TextureRegion currentFrame = getAnimation().getKeyFrame(velocity.isZero() ? 0 : walkAnimationStateTime, true);
        batch.draw(currentFrame, position.x, position.y, size.x, size.y);
    }

    private Animation<TextureRegion> getAnimation() {
        return switch (direction) {
            case UP -> upAnimation;
            case DOWN -> downAnimation;
            case LEFT -> leftAnimation;
            case RIGHT -> rightAnimation;
        };
    }

    public void typeShift(boolean isType) {
        speed = isType ? 400.0f : 200.0f;
    }

    public InputAdapter getInputAdapter() {
        return inputAdapter;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}
