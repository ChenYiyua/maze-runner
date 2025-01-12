package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.interfaces.Renderable;

public class EntryPoint extends BaseEntity implements Renderable {
    TextureRegion region;

    public EntryPoint(float x, float y, Character character) {
        super(x * 64, y * 64, 64, 64, Color.YELLOW);
        region = new TextureRegion(AssetsLoader.basicTilesTexture, 16, 32, 16, 16);
        character.setX(getX());
        character.setY(getY());
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(region, position.x, position.y, size.x, size.y);
    }
}
