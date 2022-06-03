package Entities;

public class Bullet {

    static int speed = 3;
    int x;
    int y;

    public Bullet(int xSpaceship, int ySpaceship){
        x = xSpaceship;
        y = ySpaceship;
    }

    public boolean moveOnePosition(){
        if (y > 0){
            y = y - speed;
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
