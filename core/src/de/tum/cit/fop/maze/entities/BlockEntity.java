package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import de.tum.cit.fop.maze.enums.Direction;

// 接受墙体碰撞的实体
public class BlockEntity extends BaseEntity {
    public Direction direction = Direction.DOWN;
    public float offsetX, offsetY;

    public BlockEntity(float x, float y, float width, float height, float offsetX, float offsetY, Color debugColor) {
        super(x, y, width, height, debugColor);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

}
