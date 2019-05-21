package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.HashMap;

public class Panel {

    private int width;
    private int height;
    private int x;
    private int y;

    GameManager game;
    private Texture background;
    private HashMap<String, PanelElement> panelElements;

    public Panel(GameManager gameManager, int x, int y, int width, int height) {
        this.game = gameManager;
        panelElements = new HashMap<String, PanelElement>();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw() {
        if (background != null) {
            game.spriteBatch.draw(background, x, y, width, height);
        }
        for (HashMap.Entry<String, PanelElement> entry : panelElements.entrySet()) {
            PanelElement panelElement = entry.getValue();
            if (entry.getValue().getElementType() == PanelElement.TYPE_TEXT) {
                game.bitmapFont.getData().setScale(panelElement.getScale());
                game.bitmapFont.draw(game.spriteBatch, panelElement.getText(), x + panelElement.getX(),
                        y + panelElement.getY() + game.bitmapFont.getData().xHeight * 2);
            } else if (entry.getValue().getElementType() == PanelElement.TYPE_TEXTURE) {
                game.spriteBatch.draw(panelElement.getElementTexture(), x + panelElement.getX(),
                        y + panelElement.getY(), panelElement.getWidth(), panelElement.getHeight());
            } else if (entry.getValue().getElementType() == PanelIndicator.TYPE_INDICATOR) {
                game.spriteBatch.draw(panelElement.getElementTexture(), x + panelElement.getX(),
                        y + panelElement.getY(), panelElement.getWidth(), panelElement.getHeight());
                game.bitmapFont.getData().setScale(panelElement.getScale());
                game.bitmapFont.draw(game.spriteBatch, panelElement.getText(),
                        x + panelElement.getX() + panelElement.getWidth() + 5,
                        y + panelElement.getY() + game.bitmapFont.getData().xHeight * 2);
            } else if (entry.getValue().getElementType() == PanelElement.TYPE_IMAGE) {
                entry.getValue().getImage().draw(game.spriteBatch, 1f);
            }
        }
    }

    public void addElement(String key, Texture texture, int x, int y, int width, int height) {
        panelElements.put(key, new PanelElement(texture, x, y, width, height));
    }

    public void addElement(String key, String text, int x, int y, int scale) {
        panelElements.put(key, new PanelElement(text, x, y, scale));
    }

    public void addElement(String key, PanelElement element) {
        if (element.getElementType() == PanelElement.TYPE_IMAGE) {
            element.x += this.x;
            element.y += this.y;
            element.getImage().setX(element.x);
            element.getImage().setY(element.y);
        }
        panelElements.put(key, element);
    }

    public void setElement(String key, Texture texture, int x, int y, int width, int height) {
        if (panelElements.containsKey(key)) {
            panelElements.remove(key);
            panelElements.put(key, new PanelElement(texture, x, y, width, height));
        }
    }

    public void setElement(String key, String text, int x, int y, int scale) {
        if (panelElements.containsKey(key)) {
            panelElements.remove(key);
            panelElements.put(key, new PanelElement(text, x, y, scale));
        }
    }

    public void setElement(String key, PanelElement panelElement) {
        if (panelElements.containsKey(key)) {
            panelElements.remove(key);
            panelElements.put(key, panelElement);
        }
    }

    public void setBackground(Texture bgTexture) {
        background = bgTexture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
