package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.interfaces.Renderable;
import de.tum.cit.fop.maze.utils.AnimationUtils;

// 道具心实体 为主角恢复生命
public class Heart extends BaseEntity implements Renderable {
    Animation<TextureRegion> animation;
    float stateTime = 0.0f;
    private final Character character;
    public boolean enable = true;

    public Heart(float x, float y, Character character) {
        super(x * 64, y * 64, 64, 64, Color.GOLD);
        collisionBox.set(x * 64 + 16, y * 64 + 16, 64 - 32, 64 - 32);
        animation = AnimationUtils.getAnimation(AssetsLoader.objectsTexture, 16, 16, 3, 0, 4, 0.2f);
        this.character = character;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (character.isOverlaps(collisionBox) && enable) {
            character.addHealth(1);
            enable = false;
        }
        stateTime += delta;
        if (enable) batch.draw(animation.getKeyFrame(stateTime, true), position.x, position.y, size.x, size.y);
    }
}
