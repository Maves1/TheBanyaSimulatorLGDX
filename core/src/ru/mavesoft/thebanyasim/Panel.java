package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class Panel {

    private int width;
    private int height;
    private int x;
    private int y;

    GameManager game;
    private HashMap<Texture, Array<Integer>> panelTextures;
    private HashMap<String, Array<Integer>> panelTexts;

    public Panel(GameManager gameManager, int x, int y, int width, int height) {
        this.game = gameManager;
        panelTextures = new HashMap<Texture, Array<Integer>>();
        panelTexts = new HashMap<String, Array<Integer>>();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw() {
        for (HashMap.Entry<Texture, Array<Integer>> entry : panelTextures.entrySet()) {
            game.spriteBatch.draw(entry.getKey(), x + entry.getValue().get(0),
                    y + entry.getValue().get(1), entry.getValue().get(2),
                    entry.getValue().get(3));
        }
        for (HashMap.Entry<String, Array<Integer>> entry : panelTexts.entrySet()) {
            game.bitmapFont.getData().setScale(entry.getValue().get(2));
            game.bitmapFont.draw(game.spriteBatch, entry.getKey(), x + entry.getValue().get(0),
                    y + entry.getValue().get(1));
        }
    }

    public void addElement(Texture texture, int x, int y, int width, int height) {
        Array<Integer> array = new Array<Integer>();
        array.add(x);
        array.add(y);
        array.add(width);
        array.add(height);

        panelTextures.put(texture, array);
    }
    public void addElement(String text, int x, int y, int scale) {
        Array<Integer> array = new Array<Integer>();
        array.add(x);
        array.add(y);
        array.add(scale);

        panelTexts.put(text, array);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
