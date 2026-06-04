package de.tum.cit.fop.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private ResultScreen resultScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    private ShapeRenderer debugRenderer;

    // UI Skin
    private Skin skin;
    public NativeFileChooser fileChooser;
    Music currentMusic = null;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        AssetsLoader.load();
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        debugRenderer = new ShapeRenderer();
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin

        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        resultScreen = new ResultScreen(this);

        goToMenu(false); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu(boolean fromPauseGame) {
        setCurrentMusic(AssetsLoader.menuMusic);
        menuScreen.fromPauseGame = fromPauseGame;
        this.setScreen(menuScreen); // Set the current screen to MenuScreen
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        setCurrentMusic(AssetsLoader.gameMusic);
        this.setScreen(gameScreen); // Set the current screen to GameScreen
    }

    public void goToResult(String title) {
        setCurrentMusic(AssetsLoader.menuMusic);
        resultScreen.setTitle(title);
        this.setScreen(resultScreen);
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public ShapeRenderer getDebugRenderer() {
        return debugRenderer;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setCurrentMusic(Music music) {
        if (music == currentMusic) return;
        if (this.currentMusic != null) currentMusic.stop();
        currentMusic = music;
        currentMusic.play();
    }
}
