package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class FirstLaunchScreen implements Screen {

    GameManager game;
    Server server;
    Texture bg;
    Stage stage;
    Image btnLogin;
    Image btnRegister;
    int btnWidth = 170;
    int btnHeight = 90;
    boolean showButtons = false;

    String login;
    String password;

    String[] introMessages = {"Hey, neighbour! (Tap the \nscreen to continue)", "We have a problem: \n" +
            "our banya was set on fire!", "So, I offer you \nto start our own business!", ""};
    int introCounter;

    String[] regMessages = {"Banya name", "Make up a name for your banya (login, 3 to 12 characters):", "Password",
                            "Make up a password:"};
    String[] logMessages = {"Your Login", "login", "Your Password", "password"};

    int lOrRCounter;

    public FirstLaunchScreen(GameManager gameManager) {
        game = gameManager;
        bg = game.assetManager.get(Assets.backgrounds[0]);
        introCounter = 0;
        lOrRCounter = 0;
        stage = new Stage();
        stage.setViewport(game.viewport);
        stage.getViewport().getCamera().position.set(0 - GameManager.SCREEN_WIDTH / 2, 0, 0);
        btnLogin = new Image((Texture) game.assetManager.get(Assets.btnLogin));
        btnRegister = new Image((Texture) game.assetManager.get(Assets.btnRegister));
        btnLogin.setBounds(GameManager.SCREEN_WIDTH / 2 - btnWidth / 2, 410, btnWidth, btnHeight);
        btnRegister.setBounds(GameManager.SCREEN_WIDTH / 2 - btnWidth / 2, 310, btnWidth, btnHeight);

        btnLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LogTextInput logTextInput = new LogTextInput();
                Gdx.input.getTextInput(logTextInput, logMessages[lOrRCounter], "", logMessages[lOrRCounter + 1]);
            }
        });

        btnRegister.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RegTextInput regTextInput = new RegTextInput();
                Gdx.input.getTextInput(regTextInput, regMessages[lOrRCounter], "", regMessages[lOrRCounter + 1]);
            }
        });
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
        game.makeMan(introMessages[introCounter]);
        if (Gdx.input.justTouched()) {
            if (introCounter < 3) {
                introCounter++;
            }
            if (introCounter == 3) {
                Gdx.input.setInputProcessor(stage);
                stage.addActor(btnLogin);
                stage.addActor(btnRegister);
                showButtons = true;
            }
        }
        if (showButtons) {
            btnLogin.draw(game.spriteBatch, 1.0f);
            btnRegister.draw(game.spriteBatch, 1.0f);
        }
        game.spriteBatch.end();
        stage.getCamera().update();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        stage.getCamera().viewportWidth = GameManager.SCREEN_WIDTH;
        stage.getCamera().viewportHeight = GameManager.SCREEN_HEIGHT;
        stage.getCamera().position.set(GameManager.SCREEN_WIDTH / 2, GameManager.SCREEN_HEIGHT / 2, 0);
        stage.getCamera().update();
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

    public class LogTextInput implements Input.TextInputListener {
        @Override
        public void input(String text) {
            if (lOrRCounter == 0) {
                if (text.length() > 3 && text.length() < 12) {
                    LoginChecker loginChecker = new LoginChecker(text);
                    if (loginChecker.loginExists()) {
                        saveLogin(text);
                        lOrRCounter += 2;
                        Gdx.input.getTextInput(new RegTextInput(), regMessages[lOrRCounter], "", regMessages[lOrRCounter + 1]);
                    }
                }
            } else {
                if (text.length() >= 6 && text.length() <= 16) {
                    server = new Server(Server.TYPE_LOGIN, login, text);
                    server.start();
                }
            }
        }

        @Override
        public void canceled() {

        }
    }

    public class RegTextInput implements Input.TextInputListener {
        @Override
        public void input (String text) {
            if (lOrRCounter == 0) {
                if (text.length() > 3 && text.length() < 12) {
                    saveLogin(text);
                    lOrRCounter += 2;
                    Gdx.input.getTextInput(new RegTextInput(), regMessages[lOrRCounter], "", regMessages[lOrRCounter + 1]);
                }
            } else {
                if (text.length() >= 6 && text.length() <= 16) {
                    savePassword(text);
                    game.gamePreferences.putBoolean("firstLaunch", false);
                    game.gamePreferences.putString("banyaName", login);
                    game.gamePreferences.putString("banyaPassword", password);
                    game.gamePreferences.putInteger("banyaLevel", 0);
                    game.gamePreferences.putLong("banyaMoney", 0);
                    game.gamePreferences.putInteger("banyaWaterAmount", 100);
                    game.gamePreferences.putInteger("banyaBesomsAmount", 2);
                    game.gamePreferences.putInteger("banyaWoodAmount", 5);
                    game.gamePreferences.flush();
                    server = new Server(Server.TYPE_REGISTRATION, login, password);
                    server.start();
                    game.setScreen(new MainBanyaGame(game));
                }
            }
        }

        @Override
        public void canceled () {
            lOrRCounter = 0;
        }
    }

    public void saveLogin(String login) {
        this.login = login;
    }

    public void savePassword(String password) {
        this.password = password;
    }
}
