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
    private int woodAmount;

    // Banya Preferences for all levels
    private int[] banyaCapacities = {2, 3, 4, 5, 6, 7, 8};
    private int[] banyaWashDurations = {5, 5, 5, 5, 5, 5, 5};
    private int[] banyaProfits = {400, 450, 400, 400, 400, 400, 400};

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
        this.woodAmount = game.gamePreferences.getInteger("banyaWoodAmount");
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

    public void controlCustomers(PanelElement banyaCapacity) {
        for (Iterator<Customer> iterator = customersIn.iterator(); iterator.hasNext(); ) {
            Customer currCustomer = iterator.next();
            if (TimeUtils.nanoTime() - currCustomer.getTimeOfEntering() >= banyaWashDuration * 1000000000L) {
                iterator.remove();
                money += banyaProfit;
                banyaCapacity.updateValue(getCurrentCustomers() + "/" + getBanyaCapacity());
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

    public int getWoodAmount() {
        return woodAmount;
    }

    public int getBanyaCapacity() {
        return banyaCapacity;
    }

    public int getCurrentCustomers() {
        return customersIn.size;
    }

    public int getLevel() {
        return level;
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

    public boolean buyWater() {
        if (money >= 100 && waterAmount < 200) {
            money -= 100;
            waterAmount += 20;
            return true;
        }
        return false;
    }

    public boolean buyWood() {
        if (money >= 200 && woodAmount < 50) {
            money -= 200;
            woodAmount += 2;
            return true;
        }
        return false;
    }
}
