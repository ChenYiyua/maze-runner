package de.tum.cit.fop.maze;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.fop.maze.entities.Character;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen extends InputAdapter implements Screen {
    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    Character character;
    GameMap map;

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

        character = new Character(40, 80);
        map = new GameMap("maps/level-5.properties", character);
        InputMultiplexer multiplexer = new InputMultiplexer(this, character.getInputAdapter());
        Gdx.input.setInputProcessor(multiplexer);
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        camera.position.set(character.getX() + 64, character.getY() + 32, 0);

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
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

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
// Additional methods and logic can be added as needed for the game screen
}
