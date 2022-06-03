package Entities;

import UI.GamePanel;

import javax.swing.*;

public class Spaceship extends JLabel {

    ImageIcon imageIcon = new ImageIcon("C:\\Users\\Camilla\\IdeaProjects\\Asteroids\\src\\Images\\yellow_spaceship.jpg");

    public Spaceship() {
        this.setIcon(imageIcon);
    }

    public int getHeightSpaceship() {
        return imageIcon.getIconHeight();
    }

    public int getWidthSpaceship() {
        return imageIcon.getIconWidth();
    }

    public int getPositionGunSpaceship(int unitSizeBullet) {
        return (getWidthSpaceship() / 2) - (unitSizeBullet / 2);
    }
}
