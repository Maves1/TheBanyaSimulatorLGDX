package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.graphics.Texture;

public class PanelIndicator extends PanelElement {

    final static int TYPE_INDICATOR = 2;

    public PanelIndicator(Texture texture, String value, int x, int y, int imageWidth, int imageHeight, int textScale) {
        elementType = PanelIndicator.TYPE_INDICATOR;
        this.elementTexture = texture;
        this.text = value;
        this.scale = textScale;
        this.x = x;
        this.y = y;
        this.width = imageWidth;
        this.height = imageHeight;
    }

    public void updateValue(String newValue) {
        this.text = newValue;
    }

}
