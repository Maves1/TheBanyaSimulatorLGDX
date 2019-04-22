package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MainBanyaGame extends Stage implements Screen {

    // Service variables
    GameManager game;
    boolean firstLaunch;

    Banya banya;

    Panel centerPanel;
    int centerPanelWidth = 250;
    int centerPanelHeight = 125;

    Panel statusPanel;
    int statusPanelWidth = GameManager.SCREEN_WIDTH;
    int statusPanelHeight = 50;

    PanelIndicator waterPanelIndicator;
    PanelIndicator besomPanelIndicator;
    int indicatorWidth = 40;

    // Textures
    Texture backgroundTexture;
    Texture banyaTexture;
    Texture sunTexture;
    TextureRegion sunRegion;
    Texture manTexture;
    Texture manBgTexture;
    Texture panelBgTexture;
    Texture waterIcoTexture;
    Texture besomsIcoTexture;

    //Images
    Image banyaImage;

    int manWidth = 300;
    int manHeight = 400;

    int banyaWidth = 350;
    int banyaHeight = 500;

    int sunWidth = 200;
    int sunHeight = 200;
    float sunRotation = 0.0f;
    float sunRotSpeed = 5.0f;

    Array<Cloud> arrayClouds;
    float timeLastCloudSpawned;
    float cloudSpawnFrequency = MathUtils.random(15, 20) * 1000000000f;
    short windDirection = (short) MathUtils.random(1);

    Array<Customer> arrayCustomers;
    float timeLastCustomerSpawned;
    float customerSpawnFrequency = MathUtils.random(5, 8) * 1000000000f;

    long timeSinceLastTouch;

    public MainBanyaGame(final GameManager gameManager) {
        // All needed variables are initialized here
        this.game = gameManager;
        banya = new Banya(game);
        centerPanel = new Panel(game, GameManager.SCREEN_WIDTH / 2 - centerPanelWidth / 2,
                GameManager.SCREEN_HEIGHT - 250, centerPanelWidth, centerPanelHeight);
        statusPanel = new Panel(game, 0,
                GameManager.SCREEN_HEIGHT - statusPanelHeight, statusPanelWidth, statusPanelHeight);

        // Textures
        backgroundTexture = game.assetManager.get(Assets.backgrounds[0]);
        banyaTexture = game.assetManager.get(Assets.banyas[0]);
        sunRegion = new TextureRegion((Texture) game.assetManager.get(Assets.sun));
        manTexture = game.assetManager.get(Assets.man);
        manBgTexture = game.assetManager.get(Assets.manBackground);
        panelBgTexture = game.assetManager.get(Assets.panelBackground);
        waterIcoTexture = game.assetManager.get(Assets.waterIndicator);
        besomsIcoTexture = game.assetManager.get(Assets.besomIndicator);

        //Images
        banyaImage = new Image(banyaTexture);
        banyaImage.setWidth(banyaWidth);
        banyaImage.setHeight(banyaHeight);
        banyaImage.setX(GameManager.SCREEN_WIDTH / 2 - banyaWidth / 2);
        banyaImage.setY(25);
        banyaImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                banya.buyBesoms();
                banya.buyWater();
            }
        });

        centerPanel.setBackground(panelBgTexture);
        centerPanel.addElement("banyaName", banya.getName(), 0, 30, 2);
        centerPanel.addElement("banyaMoney", Long.toString(banya.getMoney()), 0, 0, 2);

        statusPanel.setBackground(panelBgTexture);
        waterPanelIndicator = new PanelIndicator(waterIcoTexture,
                Integer.toString(banya.getWaterAmount()),
                GameManager.SCREEN_WIDTH - indicatorWidth * 4 - 20, 0,
                indicatorWidth, statusPanelHeight, 2);
        besomPanelIndicator = new PanelIndicator(besomsIcoTexture,
                Integer.toString(banya.getBesomsAmount()),
                GameManager.SCREEN_WIDTH - indicatorWidth * 2, 0, 50, statusPanelHeight, 2);
        statusPanel.addElement("waterAmountIndicator", waterPanelIndicator);
        statusPanel.addElement("besomsAmountIndicator", besomPanelIndicator);

        arrayClouds = new Array<Cloud>();
        arrayClouds.add(new Cloud(windDirection, GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT, game.assetManager));
        timeLastCloudSpawned = TimeUtils.nanoTime();

        arrayCustomers = new Array<Customer>();
        spawnNewCustomer();
        Gdx.input.setInputProcessor(this);
        this.addActor(banyaImage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.camera.update();

        banya.controlCustomers();
        centerPanel.setElement("banyaMoney", Long.toString(banya.getMoney()), 0, 0, 2);
        updateIndicators();

        game.spriteBatch.begin();
        drawEnvironment();
        centerPanel.draw();
        statusPanel.draw();
        game.spriteBatch.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        game.gamePreferences.putLong("banyaMoney", banya.getMoney());
        game.gamePreferences.putInteger("banyaWaterAmount", banya.getWaterAmount());
        game.gamePreferences.putInteger("banyaBesomsAmount", banya.getBesomsAmount());
        game.gamePreferences.flush();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        banyaTexture.dispose();
        sunTexture.dispose();
        manTexture.dispose();
        manBgTexture.dispose();
        panelBgTexture.dispose();
    }

    public void drawEnvironment() {
        processClouds();
        processCustomers();
        sunRotation += sunRotSpeed * Gdx.graphics.getDeltaTime();

        game.spriteBatch.draw(backgroundTexture, 0, 0, GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT);
        // game.spriteBatch.draw(banyaTexture, GameManager.SCREEN_WIDTH / 2 - banyaWidth / 2, 25, banyaWidth, banyaHeight);
        banyaImage.draw(game.spriteBatch, 1f);
        game.spriteBatch.draw(sunRegion, GameManager.SCREEN_WIDTH - 200, GameManager.SCREEN_HEIGHT - 225,
                sunWidth / 2, sunHeight / 2, sunWidth, sunHeight, 1.0f, 1.0f, sunRotation);
        for (Iterator<Cloud> iterator = arrayClouds.iterator(); iterator.hasNext(); ) {
            Cloud currCloud = iterator.next();
            if (windDirection == 0) {
                currCloud.x -= currCloud.getSpeed() * Gdx.graphics.getDeltaTime();
                if (currCloud.x < 0 - currCloud.getWidth()) {
                    iterator.remove();
                }
            } else if (windDirection == 1) {
                currCloud.x += currCloud.getSpeed() * Gdx.graphics.getDeltaTime();
                if (currCloud.x > GameManager.SCREEN_WIDTH) {
                    iterator.remove();
                }
            }
        }
        for (Cloud cloud : arrayClouds) {
            game.spriteBatch.draw(cloud.getTexture(), cloud.x, cloud.y, cloud.getWidth(), cloud.getHeight());
        }

        for (Iterator<Customer> iterator = arrayCustomers.iterator(); iterator.hasNext(); ) {
            Customer customer = iterator.next();
            customer.walk(Gdx.graphics.getDeltaTime());

            if (customer.getX() < 0 - customer.getWidth() && customer.direction == 0) {
                iterator.remove();
                customer.remove();
            } else if (customer.getX() > GameManager.SCREEN_WIDTH && customer.direction == 1) {
                iterator.remove();
                customer.remove();
            }
        }

        for (Customer customer : arrayCustomers) {
            game.spriteBatch.draw(customer.getTexture(), customer.getX(), customer.getY(),
                    customer.getWidth(), customer.getHeight());
        }
    }

    public void processClouds() {
        if ((TimeUtils.nanoTime() - timeLastCloudSpawned >= cloudSpawnFrequency)
                && (arrayClouds.size < 5)) {
            arrayClouds.add(new Cloud(windDirection, GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT, game.assetManager));
            timeLastCloudSpawned = TimeUtils.nanoTime();
            cloudSpawnFrequency = MathUtils.random(15, 20) * 1000000000f;
        }
    }

    public void processCustomers() {
        if ((TimeUtils.nanoTime() - timeLastCustomerSpawned >= customerSpawnFrequency)
                && (arrayCustomers.size < 5)) {
            spawnNewCustomer();
        }
    }

    public void updateIndicators() {
        waterPanelIndicator.updateValue(Integer.toString(banya.getWaterAmount()));
        besomPanelIndicator.updateValue(Integer.toString(banya.getBesomsAmount()));
    }

    public void spawnNewCustomer() {
        final Customer customer = new Customer(GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT, game.assetManager);
        customer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Tapped", "Customer");
                if (banya.takeCustomer(customer)) {
                    arrayCustomers.removeValue(customer, true);
                    customer.remove();
                }
            }
        });
        arrayCustomers.add(customer);
        this.addActor(customer);
        timeLastCustomerSpawned = TimeUtils.nanoTime();
    }

}
