package ru.mavesoft.thebanyasim.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.mavesoft.thebanyasim.GameManager;
import ru.mavesoft.thebanyasim.MainBanyaGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GameManager(), config);
	}
}
