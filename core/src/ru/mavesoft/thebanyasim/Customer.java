package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Customer {
    private int width = 100;
    private int height = 150;

    public float x;
    public float y;
    public int direction;

    private float speed = 21f;

    private long timeOfEntering;

    private Texture texture;

    public Customer(int screenWidth, int screenHeight, AssetManager assetManager) {
        texture = assetManager.get(Assets.customers[0]);
        direction = MathUtils.random(1);
        y = 10;
        if (direction == 0) {
            x = screenWidth + width;
        } else {
            x = 0 - width;
        }
    }

    public void walk(float delta) {
        if (direction == 0) {
            x -= speed * delta;
        } else {
            x += speed * delta;
        }
    }

    public void startWashing() {
        timeOfEntering = TimeUtils.nanoTime();
    }

    public boolean contains(Vector2 pointPos) {
        if (pointPos.x > this.x && pointPos.x < this.x + this.getWidth() &&
                pointPos.y > this.y && pointPos.y < this.y + this.getHeight()) {
            return true;
        }
        return false;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public long getTimeOfEntering() {
        return timeOfEntering;
    }
}
