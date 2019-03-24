package ru.mavesoft.thebanyasim;

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
	
	Texture background;
	Texture banyaTexture;
	Texture sun;
	TextureRegion sunRegion;
	Texture man;
	Texture manBackground;

	int manWidth = 300;
	int manHeight = 400;

	int banyaWidth = 300;
	int banyaHeight = 400;

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
	float customerSpawnFrequency = MathUtils.random(7, 20) * 1000000000f;

	public MainBanyaGame(final GameManager gameManager) {
		// All needed variables are initialized here
		this.game = gameManager;
		// SCREEN_HEIGHT = Gdx.graphics.getHeight();
		// SCREEN_WIDTH = Gdx.graphics.getWidth();
		banya = new Banya(game);

		// Textures
		background = game.assetManager.get(Assets.backgrounds[0]);
		banyaTexture = game.assetManager.get(Assets.banyas[0]);
		sunRegion = new TextureRegion((Texture) game.assetManager.get(Assets.sun));
		man = game.assetManager.get(Assets.man);
		manBackground = game.assetManager.get(Assets.manBackground);

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



		game.spriteBatch.begin();
		drawEnvironment();
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
	}

    public void drawEnvironment() {
		processClouds();
		processCustomers();
		sunRotation += sunRotSpeed * Gdx.graphics.getDeltaTime();
		
		game.spriteBatch.draw(background, 0, 0, GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT);
		game.spriteBatch.draw(banyaTexture, GameManager.SCREEN_WIDTH / 2 - banyaWidth / 2, 20, banyaWidth, banyaHeight);
		game.spriteBatch.draw(sunRegion, GameManager.SCREEN_WIDTH - 200, GameManager.SCREEN_HEIGHT - 200,
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
			if (Gdx.input.isTouched()) {
				Vector2 touchPos = new Vector2(Gdx.input.getX(), GameManager.SCREEN_HEIGHT - Gdx.input.getY());

				// Gdx.app.log("Touch Position", touchPos.x + " " + touchPos.y);
				// Gdx.app.log("Customer Position", customer.x + " " + customer.y);

				if (customer.contains(touchPos)) {
					if (banya.takeCustomer(customer)) {
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
			customerSpawnFrequency = MathUtils.random(7, 20) * 1000000000f;
		}
	}
}
