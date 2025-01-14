package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.interfaces.Renderable;

// 陷阱实体 固定的可使主角受到碰撞伤害 伤害一次后消失
public class Trap extends BaseEntity implements Renderable {
    TextureRegion upRegion, downRegion;
    private boolean isUp = true;
    private final Character character;

    public Trap(float x, float y, Character character) {
        super(x * 64, y * 64, 64, 64, Color.RED);
        collisionBox.set(x * 64 + 8, y * 64 + 8, 64 - 16, 64 - 16);
        upRegion = new TextureRegion(AssetsLoader.thingsTexture, 128, 64, 16, 16);
        downRegion = new TextureRegion(AssetsLoader.thingsTexture, 96, 64, 16, 16);
        this.character = character;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (isUp) {
            batch.draw(upRegion, position.x, position.y, size.x, size.y);
            // 尖刺陷阱对玩家的伤害为 2，且伤害后尖刺陷阱缩回
            if (character.isOverlaps(collisionBox) && character.subHealth(2)) {
                isUp = false;
            }
        } else {
            batch.draw(downRegion, position.x, position.y, size.x, size.y);
        }
    }
}
