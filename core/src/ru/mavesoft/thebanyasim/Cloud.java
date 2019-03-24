package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Cloud {
    public float x;
    public float y;
    private short direction;
    private float speed = 5f;
    private int width = 200;
    private int height = 150;
    private Texture texture;

    public Cloud (short direction, float screenWidth, float screenHeight, AssetManager assetManager) {
        this.direction = direction;
        this.y = screenHeight - height - MathUtils.random(height);
        texture = assetManager.get(Assets.clouds[MathUtils.random(0, 3)]);
        if (direction == 0) {
            x = screenWidth;
        } else if (direction == 1) {
            x = 0 - width;
        }
    }

    public float getSpeed() {
        return speed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Texture getTexture() {
        return texture;
    }
}
