package ru.mavesoft.thebanyasim;

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

    public Cloud (short direction, float screenHeight, float screenWidth) {
        this.direction = direction;
        this.y = screenHeight - height - MathUtils.random(height);
        // TODO: add different png files for clouds
        texture = new Texture("cloud1.png");
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
