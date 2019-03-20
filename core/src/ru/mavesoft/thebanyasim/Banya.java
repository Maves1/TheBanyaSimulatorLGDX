package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Banya {

    Preferences gamePreferences = Gdx.app.getPreferences("gamePreferences");

    // Banya properties
    private Texture banyaTexture;
    private String name;
    private int level;

    public void restoreBanya() {
        name = gamePreferences.getString("banyaName");
        level = gamePreferences.getInteger("banyaLevel");
        banyaTexture = new Texture("banya" + Integer.toString(level) + ".png");
    }

    public void Banya(String name, AssetManager assetManager) {
        this.name = name;
        level = 0;
        banyaTexture = assetManager.get("banyas/banya0.png");
    }

}
