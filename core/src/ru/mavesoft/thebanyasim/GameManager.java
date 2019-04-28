package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GameManager extends Game {

    public AssetManager assetManager;
    Preferences gamePreferences;
    OrthographicCamera camera;
    SpriteBatch spriteBatch;
    BitmapFont bitmapFont;
    GlyphLayout layout;
    ExtendViewport viewport;

    Banya banya;

    Texture manBackground;
    Texture man;

    static int SCREEN_HEIGHT;
    static int SCREEN_WIDTH;

    int manWidth = 300;
    int manHeight = 400;

    @Override
    public void create() {
        SCREEN_HEIGHT = 754;
        SCREEN_WIDTH = 480;
        assetManager = new AssetManager();
        gamePreferences = Gdx.app.getPreferences("GamePreferences");
        camera = new OrthographicCamera();
        camera.position.set(0, 0, 0);
        camera.setToOrtho(false, 480, 754);
        viewport = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        // SCREEN_HEIGHT = Gdx.graphics.getHeight();
        // SCREEN_WIDTH = Gdx.graphics.getWidth();
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(2f);
        layout = new GlyphLayout();

        manBackground = new Texture(Assets.manBackground);
        man = new Texture(Assets.man);

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
