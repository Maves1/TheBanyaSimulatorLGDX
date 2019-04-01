package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.graphics.Texture;

public class PanelElement {
    final static int TYPE_TEXT = 0;
    final static int TYPE_TEXTURE = 1;

    protected int elementType;
    protected Texture elementTexture;
    protected String text;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int scale;

    public PanelElement() {
        
    }
    
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
