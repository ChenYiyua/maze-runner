package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

// 资源加载器 保管纹理资源
public class AssetsLoader {
    public static Texture characterTexture = null;
    public static Texture basicTilesTexture = null;
    public static Texture mobsTexture = null;
    public static Texture objectsTexture = null;
    public static Texture thingsTexture = null;
    public static Texture spriteSheetTexture = null;
    public static Texture backgroundTexture = null;
    public static Music menuMusic = null;
    public static Music gameMusic = null;
    public static Sound keySound = null;
    public static Sound wonSound = null;
    public static Sound failSound = null;
    public static Sound hurtSound = null;
    public static Sound eatSound = null;

    public static void load() {
        characterTexture = new Texture("character.png");
        basicTilesTexture = new Texture("basictiles.png");
        mobsTexture = new Texture("mobs.png");
        objectsTexture = new Texture("objects.png");
        thingsTexture = new Texture("things.png");
        spriteSheetTexture = new Texture("spritesheet.png");
        backgroundTexture = new Texture("background.png");
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu.mp3"));
        menuMusic.setLooping(true);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("game.mp3"));
        gameMusic.setLooping(true);
        keySound = Gdx.audio.newSound(Gdx.files.internal("key.wav"));
        wonSound = Gdx.audio.newSound(Gdx.files.internal("won.wav"));
        failSound = Gdx.audio.newSound(Gdx.files.internal("fail.ogg"));
        hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.wav"));
        eatSound = Gdx.audio.newSound(Gdx.files.internal("eat.wav"));
    }
}
