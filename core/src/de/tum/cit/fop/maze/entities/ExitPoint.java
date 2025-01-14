package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.enums.Direction;
import de.tum.cit.fop.maze.interfaces.Renderable;

// 离开点实体 收集所有钥匙进入启用状态 在之前与墙的行为一致
public class ExitPoint extends BaseEntity implements Renderable {
    TextureRegion lockRegion, unlockRegion;
    private boolean isLocked = true;
    float x, y;
    private final Character character;
    private final MazeRunnerGame game;
    Array<BlockEntity> blocks = null;

    public ExitPoint(float x, float y, Character character, MazeRunnerGame game) {
        super(x * 64, y * 64, 64, 64, Color.YELLOW);
        lockRegion = new TextureRegion(AssetsLoader.thingsTexture, 0, 0, 16, 16);
        unlockRegion = new TextureRegion(AssetsLoader.thingsTexture, 0, 48, 16, 16);
        this.x = x;
        this.y = y;
        this.character = character;
        this.game = game;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (isLocked) {
            batch.draw(lockRegion, position.x, position.y, size.x, size.y);
            if (blocks != null) checkCollision();
        } else {
            batch.draw(unlockRegion, position.x, position.y, size.x, size.y);
            if (character.isOverlaps(collisionBox)) {
                game.getGameScreen().isWin = true;
            }
        }
    }

    public void unlock() {
        if (isLocked) {
            isLocked = false;
            collisionBox.set(x * 64 + 8, y * 64 + 8, 64 - 16, 64 - 16);
        }
    }

    public void setBlocks(Array<BlockEntity> blocks) {
        this.blocks = blocks;
    }

    public void checkCollision() {
        // 碰撞检测加自动脱出墙壁算法
        for (BlockEntity entity : blocks) {
            if (entity.isOverlaps(getCollisionBox())) {
                if (entity instanceof Character) {
                    if (entity.getCollisionBox().getX() > getCollisionBox().getX() && entity.direction == Direction.LEFT)
                        entity.setX(getCollisionBox().x + getCollisionBox().width - entity.offsetX);
                    if (entity.getCollisionBox().getX() <= getCollisionBox().getX() && entity.direction == Direction.RIGHT)
                        entity.setX(getCollisionBox().x - entity.getCollisionBox().width - entity.offsetX);
                    if (entity.getCollisionBox().getY() > getCollisionBox().getY() && entity.direction == Direction.DOWN)
                        entity.setY(getCollisionBox().y + getCollisionBox().height - entity.offsetY);
                    if (entity.getCollisionBox().getY() <= getCollisionBox().getY() && entity.direction == Direction.UP)
                        entity.setY(getCollisionBox().y - entity.getCollisionBox().height - entity.offsetY);
                }
                if (entity instanceof Enemy) {
                    if (entity.getCollisionBox().getX() > getCollisionBox().getX() && entity.direction == Direction.LEFT)
                        entity.setX(getCollisionBox().x + getCollisionBox().width - entity.offsetX + 12);
                    if (entity.getCollisionBox().getX() <= getCollisionBox().getX() && entity.direction == Direction.RIGHT)
                        entity.setX(getCollisionBox().x - entity.getCollisionBox().width - entity.offsetX - 12);
                    if (entity.getCollisionBox().getY() > getCollisionBox().getY() && entity.direction == Direction.DOWN)
                        entity.setY(getCollisionBox().y + getCollisionBox().height - entity.offsetY + 12);
                    if (entity.getCollisionBox().getY() <= getCollisionBox().getY() && entity.direction == Direction.UP)
                        entity.setY(getCollisionBox().y - entity.getCollisionBox().height - entity.offsetY - 12);
                    ((Enemy) entity).calcDirection();
                }
            }
        }
    }
}
