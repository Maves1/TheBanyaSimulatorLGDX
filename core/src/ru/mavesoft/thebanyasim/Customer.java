package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Customer {
    private int width = 100;
    private int height = 150;

    public float x;
    public float y;
    public int direction;

    private float speed = 20f;

    private Texture texture;

    public Customer(int screenWidth, int screenHeight, AssetManager assetManager) {
        texture = assetManager.get("customers/customer0.png");
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

    public void enterBanya() {

    }

    public void leaveBanya() {

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
}
