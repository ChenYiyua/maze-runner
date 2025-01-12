package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.tum.cit.fop.maze.interfaces.Renderable;

public class Enemy extends BaseEntity implements Renderable {
    public Enemy(float x, float y) {
        super(x * 64, y * 64, 64, 64, Color.RED);
        collisionBox.set(x * 64 + 8, y * 64 + 8, 64 - 16, 64 - 16);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {

    }
}
