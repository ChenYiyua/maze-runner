package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Constants;
import de.tum.cit.fop.maze.interfaces.DebugRenderable;

public class BaseEntity implements DebugRenderable {
    public final Vector2 position = new Vector2();
    public final Vector2 size = new Vector2();
    public final Rectangle collisionBox;
    private final Color debugColor;

    public BaseEntity(float x, float y, float width, float height, Color debugColor) {
        position.set(x, y);
        size.set(width, height);
        collisionBox = new Rectangle(x, y, width, height);
        this.debugColor = debugColor;
    }

    @Override
    public void render(ShapeRenderer renderer, float delta) {
        if (Constants.IS_DEBUG) {
            renderer.setColor(debugColor);
            renderer.rect(collisionBox.x, collisionBox.y, collisionBox.width, collisionBox.height);
        }
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setX(float x) {
        position.x = x;
    }

    public void setY(float y) {
        position.y = y;
    }

    public Rectangle getCollisionBox() {
        return collisionBox;
    }

    public boolean isOverlaps(Rectangle collisionBox) {
        return this.collisionBox.overlaps(collisionBox);
    }
}
