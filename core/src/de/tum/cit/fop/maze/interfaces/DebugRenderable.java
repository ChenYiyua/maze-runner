package de.tum.cit.fop.maze.interfaces;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// 线框渲染接口 提供 Debug 绘制功能
public interface DebugRenderable {
    void render(ShapeRenderer renderer, float delta);
}
