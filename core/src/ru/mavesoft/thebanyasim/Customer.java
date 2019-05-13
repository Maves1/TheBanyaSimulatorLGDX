package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;

public class Customer extends Actor {
    public int direction;

    private float speed = 70f;

    private long timeOfEntering;

    private Texture texture;

    public Customer(int screenWidth, int screenHeight, AssetManager assetManager) {
        super();
        this.setWidth(100);
        this.setHeight(150);
        texture = assetManager.get(Assets.customers[0]);
        direction = MathUtils.random(1);
        this.setY(10);
        if (direction == 0) {
            this.setX(screenWidth + this.getWidth());
        } else {
            this.setX(0 - this.getWidth());
        }
    }

    public void walk(float delta) {
        if (direction == 0) {
            this.setX(this.getX() - speed * delta);
        } else {
            this.setX(this.getX() + speed * delta);
        }
    }

    public void startWashing() {
        timeOfEntering = TimeUtils.nanoTime();
    }

    public boolean contains(Vector2 pointPos) {
        if (pointPos.x > this.getX() && pointPos.x < this.getX() + this.getWidth() &&
                pointPos.y > this.getY() && pointPos.y < this.getY() + this.getHeight()) {
            return true;
        }
        return false;
    }

    public Texture getTexture() {
        return texture;
    }

    public long getTimeOfEntering() {
        return timeOfEntering;
    }
}
