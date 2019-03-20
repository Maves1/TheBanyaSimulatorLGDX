package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameManager extends Game {

    public AssetManager assetManager;
    Preferences gamePreferences;
    SpriteBatch spriteBatch;
    BitmapFont bitmapFont;
    GlyphLayout layout;

    Texture manBackground;
    Texture man;

    int SCREEN_HEIGHT;
    int SCREEN_WIDTH;

    int manWidth = 300;
    int manHeight = 400;

    @Override
    public void create() {
        assetManager = new AssetManager();
        gamePreferences = Gdx.app.getPreferences("GamePreferences");
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        spriteBatch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(2f);
        layout = new GlyphLayout();

        manBackground = new Texture("manBackground.png");
        man = new Texture("man.png");

        this.setScreen(new SplashScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        spriteBatch.dispose();
        bitmapFont.dispose();
        manBackground.dispose();
        man.dispose();
    }

    public void makeMan(String message) {
        Color c = spriteBatch.getColor();
        spriteBatch.setColor(c.r, c.g, c.b, 0.5f);
        spriteBatch.draw(manBackground, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        spriteBatch.setColor(c.r, c.g, c.b, 1f);
        spriteBatch.draw(man, SCREEN_WIDTH - manWidth, -70, manWidth, manHeight);
        layout.setText(bitmapFont, message);
        bitmapFont.draw(spriteBatch, message, SCREEN_WIDTH / 2 - layout.width / 2, SCREEN_HEIGHT / 2);
    }
}