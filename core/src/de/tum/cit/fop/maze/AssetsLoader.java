package de.tum.cit.fop.maze;

import com.badlogic.gdx.graphics.Texture;

public class AssetsLoader {
    public static Texture characterTexture = null;
    public static Texture basicTilesTexture = null;

    public static void load() {
        characterTexture = new Texture("character.png");
        basicTilesTexture = new Texture("basictiles.png");
    }
}
