package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class FirstLaunchScreen implements Screen {

    GameManager game;
    Texture bg;

    public FirstLaunchScreen(GameManager gameManager) {
        game = gameManager;
        bg = game.assetManager.get(Assets.backgrounds[0]);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.begin();
        game.spriteBatch.draw(bg, 0, 0, GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT);
        game.makeMan("Hey! This text will be replaced, \n for now just tap the screen");
        if (Gdx.input.justTouched()) {
            MyTextInputListener myTextInputListener = new MyTextInputListener();
            Gdx.input.getTextInput(myTextInputListener, "Give your banya a name:",
                    "", "Banya name");
        }
        game.spriteBatch.end();
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

    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {
            if (text.length() > 3 && text.length() < 12) {
                game.gamePreferences.putBoolean("firstLaunch", false);
                game.gamePreferences.putString("banyaName", text);
                game.gamePreferences.putInteger("banyaLevel", 0);
                game.gamePreferences.putLong("banyaMoney", 0);
                game.gamePreferences.flush();
                game.setScreen(new MainBanyaGame(game));
            }
        }

        @Override
        public void canceled () {
        }
    }
}
