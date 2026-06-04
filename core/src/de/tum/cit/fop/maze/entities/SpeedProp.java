package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.interfaces.Renderable;
import de.tum.cit.fop.maze.utils.AnimationUtils;

public class SpeedProp extends BaseEntity implements Renderable {
    TextureRegion region;
    float stateTime = 0.0f;
    private final Character character;
    public boolean enable = true;

    public SpeedProp(float x, float y, Character character) {
        super(x * 64, y * 64, 64, 64, Color.GOLD);
        collisionBox.set(x * 64 + 16, y * 64 + 16, 64 - 32, 64 - 32);
        region = new TextureRegion(AssetsLoader.spriteSheetTexture, 16 * 10, 16 * 12, 16, 16);
        this.character = character;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (character.isOverlaps(collisionBox) && enable) {
            // 每个加速道具增加 3 秒二倍速持续时间
            character.speedPropTime += 3.0f;
            enable = false;
            character.score += 30;
            AssetsLoader.eatSound.play();
        }
        stateTime += delta;
        if (enable) batch.draw(region, position.x, position.y, size.x, size.y);
    }
}
