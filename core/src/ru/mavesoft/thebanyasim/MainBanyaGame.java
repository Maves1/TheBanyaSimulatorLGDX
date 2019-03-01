package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	Texture cloud;
	TextureRegion sunRegion;

	int banyaWidth = 300;
	int banyaHeight = 400;

	int sunWidth = 200;
	int sunHeight = 200;
	float sunRotation = 0.0f;
	float sunRotSpeed = 0.1f;

	int cloudWidth = 200;
	int cloudHeight = 150;
	float cloudXPos = 0 - cloudWidth;
	float cloudSpeed = 0.2f;
	
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
		cloud = new Texture("cloud1.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sunRotation += sunRotSpeed;
		cloudXPos += cloudSpeed;

		batch.begin();
		batch.draw(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batch.draw(banya, SCREEN_WIDTH / 2 - banyaWidth / 2, 20, banyaWidth, banyaHeight);
		batch.draw(sunRegion, SCREEN_WIDTH - 200, SCREEN_HEIGHT - 200, sunWidth / 2, sunHeight / 2, sunWidth,  sunHeight, 1.0f, 1.0f, sunRotation);
		batch.draw(cloud, cloudXPos, SCREEN_HEIGHT - 250, cloudWidth, cloudHeight);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
