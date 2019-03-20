package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MainBanyaGame implements Screen {
    // Service variables
	GameManager game;
	int SCREEN_HEIGHT;
	int SCREEN_WIDTH;
	boolean firstLaunch;

	Texture background;
	Texture banya;
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
	float cloudSpawnFrequency = MathUtils.random(9, 15) * 1000000000f;
	short windDirection = (short) MathUtils.random(1);

	Array<Customer> arrayCustomers;
	float timeLastCustomerSpawned;
	float customerSpawnFrequency = MathUtils.random(20, 40) * 1000000000f;

	public MainBanyaGame(final GameManager gameManager) {
		// All needed variables (such as screen height) are initialized here
		this.game = gameManager;
		SCREEN_HEIGHT = Gdx.graphics.getHeight();
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		firstLaunch = game.gamePreferences.getBoolean("firstLaunch", true);

		// Textures
		background = game.assetManager.get("backgrounds/background0.png");
		banya = game.assetManager.get("banyas/banya1.png");
		sunRegion = new TextureRegion((Texture) game.assetManager.get("sun.png"));
		man = game.assetManager.get("man.png");
		manBackground = game.assetManager.get("manBackground.png");

        arrayClouds = new Array<Cloud>();
        arrayClouds.add(new Cloud(windDirection, SCREEN_HEIGHT, SCREEN_WIDTH, game.assetManager));
        timeLastCloudSpawned = TimeUtils.nanoTime();

        arrayCustomers = new Array<Customer>();
        arrayCustomers.add(new Customer(SCREEN_WIDTH, SCREEN_HEIGHT, game.assetManager));
        timeLastCustomerSpawned = TimeUtils.nanoTime();
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sunRotation += sunRotSpeed * Gdx.graphics.getDeltaTime();

		if ((TimeUtils.nanoTime() - timeLastCloudSpawned >= cloudSpawnFrequency)
                && (arrayClouds.size < 5)) {
		    arrayClouds.add(new Cloud(windDirection, SCREEN_HEIGHT, SCREEN_WIDTH, game.assetManager));
            timeLastCloudSpawned = TimeUtils.nanoTime();
            cloudSpawnFrequency = MathUtils.random(9, 15) * 1000000000f;
        }

		if ((TimeUtils.nanoTime() - timeLastCustomerSpawned >= customerSpawnFrequency)
				&& (arrayCustomers.size < 5)) {
			arrayCustomers.add(new Customer(SCREEN_HEIGHT, SCREEN_WIDTH, game.assetManager));
			timeLastCustomerSpawned = TimeUtils.nanoTime();
			customerSpawnFrequency = MathUtils.random(9, 15) * 1000000000f;
		}

		game.spriteBatch.begin();
		drawEnvironment();
        if (Gdx.input.isTouched()) {

        }
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
		banya.dispose();
		sun.dispose();
		man.dispose();
		manBackground.dispose();
	}

    public void drawEnvironment() {
		game.spriteBatch.draw(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		game.spriteBatch.draw(banya, SCREEN_WIDTH / 2 - banyaWidth / 2, 20, banyaWidth, banyaHeight);
		game.spriteBatch.draw(sunRegion, SCREEN_WIDTH - 200, SCREEN_HEIGHT - 200,
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
				if (currCloud.x > SCREEN_WIDTH) {
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
			} else if (customer.x > SCREEN_WIDTH && customer.direction == 1) {
		    	iterator.remove();
			}
			if (Gdx.input.isTouched()) {
				float touchX = Gdx.input.getX();
				float touchY = Gdx.input.getY();

				// Gdx.app.log("Touch Position", touchX + " " + touchY);

				if (touchX > customer.x && touchX < customer.x + customer.getWidth() &&
						touchY < SCREEN_HEIGHT - customer.y && touchY > SCREEN_HEIGHT - (customer.y + customer.getHeight())) {
					iterator.remove();
				}
			}
        }

        for (Customer customer : arrayCustomers) {
        	game.spriteBatch.draw(customer.getTexture(), customer.x, customer.y,
					customer.getWidth(), customer.getHeight());
		}
	}
}
