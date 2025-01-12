package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterable;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import de.tum.cit.fop.maze.entities.*;
import de.tum.cit.fop.maze.entities.Character;
import de.tum.cit.fop.maze.interfaces.DebugRenderable;
import de.tum.cit.fop.maze.interfaces.Renderable;

import java.io.IOException;
import java.util.Properties;

public class GameMap implements Renderable, DebugRenderable {
    Properties properties;
    Character character;
    int maxX = 0, maxY = 0;
    Array<Wall> walls = new Array<>();
    EntryPoint entryPoint = null;
    ExitPoint exitPoint = null;
    Array<Trap> traps = new Array<>();
    Array<Key> keys = new Array<>();

    public GameMap(String internalPath, Character character) {
        properties = new Properties();
        this.character = character;
        try {
            properties.load(Gdx.files.internal(internalPath).reader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                        walls.add(new Wall(x, y, character));
                        break;
                    case 1:
                        entryPoint = new EntryPoint(x, y, character);
                        break;
                    case 2:
                        exitPoint = new ExitPoint(x, y, character);
                        break;
                    case 3:
                        traps.add(new Trap(x, y));
                        break;
                    case 5:
                        keys.add(new Key(x, y, character));
                        break;
                }

            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        // 限制玩家保持在地图边缘内部
        if (character.getX() < 0) character.setX(0);
        if (character.getX() > maxX * 64) character.setX(maxX * 64);
        if (character.getY() < 0) character.setY(0);
        if (character.getY() > maxY * 64) character.setY(maxY * 64);
        for (Wall wall : walls) wall.render(batch, delta);
        if (entryPoint != null) entryPoint.render(batch, delta);
        if (exitPoint != null) exitPoint.render(batch, delta);
        for (Trap trap : traps) trap.render(batch, delta);
        character.render(batch, delta);
        ArrayIterator<Key> iterable = keys.iterator();
        while (iterable.hasNext()) {
            Key key = iterable.next();
            if (!key.enable) {
                iterable.remove();
            } else {
                key.render(batch, delta);
            }
        }
    }

    @Override
    public void render(ShapeRenderer renderer, float delta) {
        if (Constants.IS_DEBUG) {
            for (Wall wall : walls) wall.render(renderer, delta);
            if (entryPoint != null) entryPoint.render(renderer, delta);
            if (exitPoint != null) exitPoint.render(renderer, delta);
            for (Trap trap : traps) trap.render(renderer, delta);
            character.render(renderer, delta);
            for (Key key : keys) key.render(renderer, delta);
        }
    }
}
