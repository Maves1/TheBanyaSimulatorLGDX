package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class FirstLaunchScreen implements Screen {

    GameManager game;
    Texture bg;

    public FirstLaunchScreen(GameManager gameManager) {
        game = gameManager;
        bg = new Texture("background0.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.spriteBatch.begin();
        game.spriteBatch.draw(bg, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
        game.makeMan("test");
        if (Gdx.input.justTouched()) {
            MyTextInputListener myTextInputListener = new MyTextInputListener();
            Gdx.input.getTextInput(myTextInputListener, "Dialog Title", "Initial Textfield Value", "Hint Value");
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
            game.setScreen(new MainBanyaGame(game));
        }

        @Override
        public void canceled () {
        }
    }
}
