package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.enums.Direction;
import de.tum.cit.fop.maze.interfaces.Renderable;
import de.tum.cit.fop.maze.utils.AnimationUtils;

// 主角实体 接受键盘输入控制
public class Character extends BlockEntity implements Renderable {
    private float walkAnimationStateTime;
    private float speed = 200.0f;
    private final Vector2 velocity = new Vector2();
    private final Animation<TextureRegion> downAnimation, rightAnimation, upAnimation, leftAnimation;
    private final TextureRegion arrowRegion;
    private boolean typedDown = false, typedLeft = false, typedUp = false, typedRight = false;
    private final InputAdapter inputAdapter;
    public int maxHearts = 3, currentHealth = maxHearts * 4;
    public boolean isDead = false;
    public float speedPropTime = 0.0f;
    public int score = 0;

    // 受伤间隔 防止帧伤
    private float hurtInterval = 0.0f;
    private Array<ExitPoint> exitPoints = null;

    public Character(float x, float y) {
        super(x, y, 64, 128, 8, 24, Color.GREEN);
        collisionBox.set(x + offsetX, y + offsetY, size.x - 16, size.y - 96);
        arrowRegion = new TextureRegion(AssetsLoader.spriteSheetTexture, 16 * 2, 0, 16, 16);
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
        float tmpSpeed = speed;
        if (speedPropTime > 0.0f) {
            speedPropTime -= delta;
            tmpSpeed *= 2;
        }
        if (typedLeft) velocity.x -= tmpSpeed;
        else if (typedRight) velocity.x += tmpSpeed;
        else if (typedUp) velocity.y += tmpSpeed;
        else if (typedDown) velocity.y -= tmpSpeed;
        if (velocity.y > 0) direction = Direction.UP;
        if (velocity.y < 0) direction = Direction.DOWN;
        if (velocity.x > 0) direction = Direction.RIGHT;
        if (velocity.x < 0) direction = Direction.LEFT;
        position.add(velocity.scl(delta));
        collisionBox.setPosition(position.x + offsetX, position.y + offsetY);
        walkAnimationStateTime += delta * tmpSpeed / 200.0f;
        // 受伤时变为红色
        if (hurtInterval > 0.0f) {
            batch.setColor(Color.RED);
            hurtInterval -= delta;
        }
        TextureRegion currentFrame = getAnimation().getKeyFrame(velocity.isZero() ? 0 : walkAnimationStateTime, true);
        batch.draw(currentFrame, position.x, position.y, size.x, size.y);
        batch.setColor(Color.WHITE);
        // 根据最近出口点的方向绘制箭头
        if (exitPoints != null) {
            float minLength = Float.MAX_VALUE;
            Vector2 minPosition = null;
            for (ExitPoint exitPoint : exitPoints) {
                float dst = exitPoint.position.cpy().dst(position);
                if (dst < minLength) {
                    minLength = dst;
                    minPosition = exitPoint.position.cpy();
                }
            }
            if (minPosition != null) {
                float angle = position.cpy().add(32, 64).sub(minPosition.add(32, 32)).angleDeg();
                batch.draw(arrowRegion, position.x + MathUtils.cosDeg(angle + 180f) * 64f, position.y + 32 + MathUtils.sinDeg(angle + 180f) * 64f, 32, 32, 64, 64, 1f, 1f, angle + 45f);
            }
        }
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

    public void addHealth(int health) {
        // 增加生命，将生命限制在最大血量内
        currentHealth = Math.min(health + currentHealth, maxHearts * 4);
    }

    // 返回值：是否成功扣除生命
    public boolean subHealth(int health) {
        if (hurtInterval <= 0) {
            // 0.6 秒无敌时间
            hurtInterval = 0.6f;
            // 减少生命，将生命限制在 0 以上
            currentHealth = Math.max(currentHealth - health, 0);
            if (currentHealth == 0) isDead = true;
            if (AssetsLoader.hurtSound != null) AssetsLoader.hurtSound.play();
            return true;
        }
        return false;
    }

    public void setExitPoints(Array<ExitPoint> exitPoints) {
        this.exitPoints = exitPoints;
    }

    public void stopInput() {
        typedLeft = false;
        typedDown = false;
        typedUp = false;
        typedRight = false;
    }
}
