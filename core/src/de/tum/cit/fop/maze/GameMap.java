package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.interfaces.Renderable;

import java.io.IOException;
import java.util.Properties;

public class GameMap implements Renderable {
    Properties properties;
    TextureRegion region;

    public GameMap(String internalPath) {
        properties = new Properties();
        try {
            properties.load(Gdx.files.internal(internalPath).reader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        region = new TextureRegion(AssetsLoader.basicTilesTexture, 0, 0, 16, 16);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        for (Object obj : properties.keySet()) {
            if (obj instanceof String) {
                if (obj.equals("Width") || obj.equals("Height")) continue;
                String[] position = ((String) obj).split(",");
                batch.draw(region, Integer.parseInt(position[0]) * 64, Integer.parseInt(position[1]) * 64, 64, 64);
            }
        }
    }
}
