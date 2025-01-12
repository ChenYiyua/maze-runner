package de.tum.cit.fop.maze;

import com.badlogic.gdx.graphics.Texture;

public class AssetsLoader {
    public static Texture characterTexture = null;
    public static Texture basicTilesTexture = null;
    public static Texture mobsTexture = null;
    public static Texture objectsTexture = null;
    public static Texture thingsTexture = null;
    public static Texture spriteSheetTexture = null;

    public static void load() {
        characterTexture = new Texture("character.png");
        basicTilesTexture = new Texture("basictiles.png");
        mobsTexture = new Texture("mobs.png");
        objectsTexture = new Texture("objects.png");
        thingsTexture = new Texture("things.png");
        spriteSheetTexture = new Texture("spritesheet.png");
    }
}
