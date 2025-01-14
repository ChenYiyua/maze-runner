package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.enums.Direction;
import de.tum.cit.fop.maze.interfaces.Renderable;

// 墙实体 可移动实体不可穿过
public class Wall extends BaseEntity implements Renderable {
    private final TextureRegion region;
    private Array<BlockEntity> blocks = null;

    public Wall(int x, int y) {
        super(x * 64, y * 64, 64, 64, Color.CYAN);
        region = new TextureRegion(AssetsLoader.basicTilesTexture, 80, 0, 16, 16);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(region, position.x, position.y, 64, 64);
        if (blocks != null) checkCollision();
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