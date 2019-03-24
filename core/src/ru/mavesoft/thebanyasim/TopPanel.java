package ru.mavesoft.thebanyasim;

public class TopPanel {

    GameManager game;
    Banya banya;

    String banyaTitle;
    long banyaMoney;

    public TopPanel(GameManager gameManager, Banya banya) {
        this.game = gameManager;
        this.banya = banya;
    }
}
