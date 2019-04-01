package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MainBanyaGame implements Screen {
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
	
	Texture background;
	Texture banyaTexture;
	Texture sun;
	TextureRegion sunRegion;
	Texture man;
	Texture manBackground;
	Texture panelBackground;
	Texture waterdropIndicator;
	Texture besomsIndicator;

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
	float customerSpawnFrequency = MathUtils.random(7, 15) * 1000000000f;

	long timeSinceLastTouch;

	public MainBanyaGame(final GameManager gameManager) {
		// All needed variables are initialized here
		this.game = gameManager;
		// SCREEN_HEIGHT = Gdx.graphics.getHeight();
		// SCREEN_WIDTH = Gdx.graphics.getWidth();
		banya = new Banya(game);
		centerPanel = new Panel(game, GameManager.SCREEN_WIDTH / 2 - centerPanelWidth / 2,
				GameManager.SCREEN_HEIGHT - 250, centerPanelWidth, centerPanelHeight);
		statusPanel = new Panel(game, 0,
				GameManager.SCREEN_HEIGHT - statusPanelHeight, statusPanelWidth, statusPanelHeight);

		// Textures
		background = game.assetManager.get(Assets.backgrounds[0]);
		banyaTexture = game.assetManager.get(Assets.banyas[0]);
		sunRegion = new TextureRegion((Texture) game.assetManager.get(Assets.sun));
		man = game.assetManager.get(Assets.man);
		manBackground = game.assetManager.get(Assets.manBackground);
		panelBackground = game.assetManager.get(Assets.panelBackground);
		waterdropIndicator = game.assetManager.get(Assets.waterIndicator);
		besomsIndicator = game.assetManager.get(Assets.besomIndicator);

		centerPanel.setBackground(panelBackground);
		centerPanel.addElement("banyaName", banya.getName(), 0, 30, 2);
		centerPanel.addElement("banyaMoney", Long.toString(banya.getMoney()), 0, 0, 2);

		statusPanel.setBackground(panelBackground);
		waterPanelIndicator = new PanelIndicator(waterdropIndicator,
				Integer.toString(banya.getWaterAmount()),
				GameManager.SCREEN_WIDTH - indicatorWidth * 4 - 20, 0,
				indicatorWidth, statusPanelHeight, 2);
		besomPanelIndicator = new PanelIndicator(besomsIndicator,
				Integer.toString(banya.getBesomsAmount()),
				GameManager.SCREEN_WIDTH - indicatorWidth * 2, 0, 50, statusPanelHeight, 2);
		statusPanel.addElement("waterAmountIndicator", waterPanelIndicator);
		statusPanel.addElement("besomsAmountIndicator", besomPanelIndicator);

        arrayClouds = new Array<Cloud>();
        arrayClouds.add(new Cloud(windDirection, GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT, game.assetManager));
        timeLastCloudSpawned = TimeUtils.nanoTime();

        arrayCustomers = new Array<Customer>();
        arrayCustomers.add(new Customer(GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT, game.assetManager));
        timeLastCustomerSpawned = TimeUtils.nanoTime();
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.camera.update();

		banya.controlCustomers();
		centerPanel.setElement("banyaMoney", Long.toString(banya.getMoney()), 0, 0, 2);

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
	public void dispose () {
		background.dispose();
		banyaTexture.dispose();
		sun.dispose();
		man.dispose();
		manBackground.dispose();
		panelBackground.dispose();
	}

    public void drawEnvironment() {
		processClouds();
		processCustomers();
		sunRotation += sunRotSpeed * Gdx.graphics.getDeltaTime();
		
		game.spriteBatch.draw(background, 0, 0, GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT);
		game.spriteBatch.draw(banyaTexture, GameManager.SCREEN_WIDTH / 2 - banyaWidth / 2, 25, banyaWidth, banyaHeight);
		game.spriteBatch.draw(sunRegion, GameManager.SCREEN_WIDTH - 200, GameManager.SCREEN_HEIGHT - 225,
                sunWidth / 2, sunHeight / 2, sunWidth,  sunHeight, 1.0f, 1.0f, sunRotation);
		for (Iterator<Cloud> iterator = arrayClouds.iterator(); iterator.hasNext();) {
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

		    if (customer.x < 0 - customer.getWidth() && customer.direction == 0) {
		    	iterator.remove();
			} else if (customer.x > GameManager.SCREEN_WIDTH && customer.direction == 1) {
		    	iterator.remove();
			}
			if (Gdx.input.justTouched() && TimeUtils.nanoTime() - timeSinceLastTouch > 600000000L) {
				Vector2 touchPos = new Vector2(Gdx.input.getX(), GameManager.SCREEN_HEIGHT - Gdx.input.getY());

				// Gdx.app.log("Touch Position", touchPos.x + " " + touchPos.y);
				// Gdx.app.log("Customer Position", customer.x + " " + customer.y);

				if (customer.contains(touchPos)) {
					if (banya.takeCustomer(customer)) {
						timeSinceLastTouch = TimeUtils.nanoTime();
						iterator.remove();
					}
				}
			}
        }

        for (Customer customer : arrayCustomers) {
        	game.spriteBatch.draw(customer.getTexture(), customer.x, customer.y,
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
			arrayCustomers.add(new Customer(GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT, game.assetManager));
			timeLastCustomerSpawned = TimeUtils.nanoTime();
			customerSpawnFrequency = MathUtils.random(7, 15) * 1000000000f;
		}
	}
}
