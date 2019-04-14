package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Banya {

    GameManager game;

    // Banya properties
    private Texture banyaTexture;
    private String name;
    private int level;
    private long money;
    private int waterAmount;
    private int besomsAmount;

    // Banya Preferences for all levels
    private int[] banyaCapacities = {2, 3, 7, 10, 15, 25, 50};
    private int[] banyaWashDurations = {5, 5, 5, 5, 5, 5, 5};
    private int[] banyaProfits = {200, 200, 200, 200, 200, 200, 200};

    // Banya Preferences for current level
    private int banyaCapacity;
    private int banyaWashDuration;
    private int banyaProfit;

    Array<Customer> customersIn;

    public Banya(GameManager gameManager) {
        game = gameManager;
        this.name = game.gamePreferences.getString("banyaName");
        this.level = game.gamePreferences.getInteger("banyaLevel");
        this.money = game.gamePreferences.getLong("banyaMoney");
        this.waterAmount = game.gamePreferences.getInteger("banyaWaterAmount");
        this.besomsAmount = game.gamePreferences.getInteger("banyaBesomsAmount");
        banyaTexture = game.assetManager.get(Assets.banyas[level]);
        customersIn = new Array<Customer>();
        banyaCapacity = banyaCapacities[level];
        banyaWashDuration = banyaWashDurations[level];
        banyaProfit = banyaProfits[level];
    }

    public boolean takeCustomer(Customer newCustomer) {
        if (customersIn.size < banyaCapacity && waterAmount >= 20 && besomsAmount >= 1) {
            waterAmount -= 20;
            besomsAmount--;
            customersIn.add(newCustomer);
            newCustomer.startWashing();
            return true;
        }
        return false;
    }

    public void controlCustomers() {
        for (Iterator<Customer> iterator = customersIn.iterator(); iterator.hasNext(); ) {
            Customer currCustomer = iterator.next();
            if (TimeUtils.nanoTime() - currCustomer.getTimeOfEntering() >= banyaWashDuration * 1000000000L) {
                iterator.remove();
                money += banyaProfit;
            }
        }
    }

    public String getName() {
        return name;
    }

    public long getMoney() {
        return money;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public int getBesomsAmount() {
        return besomsAmount;
    }

    // This is govnocode, I'll replace it later
    public boolean buyBesoms() {
        if (money >= 200 && besomsAmount < 20) {
            money -= 200;
            besomsAmount++;
            return true;
        }
        return false;
    }
}
