package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Banya {

    GameManager game;

    // Banya properties
    private Texture banyaTexture;
    private String name;
    private int level;

    // Banya Preferences for all levels
    private int[] banyaCapacities = {2, 3, 7, 10, 15, 25, 50};

    // Banya Preferences for current level
    private int banyaCapacity;

    Array<Customer> customersIn;

    public Banya(GameManager gameManager) {
        game = gameManager;
        this.name = game.gamePreferences.getString("banyaName");
        this.level = game.gamePreferences.getInteger("banyaLevel");
        banyaTexture = game.assetManager.get(Assets.banyas[level]);
        customersIn = new Array<Customer>();
        banyaCapacity = banyaCapacities[level];
    }

    public boolean takeCustomer(Customer newCustomer) {
        if (customersIn.size < banyaCapacity) {
            customersIn.add(newCustomer);
            newCustomer.startWashing();
            return true;
        }
        return false;
    }

    public void controlCustomers() {
        for (Customer customer : customersIn) {

        }
    }

    public String getName() {
        return name;
    }
}
