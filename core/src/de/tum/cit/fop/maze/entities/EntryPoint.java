package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.enums.Direction;
import de.tum.cit.fop.maze.interfaces.Renderable;

// 出生点实体 自动更新主角位置
public class EntryPoint extends BaseEntity implements Renderable {
    TextureRegion region;
    Array<BlockEntity> blocks = null;

    public EntryPoint(float x, float y, Character character) {
        super(x * 64, y * 64, 64, 64, Color.YELLOW);
        region = new TextureRegion(AssetsLoader.basicTilesTexture, 16, 32, 16, 16);
        character.setX(getX());
        character.setY(getY());
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        checkEnemyCollision();
        batch.draw(region, position.x, position.y, size.x, size.y);
    }

    public void setBlocks(Array<BlockEntity> blocks) {
        this.blocks = blocks;
    }

    public void checkEnemyCollision() {
        // 碰撞检测加自动脱出墙壁算法
        for (BlockEntity entity : blocks) {
            if (entity.isOverlaps(getCollisionBox())) {
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
