package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;

public class Banya {

    Preferences gamePreferences = Gdx.app.getPreferences("gamePreferences");

    // Banya properties
    private String name;
    private Texture banyaTexture;
    private int level;

    public void createBanya() {
        name = gamePreferences.getString("banyaName");
        level = gamePreferences.getInteger("banyaLevel");
        banyaTexture = new Texture("banya" + Integer.toString(level) + ".png");
    }


}
