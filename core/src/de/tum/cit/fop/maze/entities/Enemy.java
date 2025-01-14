package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.enums.Direction;
import de.tum.cit.fop.maze.interfaces.Renderable;
import de.tum.cit.fop.maze.utils.AnimationUtils;

// 可移动敌人实体
public class Enemy extends BlockEntity implements Renderable {
    private float walkAnimationStateTime;
    private float speed = 100f;
    private final Animation<TextureRegion> downAnimation, rightAnimation, upAnimation, leftAnimation;
    private final Rectangle downBox, rightBox, upBox, leftBox;
    private boolean canDown, canRight, canUp, canLeft;
    private final Character character;
    public boolean enable = true;
    private Array<BaseEntity> blocks = null;
    private float calcTime = 0.0f;
    // 警戒半径，在此范围内敌人将发现角色
    private float warningRadius = 256.0f;
    private boolean isWarning = false;

    public Enemy(float x, float y, Character character) {
        super(x * 64, y * 64, 64, 64, 16, 16, Color.RED);
        collisionBox.set(x * 64 + offsetX, y * 64 + offsetY, 64 - 32, 64 - 32);
        downAnimation = AnimationUtils.getAnimation(AssetsLoader.mobsTexture, 16, 16, 4, 0, 3, 0.2f);
        rightAnimation = AnimationUtils.getAnimation(AssetsLoader.mobsTexture, 16, 16, 6, 0, 3, 0.2f);
        upAnimation = AnimationUtils.getAnimation(AssetsLoader.mobsTexture, 16, 16, 7, 0, 3, 0.2f);
        leftAnimation = AnimationUtils.getAnimation(AssetsLoader.mobsTexture, 16, 16, 5, 0, 3, 0.2f);
        downBox = new Rectangle(0, 0, 30, 48);
        rightBox = new Rectangle(0, 0, 48, 30);
        upBox = new Rectangle(0, 0, 30, 48);
        leftBox = new Rectangle(0, 0, 48, 30);
        this.character = character;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        // 根据警戒半径设置是否处于警戒状态
        setWarning(position.cpy().add(32, 32).dst(character.position.cpy().add(32, 32)) < warningRadius);
        if (character.isOverlaps(collisionBox) && character.subHealth(1)) {
            enable = false;
        }
        switch (direction) {
            case UP -> position.add(0, speed * delta);
            case DOWN -> position.add(0, -speed * delta);
            case LEFT -> position.add(-speed * delta, 0);
            case RIGHT -> position.add(speed * delta, 0);
        }
        collisionBox.setPosition(position.x + 16, position.y + 16);
        downBox.setPosition(position.x + 16, position.y - 32);
        rightBox.setPosition(position.x + 48, position.y + 16);
        upBox.setPosition(position.x + 16, position.y + 48);
        leftBox.setPosition(position.x - 32, position.y + 16);
        calcTime -= delta;
        walkAnimationStateTime += delta;
        if (calcTime < 0f) {
            calcDirection();
            calcTime = isWarning ? MathUtils.random(0.5f, 1.0f) : MathUtils.random(0.5f, 6.0f);
        }
        if (enable)
            batch.draw(getAnimation().getKeyFrame(walkAnimationStateTime, true), position.x, position.y, size.x, size.y);
    }

    public void setWarning(boolean isWarning) {
        // 如果发现了主角就立即开始搜索
        if (!this.isWarning && isWarning) {
            calcTime = 0.0f;
        }
        this.isWarning = isWarning;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // 增强 Debug 渲染方法
    @Override
    public void render(ShapeRenderer renderer, float delta) {
        super.render(renderer, delta);
        renderer.setColor(canDown ? Color.GREEN : Color.RED);
        renderer.rect(downBox.x, downBox.y, downBox.width, downBox.height);
        renderer.setColor(canUp ? Color.GREEN : Color.RED);
        renderer.rect(upBox.x, upBox.y, upBox.width, upBox.height);
        renderer.setColor(canLeft ? Color.GREEN : Color.RED);
        renderer.rect(leftBox.x, leftBox.y, leftBox.width, leftBox.height);
        renderer.setColor(canRight ? Color.GREEN : Color.RED);
        renderer.rect(rightBox.x, rightBox.y, rightBox.width, rightBox.height);
        renderer.setColor(isWarning ? Color.RED : Color.BLUE);
        renderer.circle(position.x, position.y, warningRadius);
    }

    private Animation<TextureRegion> getAnimation() {
        return switch (direction) {
            case UP -> upAnimation;
            case DOWN -> downAnimation;
            case LEFT -> leftAnimation;
            case RIGHT -> rightAnimation;
        };
    }

    public Direction calcDirection() {
        canUp = true;
        canDown = true;
        canRight = true;
        canLeft = true;
        for (BaseEntity entity : blocks) {
            if (canUp && entity.isOverlaps(upBox)) {
                canUp = false;
            }
            if (canDown && entity.isOverlaps(downBox)) {
                canDown = false;
            }
            if (canRight && entity.isOverlaps(rightBox)) {
                canRight = false;
            }
            if (canLeft && entity.isOverlaps(leftBox)) {
                canLeft = false;
            }
        }
        Array<Direction> directions = new Array<>();
        Direction choose = null;
        // 看到主角 开始智能寻路
        if (isWarning) {
            float xOffset = position.x - character.position.x;
            float yOffset = position.y - character.position.y;
            if (Math.abs(xOffset) < Math.abs(yOffset)) {
                if (xOffset > 0 && canLeft) choose = Direction.LEFT;
                if (xOffset < 0 && canRight) choose = Direction.RIGHT;
                if (yOffset < 0 && canUp) choose = Direction.UP;
                if (yOffset > 0 && canDown) choose = Direction.DOWN;
            } else {
                if (yOffset < 0 && canUp) choose = Direction.UP;
                if (yOffset > 0 && canDown) choose = Direction.DOWN;
                if (xOffset > 0 && canLeft) choose = Direction.LEFT;
                if (xOffset < 0 && canRight) choose = Direction.RIGHT;
            }
        }
        // 敌人随机巡游模式
        if (choose == null) {
            if (canUp) directions.add(Direction.UP);
            if (canDown) directions.add(Direction.DOWN);
            if (canRight) directions.add(Direction.RIGHT);
            if (canLeft) directions.add(Direction.LEFT);
            choose = directions.random();
        }
        // 被完全卡死 只能随机寻路
        if (choose == null) {
            directions.add(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
            choose = directions.random();
        }
        setDirection(choose);
        return choose;
    }

    // 设置障碍物 怪物寻路时会躲避障碍物
    public void setBlocks(Array<BaseEntity> blocks) {
        this.blocks = blocks;
    }
}
