package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PanelElement {
    final static int TYPE_TEXT = 0;
    final static int TYPE_TEXTURE = 1;
    final static int TYPE_IMAGE = 2;

    protected int elementType;
    protected Texture elementTexture;
    protected String text;

    protected Image elementImage;

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

    public PanelElement(Image image, int x, int y, int width, int height) {
        elementType = PanelElement.TYPE_IMAGE;
        elementImage = image;
        // elementImage.setBounds(x, y, width, height);
        elementImage.setWidth(width);
        elementImage.setHeight(height);
        elementImage.setX(x);
        elementImage.setY(y);
        this.x = x;
        this.y = y;
    }
    public void updateValue(String newValue) {
        this.text = newValue;
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

    public Image getImage() {
        return elementImage;
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
