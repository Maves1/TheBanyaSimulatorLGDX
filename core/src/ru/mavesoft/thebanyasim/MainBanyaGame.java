package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MainBanyaGame extends ApplicationAdapter {
    // Service variables
    Preferences gamePreferences;
	int SCREEN_HEIGHT;
	int SCREEN_WIDTH;
	boolean firstLaunch;

	SpriteBatch batch;
	Texture background;
	Texture banya;
	Texture sun;
	TextureRegion sunRegion;

	int banyaWidth = 300;
	int banyaHeight = 400;

	int sunWidth = 200;
	int sunHeight = 200;
	float sunRotation = 0.0f;
	float sunRotSpeed = 5.0f;

	Array<Cloud> arrayClouds;
	float timeLastCloudSpawned;
	float cloudSpawnFrequency = MathUtils.random(9000000000f, 15000000000f);
	short windDirection = (short) MathUtils.random(1);
	
	@Override
	public void create () {
		// All needed variables (such as screen height) are initialized here
        gamePreferences = Gdx.app.getPreferences("GamePreferences");
		SCREEN_HEIGHT = Gdx.graphics.getHeight();
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		firstLaunch = gamePreferences.getBoolean("firstLaunch", true);

		// Textures
		batch = new SpriteBatch();
		background = new Texture("background0.png");
		banya = new Texture("banya1.png");
		sunRegion = new TextureRegion(new Texture("sun.png"));

        arrayClouds = new Array<Cloud>();
        arrayClouds.add(new Cloud(windDirection, SCREEN_HEIGHT, SCREEN_WIDTH));
        timeLastCloudSpawned = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sunRotation += sunRotSpeed * Gdx.graphics.getDeltaTime();

		if ((TimeUtils.nanoTime() - timeLastCloudSpawned >= cloudSpawnFrequency)
                && (arrayClouds.size < 5)) {
		    arrayClouds.add(new Cloud(windDirection, SCREEN_HEIGHT, SCREEN_WIDTH));
            timeLastCloudSpawned = TimeUtils.nanoTime();
            cloudSpawnFrequency = MathUtils.random(9000000000f, 15000000000f);
        }

		batch.begin();
		batch.draw(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batch.draw(banya, SCREEN_WIDTH / 2 - banyaWidth / 2, 20, banyaWidth, banyaHeight);
		batch.draw(sunRegion, SCREEN_WIDTH - 200, SCREEN_HEIGHT - 200, sunWidth / 2, sunHeight / 2, sunWidth,  sunHeight, 1.0f, 1.0f, sunRotation);
		drawClouds();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void drawClouds() {
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
            batch.draw(cloud.getTexture(), cloud.x, cloud.y, cloud.getWidth(), cloud.getHeight());
        }
    }
}
