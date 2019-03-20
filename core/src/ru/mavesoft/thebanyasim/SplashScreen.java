package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen implements Screen {

    GameManager game;
    long startTime;
    final long SPLASH_DURATION = 3000000000L;
    Texture background;

    public SplashScreen(GameManager gameManager) {
        game = gameManager;
        loadAssets();
        startTime = TimeUtils.nanoTime();
        background = new Texture("banyas/banya0.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.begin();
        game.spriteBatch.draw(background, 0, 0);
        game.spriteBatch.end();

        if (game.assetManager.update() && TimeUtils.nanoTime() - startTime > SPLASH_DURATION) {
            if (game.gamePreferences.getBoolean("firstLaunch", true)) {
                game.setScreen(new FirstLaunchScreen(game));
            } else {
                game.setScreen(new MainBanyaGame(game));
            }
        }
    }

    public void loadAssets() {
        game.assetManager.load("backgrounds/background0.png", Texture.class);
        game.assetManager.load("banyas/banya1.png", Texture.class);
        game.assetManager.load("clouds/cloud1.png", Texture.class);
        game.assetManager.load("man.png", Texture.class);
        game.assetManager.load("manBackground.png", Texture.class);
        game.assetManager.load("sun.png", Texture.class);
        game.assetManager.load("customers/customer0.png", Texture.class);
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
        background.dispose();
    }
}
