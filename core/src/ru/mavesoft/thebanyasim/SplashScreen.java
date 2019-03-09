package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen implements Screen {

    GameManager game;
    AssetManager assetManager = new AssetManager();
    long startTime;
    Texture t = new Texture("banya0.png");

    public SplashScreen(GameManager gameManager) {
        game = gameManager;
        startTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.spriteBatch.begin();
        game.spriteBatch.draw(t, 0, 0);
        game.spriteBatch.end();

        if (assetManager.update() && TimeUtils.timeSinceMillis(startTime) > 5000) {
            if (game.gamePreferences.getBoolean("firstLaunch", true)) {
                game.setScreen(new FirstLaunchScreen(game));
            } else {
                game.setScreen(new MainBanyaGame(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
