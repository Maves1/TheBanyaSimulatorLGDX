package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen implements Screen {

    GameManager game;
    private long startTime;
    private final long SPLASH_DURATION = 3000000000L;
    private Texture background;

    public SplashScreen(GameManager gameManager) {
        game = gameManager;
        loadAssets();
        startTime = TimeUtils.nanoTime();
        background = new Texture(Assets.splashScreen);
        Gdx.app.log("is first launch", Boolean.toString(game.gamePreferences.getBoolean("firstLaunch")));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.begin();
        game.spriteBatch.draw(background, 0, 0, GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT);
        game.spriteBatch.end();

        if (game.assetManager.update() && TimeUtils.nanoTime() - startTime > SPLASH_DURATION) {
            if (game.gamePreferences.getBoolean("firstLaunch",true)) {
                game.setScreen(new FirstLaunchScreen(game));
            } else {
                game.setScreen(new MainBanyaGame(game));
            }
        }
    }

    public void loadAssets() {
        game.assetManager.load(Assets.backgrounds[game.gamePreferences.getInteger("banyaLevel", 0)], Texture.class);
        game.assetManager.load(Assets.banyas[game.gamePreferences.getInteger("banyaLevel", 0)], Texture.class);
        game.assetManager.load(Assets.clouds[0], Texture.class);
        game.assetManager.load(Assets.clouds[1], Texture.class);
        game.assetManager.load(Assets.clouds[2], Texture.class);
        game.assetManager.load(Assets.clouds[3], Texture.class);
        game.assetManager.load(Assets.man, Texture.class);
        game.assetManager.load(Assets.manBackground, Texture.class);
        game.assetManager.load(Assets.sun, Texture.class);
        game.assetManager.load(Assets.customers[0], Texture.class);
        game.assetManager.load(Assets.panelBackground, Texture.class);
        game.assetManager.load(Assets.waterIndicator, Texture.class);
        game.assetManager.load(Assets.besomIndicator, Texture.class);
        game.assetManager.load(Assets.woodIndicator, Texture.class);
        game.assetManager.load(Assets.btnPlus, Texture.class);
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
