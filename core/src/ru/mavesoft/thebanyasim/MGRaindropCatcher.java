package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MGRaindropCatcher implements Screen {

    Panel gameOverPanel;
    Panel gameWonPanel;
    int endPanelWidth = 300;
    int endPanelHeight = 400;

    GameManager game;
    Banya banya;
    MainBanyaGame mainBanyaGame;

    private Rectangle bucketRect;
    private Texture bucketTexture;
    private Texture dropletTexture;
    private Texture bgTexture;
    private Sound dropSound;
    private Music bgMusic;

    private Array<Rectangle> droplets;

    private int dropletSpeed;

    private long timeSinceLastDrop;
    private long spawnFrequency;

    private long timeTillTheEnd;
    private long timeSinceLastMeasure;

    private boolean isRunning;
    private boolean lostGame;

    public MGRaindropCatcher(GameManager gameManager, Banya banya, MainBanyaGame mainBanyaGame) {
        this.game = gameManager;
        this.banya = banya;
        this.mainBanyaGame = mainBanyaGame;

        bgTexture = game.assetManager.get(Assets.backgrounds[0]);
        bucketTexture = game.assetManager.get(Assets.bucket);
        dropletTexture = game.assetManager.get(Assets.droplet);
        dropSound = Gdx.audio.newSound(Gdx.files.internal("minigames/raindropcatcher/dropsound.wav"));
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("minigames/raindropcatcher/bgmusic.mp3"));

        droplets = new Array<Rectangle>();

        bucketRect = new Rectangle();
        bucketRect.x = GameManager.SCREEN_WIDTH / 2 - bucketTexture.getWidth() / 2;
        bucketRect.y = 20;
        bucketRect.width = bucketTexture.getWidth();
        bucketRect.height = bucketTexture.getHeight();

        bgMusic.setLooping(true);
        bgMusic.play();

        gameOverPanelFill();
        gameWonPanelFill();

        spawnFrequency = 1000000000L;
        dropletSpeed = 100;
        startGame();
    }

    public void createADrop() {
        Rectangle dropRect = new Rectangle();
        dropRect.width = dropletTexture.getWidth();
        dropRect.height = dropletTexture.getHeight();
        dropRect.x = MathUtils.random(GameManager.SCREEN_WIDTH - dropRect.width);
        dropRect.y = GameManager.SCREEN_HEIGHT;
        droplets.add(dropRect);
        timeSinceLastDrop = TimeUtils.nanoTime();
    }

    public void startGame() {
        timeTillTheEnd = 30000000000L;
        createADrop();
        isRunning = true;
        lostGame = false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (isRunning) {

            if (timeTillTheEnd <= 0) {
                isRunning = false;
            }

            if (TimeUtils.nanoTime() - timeSinceLastMeasure >= 1000000000L) {
                timeTillTheEnd -= 1000000000;
                timeSinceLastMeasure = TimeUtils.nanoTime();
                spawnFrequency -= 35000000;
                dropletSpeed += 20;
            }

            if (Gdx.input.isTouched()) {
                bucketRect.x = Gdx.input.getX() - bucketTexture.getWidth() / 2;
                if (bucketRect.x <= 0) {
                    bucketRect.x = 0;
                } else if (bucketRect.x >= GameManager.SCREEN_WIDTH) {
                    bucketRect.x = GameManager.SCREEN_WIDTH - bucketTexture.getWidth();
                }
            }

            if (TimeUtils.nanoTime() - timeSinceLastDrop >= spawnFrequency) {
                createADrop();
            }

            for (Iterator<Rectangle> iter = droplets.iterator(); iter.hasNext(); ) {
                Rectangle drop = iter.next();
                drop.y -= dropletSpeed * Gdx.graphics.getDeltaTime();
                if (drop.y <= 0 - dropletTexture.getHeight()) {
                    iter.remove();
                    isRunning = false;
                    lostGame = true;
                }
                if (drop.overlaps(bucketRect)) {
                    dropSound.play();
                    iter.remove();
                }
            }
            game.spriteBatch.begin();
            game.spriteBatch.draw(bgTexture, 0, 0);
            game.spriteBatch.draw(bucketTexture, bucketRect.x, bucketRect.y);
            for (Rectangle drop : droplets) {
                game.spriteBatch.draw(dropletTexture, drop.x, drop.y);
            }
            game.bitmapFont.draw(game.spriteBatch, Long.toString(timeTillTheEnd / 1000000000L), GameManager.SCREEN_WIDTH / 2, GameManager.SCREEN_HEIGHT - 30);
            game.spriteBatch.end();
        } else {
            if (lostGame) {
                game.spriteBatch.begin();
                game.spriteBatch.draw(bgTexture, 0, 0);
                gameOverPanel.draw();
                game.spriteBatch.end();
                if (Gdx.input.justTouched()) {
                    banya.givePenalty(100);
                    bgMusic.stop();
                    game.setScreen(mainBanyaGame);
                }
            } else {
                game.spriteBatch.begin();
                game.spriteBatch.draw(bgTexture, 0, 0);
                gameWonPanel.draw();
                game.spriteBatch.end();
                if (Gdx.input.justTouched()) {
                    banya.addWater(20);
                    bgMusic.stop();
                    game.setScreen(mainBanyaGame);
                }
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
        dropletTexture.dispose();
        bucketTexture.dispose();
        bgMusic.dispose();
        dropSound.dispose();
    }

    public void gameOverPanelFill() {
        gameOverPanel = new Panel(game, GameManager.SCREEN_WIDTH / 2 - endPanelWidth / 2,
                GameManager.SCREEN_HEIGHT / 2 - 100, endPanelWidth, endPanelHeight);
        gameOverPanel.setBackground((Texture) game.assetManager.get(Assets.panelBackground));
        GlyphLayout glyphLayout = new GlyphLayout(game.bitmapFont, "Game Over!");
        gameOverPanel.addElement("gameOverSign", "Game Over!", (int) (gameOverPanel.getWidth() / 2 - glyphLayout.width / 2), endPanelHeight - 50, 2);
        glyphLayout = new GlyphLayout(game.bitmapFont, "Money: -100 \n\nTap the screen \nto continue");
        gameOverPanel.addElement("info", "Money: -100 \n\nTap the screen \nto continue", (int) (gameOverPanel.getWidth() / 2 - glyphLayout.width / 2), 130, 2);
    }

    public void gameWonPanelFill() {
        gameWonPanel = new Panel(game, GameManager.SCREEN_WIDTH / 2 - endPanelWidth / 2,
                GameManager.SCREEN_HEIGHT / 2 - 100, endPanelWidth, endPanelHeight);
        gameWonPanel.setBackground((Texture) game.assetManager.get(Assets.panelBackground));
        GlyphLayout glyphLayout = new GlyphLayout(game.bitmapFont, "You Won!");
        gameWonPanel.addElement("gameWonSign", "You Won!", (int) (gameWonPanel.getWidth() / 2 - glyphLayout.width / 2), endPanelHeight - 50, 2);
        glyphLayout = new GlyphLayout(game.bitmapFont, "Water: +20 \n\nTap the screen \nto continue");
        gameWonPanel.addElement("info", "Water: +20 \n\nTap the screen \nto continue", (int) (gameWonPanel.getWidth() / 2 - glyphLayout.width / 2), 130, 2);
    }
}
