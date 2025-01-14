package de.tum.cit.fop.maze;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.fop.maze.entities.Character;
import de.tum.cit.fop.maze.entities.ExitPoint;
import de.tum.cit.fop.maze.entities.Key;
import de.tum.cit.fop.maze.interfaces.Renderable;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen extends InputAdapter implements Screen {
    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final Matrix4 hudMatrix = new Matrix4();
    private final HeartHud heartHud;
    private final KeyHud keyHud;
    private final BitmapFont font;
    GameMap map;
    public boolean isWin = false, isLose = false;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
        map = new GameMap(Gdx.files.internal("maps/level-1.properties"), game);
        hudMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        heartHud = new HeartHud(16, Gdx.graphics.getHeight() - 64);
        keyHud = new KeyHud(16, Gdx.graphics.getHeight() - 128);
        heartHud.setCharacter(map.getCharacter());
        keyHud.setKeysArray(map.keys);
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        if (map.getCharacter().isDead) isLose = true;
        if (isWin) game.goToResult("You Win!");
        if (isLose) game.goToResult("You Lose...");
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        camera.position.set(map.getCharacter().getX() + 64, map.getCharacter().getY() + 32, 0);

        camera.update(); // Update the camera

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getDebugRenderer().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        map.render(game.getSpriteBatch(), delta);

        game.getSpriteBatch().end(); // Important to call this after drawing everything

        game.getDebugRenderer().begin(ShapeRenderer.ShapeType.Line);

        map.render(game.getDebugRenderer(), delta);
        game.getDebugRenderer().end();

        game.getSpriteBatch().setProjectionMatrix(hudMatrix);
        game.getSpriteBatch().begin();
        heartHud.render(game.getSpriteBatch(), delta);
        keyHud.render(game.getSpriteBatch(), delta);
        game.getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
        hudMatrix.setToOrtho2D(0, 0, width, height);
        heartHud.setY(height - 64);
        keyHud.setY(height - 128);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
        if (isWin || isLose) {
            isWin = false;
            isLose = false;
            map.reload();
            heartHud.setCharacter(map.getCharacter());
            keyHud.setKeysArray(map.keys);
        }
        InputMultiplexer multiplexer = new InputMultiplexer(this, map.getCharacter().getInputAdapter());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        float zoom = amountY * 0.1f;
        if ((camera.zoom + zoom) > 0.5 && (camera.zoom + zoom) < 2.0) camera.zoom += zoom;
        return super.scrolled(amountX, amountY);
    }

    public GameMap getMap() {
        return map;
    }
}

// 心心容器 HUD 叠加层显示
class HeartHud implements Renderable {
    private float x, y;
    private float size = 48f;
    private Character character = null;
    private final TextureRegion heartRegion4, heartRegion3, heartRegion2, heartRegion1, heartRegion0;

    public HeartHud(float x, float y) {
        this.x = x;
        this.y = y;
        this.heartRegion4 = new TextureRegion(AssetsLoader.objectsTexture, 64, 0, 16, 16);
        this.heartRegion3 = new TextureRegion(AssetsLoader.objectsTexture, 80, 0, 16, 16);
        this.heartRegion2 = new TextureRegion(AssetsLoader.objectsTexture, 96, 0, 16, 16);
        this.heartRegion1 = new TextureRegion(AssetsLoader.objectsTexture, 112, 0, 16, 16);
        this.heartRegion0 = new TextureRegion(AssetsLoader.objectsTexture, 128, 0, 16, 16);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (character == null) return;
        float currentX = x;
        // 渲染满心
        for (int i = 0; i < character.currentHealth / 4; i++) {
            batch.draw(heartRegion4, x + currentX, y, size, size);
            currentX += size;
        }
        // 渲染半满心
        TextureRegion region = getLastRegion(character.currentHealth);
        if (region != null) {
            batch.draw(region, x + currentX, y, size, size);
            currentX += size;
        }
        // 渲染空心
        int characterLossHeart = character.maxHearts * 4 - character.currentHealth;
        for (int i = 0; i < characterLossHeart / 4; i++) {
            batch.draw(heartRegion0, x + currentX, y, size, size);
            currentX += size;
        }
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    private TextureRegion getLastRegion(int heart) {
        return switch (heart % 4) {
            case 0 -> null;
            case 1 -> heartRegion1;
            case 2 -> heartRegion2;
            case 3 -> heartRegion3;
            default -> throw new IllegalStateException("Unexpected value: " + heart % 4);
        };
    }

    public void setY(float y) {
        this.y = y;
    }
}

// 剩余钥匙 HUD 叠加层显示
class KeyHud implements Renderable {
    private float x, y;
    private Array<Key> keys = null;
    private TextureRegion region;

    public KeyHud(float x, float y) {
        this.x = x;
        this.y = y;
        region = new TextureRegion(AssetsLoader.spriteSheetTexture, 11 * 16, 10 * 16, 16, 16);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (keys == null) return;
        for (int i = 0; i < keys.size; i++) {
            batch.draw(region, 12 + x + i * 48, y, 48, 48);
        }
    }

    public void setKeysArray(Array<Key> keys) {
        this.keys = keys;
    }

    public void setY(float y) {
        this.y = y;
    }
}
