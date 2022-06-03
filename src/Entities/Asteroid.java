package Entities;

import java.util.Random;

public class Asteroid {

    static int speed = 1;
    int x;
    int y;
    Random random = new Random();

    public Asteroid(int screenWidth, int unitSize) {
        x = random.nextInt((screenWidth / unitSize)) * unitSize;
        y = 0;
    }

    public boolean moveOnePosition(int screenHeight){
        if (y < screenHeight){
            y = y + speed;
            return true;
        } else {
            return false;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
