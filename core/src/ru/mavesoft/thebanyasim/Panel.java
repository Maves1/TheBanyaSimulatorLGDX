package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.graphics.Texture;

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
            } else {
                game.spriteBatch.draw(panelElement.getElementTexture(), x + panelElement.getX(),
                        y + panelElement.getY(), panelElement.getWidth(), panelElement.getHeight());
            }
        }
    }

    public void addElement(String key, Texture texture, int x, int y, int width, int height) {
        panelElements.put(key, new PanelElement(texture, x, y, width, height));
    }

    public void addElement(String key, String text, int x, int y, int scale) {
        panelElements.put(key, new PanelElement(text, x, y, scale));
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

    public void setBackground(Texture bgTexture) {
        background = bgTexture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public class PanelElement {
        final static int TYPE_TEXT = 0;
        final static int TYPE_TEXTURE = 1;

        private int elementType;
        private Texture elementTexture;
        private String text;

        private int x;
        private int y;
        private int width;
        private int height;
        private int scale;

        public PanelElement(Texture texture, int x, int y, int width, int height) {
            elementType = PanelElement.TYPE_TEXTURE;
            this.elementTexture = texture;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public PanelElement(String text, int x, int y, int scale) {
            elementType = PanelElement.TYPE_TEXT;
            this.text = text;
            this.x = x;
            this.y = y;
            this.scale = scale;
        }

        public int getElementType() {
            return elementType;
        }

        public Texture getElementTexture() {
            return elementTexture;
        }

        public String getText() {
            return text;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getScale() {
            return scale;
        }
    }
}
