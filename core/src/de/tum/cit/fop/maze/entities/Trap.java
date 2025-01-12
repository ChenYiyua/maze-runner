package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.interfaces.Renderable;

public class Trap extends BaseEntity implements Renderable {
    TextureRegion upRegion, downRegion;

    public Trap(float x, float y) {
        super(x * 64, y * 64, 64, 64, Color.RED);
        collisionBox.set(x * 64 + 8, y * 64 + 8, 64 - 16, 64 - 16);
        upRegion = new TextureRegion(AssetsLoader.thingsTexture, 128, 64, 16, 16);
        downRegion = new TextureRegion(AssetsLoader.thingsTexture, 96, 64, 16, 16);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(upRegion, position.x, position.y, size.x, size.y);
    }
}
