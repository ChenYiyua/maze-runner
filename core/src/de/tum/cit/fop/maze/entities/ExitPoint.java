package de.tum.cit.fop.maze.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.AssetsLoader;
import de.tum.cit.fop.maze.interfaces.Renderable;

public class ExitPoint extends BaseEntity implements Renderable {
    TextureRegion lockRegion, unlockRegion;
    private boolean isLocked = true;
    float x, y;
    private final Character character;

    public ExitPoint(float x, float y, Character character) {
        super(x * 64, y * 64, 64, 64, Color.YELLOW);
        lockRegion = new TextureRegion(AssetsLoader.thingsTexture, 0, 0, 16, 16);
        unlockRegion = new TextureRegion(AssetsLoader.thingsTexture, 48, 0, 16, 16);
        this.x = x;
        this.y = y;
        this.character = character;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (isLocked) {
            batch.draw(lockRegion, position.x, position.y, size.x, size.y);
            checkCollision();
        } else {
            batch.draw(unlockRegion, position.x, position.y, size.x, size.y);
        }
    }

    public void unlock() {
        isLocked = false;
        collisionBox.set(x * 64 + 8, y * 64 + 8, 64 - 16, 64 - 16);
    }

    public void checkCollision() {
        // 碰撞检测加自动脱出墙壁算法 (仅对玩家生效)
        if (character.isOverlaps(getCollisionBox())) {
            if (character.getCollisionBox().getX() > getCollisionBox().getX() && character.getVelocity().x < 0)
                character.setX(getCollisionBox().x + getCollisionBox().width - 8);
            if (character.getCollisionBox().getX() <= getCollisionBox().getX() && character.getVelocity().x > 0)
                character.setX(getCollisionBox().x - character.getCollisionBox().width - 8);
            if (character.getCollisionBox().getY() > getCollisionBox().getY() && character.getVelocity().y < 0)
                character.setY(getCollisionBox().y + getCollisionBox().height - 24);
            if (character.getCollisionBox().getY() <= getCollisionBox().getY() && character.getVelocity().y > 0)
                character.setY(getCollisionBox().y - character.getCollisionBox().height - 24);
        }
    }
}
