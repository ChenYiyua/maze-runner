package de.tum.cit.fop.maze.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// 渲染接口 提供游戏纹理绘制功能
public interface Renderable {
    void render(SpriteBatch batch, float delta);
}
