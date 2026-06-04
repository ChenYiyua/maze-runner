package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.interfaces.Renderable;

// 钥匙实体 收集所有钥匙打开终点大门
public class Key extends BaseEntity implements Renderable {
    TextureRegion region;
    private final Character character;
    public boolean enable = true;

    public Key(float x, float y, Character character) {
        super(x * 64, y * 64, 64, 64, Color.BLUE);
        collisionBox.set(x * 64 + 16, y * 64 + 8, 64 - 32, 64 - 16);
        region = new TextureRegion(AssetsLoader.spriteSheetTexture, 11 * 16, 10 * 16, 16, 16);
        this.character = character;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (character.isOverlaps(collisionBox)) {
            enable = false;
            character.score += 50;
            if (AssetsLoader.keySound != null) AssetsLoader.keySound.play();
        }
        if (enable) batch.draw(region, position.x, position.y, size.x, size.y);
    }
}
