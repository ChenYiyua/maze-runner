package de.tum.cit.fop.maze;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import de.tum.cit.fop.maze.entities.*;
import de.tum.cit.fop.maze.entities.Character;
import de.tum.cit.fop.maze.interfaces.DebugRenderable;
import de.tum.cit.fop.maze.interfaces.Renderable;

import java.io.IOException;
import java.util.Properties;

// 地图加载类 从 properties 文件生成可供游戏的地图
public class GameMap implements Renderable, DebugRenderable {
    Properties properties;
    Character character;
    int maxX = 0, maxY = 0;
    Array<Wall> walls = new Array<>();
    EntryPoint entryPoint = null;
    Array<ExitPoint> exitPoints = new Array<>();
    Array<Trap> traps = new Array<>();
    Array<Enemy> enemies = new Array<>();
    Array<Key> keys = new Array<>();
    Array<BaseEntity> enemiesWalls = new Array<>();
    Array<BlockEntity> blocks = new Array<>();
    Array<Heart> hearts = new Array<>();
    Array<SpeedProp> speedProps = new Array<>();
    FileHandle saveHandle;
    MazeRunnerGame game;
    TextureRegion background;
    public int keyCount = 0;

    public GameMap(FileHandle fileHandle, MazeRunnerGame game) {
        this.game = game;
        background = new TextureRegion(AssetsLoader.basicTilesTexture, 0, 16, 16, 16);
        reload(fileHandle);
    }

    public void reload() {
        clear();
        load(saveHandle);
    }

    public void reload(FileHandle fileHandle) {
        clear();
        load(fileHandle);
    }

    public void clear() {
        keyCount = 0;
        maxX = 0;
        maxY = 0;
        walls.clear();
        entryPoint = null;
        exitPoints.clear();
        traps.clear();
        enemies.clear();
        keys.clear();
        enemiesWalls.clear();
        blocks.clear();
        hearts.clear();
        speedProps.clear();
    }

    public void load(FileHandle fileHandle) {
        saveHandle = fileHandle;
        properties = new Properties();
        this.character = new Character(-100, -100);
        try {
            properties.load(fileHandle.reader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        blocks.add(character);
        for (Object key : properties.keySet()) {
            Object value = properties.get(key);
            if (key instanceof String && value instanceof String) {
                if (key.equals("Width") || key.equals("Height")) continue;
                String[] position = ((String) key).split(",");
                int x = Integer.parseInt(position[0]), y = Integer.parseInt(position[1]);
                maxX = Math.max(x, maxX);
                maxY = Math.max(y, maxY);
                switch (Integer.parseInt((String) value)) {
                    case 0:
                        Wall wall = new Wall(x, y);
                        walls.add(wall);
                        enemiesWalls.add(wall);
                        break;
                    case 1:
                        entryPoint = new EntryPoint(x, y, character);
                        enemiesWalls.add(entryPoint);
                        break;
                    case 2:
                        ExitPoint exitPoint = new ExitPoint(x, y, character, game);
                        exitPoints.add(exitPoint);
                        enemiesWalls.add(exitPoint);
                        break;
                    case 3:
                        traps.add(new Trap(x, y, character));
                        break;
                    case 4:
                        Enemy enemy = new Enemy(x, y, character);
                        enemies.add(enemy);
                        blocks.add(enemy);
                        break;
                    case 5:
                        keys.add(new Key(x, y, character));
                        keyCount++;
                        break;
                }
            }
        }
        entryPoint.setBlocks(blocks);
        for (ExitPoint exitPoint : exitPoints) {
            exitPoint.setBlocks(blocks);
        }
        for (Wall wall : walls) {
            wall.setBlocks(blocks);
        }
        for (Enemy enemy : enemies) {
            enemy.setBlocks(enemiesWalls);
        }
        // 道具心随机散布于地图之上
        for (int i = 0; i < maxX * maxY / 20; i++) {
            hearts.add(new Heart(MathUtils.random(maxX), MathUtils.random(maxY), character));
        }
        // 加速道具随机散布于地图之上
        for (int i = 0; i < maxX * maxY / 20; i++) {
            speedProps.add(new SpeedProp(MathUtils.random(maxX), MathUtils.random(maxY), character));
        }
        character.setExitPoints(exitPoints);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        // 限制玩家保持在地图边缘内部
        if (character.getX() < 0) character.setX(0);
        if (character.getX() > maxX * 64) character.setX(maxX * 64);
        if (character.getY() < 0) character.setY(0);
        if (character.getY() > maxY * 64) character.setY(maxY * 64);
        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxY; j++) {
                batch.draw(background, i * 64, j * 64, 64, 64);
            }
        }
        checkHearts(batch, delta);
        checkSpeedProps(batch, delta);
        for (Wall wall : walls) wall.render(batch, delta);
        if (entryPoint != null) entryPoint.render(batch, delta);
        for (ExitPoint exitPoint : exitPoints) exitPoint.render(batch, delta);
        for (Trap trap : traps) trap.render(batch, delta);
        checkEnemies(batch, delta);
        character.render(batch, delta);
        checkKeys(batch, delta);
    }

    @Override
    public void render(ShapeRenderer renderer, float delta) {
        if (Constants.IS_DEBUG) {
            for (Heart heart : hearts) heart.render(renderer, delta);
            for (SpeedProp speedProp : speedProps) speedProp.render(renderer, delta);
            for (Wall wall : walls) wall.render(renderer, delta);
            if (entryPoint != null) entryPoint.render(renderer, delta);
            for (ExitPoint exitPoint : exitPoints) exitPoint.render(renderer, delta);
            for (Trap trap : traps) trap.render(renderer, delta);
            for (Enemy enemy : enemies) enemy.render(renderer, delta);
            character.render(renderer, delta);
            for (Key key : keys) key.render(renderer, delta);
        }
    }

    private void checkHearts(SpriteBatch batch, float delta) {
        ArrayIterator<Heart> iterable = hearts.iterator();
        while (iterable.hasNext()) {
            Heart heart = iterable.next();
            if (!heart.enable) {
                iterable.remove();
            } else {
                heart.render(batch, delta);
            }
        }
    }

    private void checkSpeedProps(SpriteBatch batch, float delta) {
        ArrayIterator<SpeedProp> iterable = speedProps.iterator();
        while (iterable.hasNext()) {
            SpeedProp speedProp = iterable.next();
            if (!speedProp.enable) {
                iterable.remove();
            } else {
                speedProp.render(batch, delta);
            }
        }
    }

    private void checkEnemies(SpriteBatch batch, float delta) {
        ArrayIterator<Enemy> iterable = enemies.iterator();
        while (iterable.hasNext()) {
            Enemy enemy = iterable.next();
            if (!enemy.enable) {
                iterable.remove();
            } else {
                enemy.render(batch, delta);
            }
        }
    }

    private void checkKeys(SpriteBatch batch, float delta) {
        // 当钥匙被全部收集时就打开终点大门
        ArrayIterator<Key> iterable = keys.iterator();
        while (iterable.hasNext()) {
            Key key = iterable.next();
            if (!key.enable) {
                iterable.remove();
                if (keys.isEmpty()) {
                    for (ExitPoint exitPoint : exitPoints) exitPoint.unlock();
                }
            } else {
                key.render(batch, delta);
            }
        }
    }

    public Character getCharacter() {
        return character;
    }
}
