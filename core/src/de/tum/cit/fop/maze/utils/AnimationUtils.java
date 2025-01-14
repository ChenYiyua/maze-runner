package de.tum.cit.fop.maze.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;

// 动画工具类
public class AnimationUtils {
    public static Animation<TextureRegion> getAnimation(Texture texture, int frameWidth, int frameHeight, int row, int col, int count, float duration) {
        TextureRegion[][] regions = TextureRegion.split(texture, frameWidth, frameHeight);
        return new Animation<>(duration, Arrays.copyOfRange(regions[row], col, col + count));
    }
}
