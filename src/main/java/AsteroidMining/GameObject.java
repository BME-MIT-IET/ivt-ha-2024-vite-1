package AsteroidMining;

import java.awt.*;
import java.io.IOException;

public abstract class GameObject {

    protected int x,y, nexDestX, nextDestY, velX=0, velY=0;
    protected int width=0, height=0;

    private ID id;

    public GameObject(int x, int y, ID id){
        this.x = x;
        this.y = y;
        this.id = id;
    }
    public GameObject(ID id){this.id = id;}

    public abstract void tick();

    Rectangle getBounds(){
        return new Rectangle(x,y, width, height);
    }

    public abstract void render(Graphics g) throws IOException;

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    public void setVelX(int vx){
        this.velX = vx;
    }
    public int getVelX(int vx){
        return this.velX;
    }

    public void setVelY(int vy){
        this.velY = vy;
    }
    public int getVelY(int vx){
        return this.velY;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public ID getId(){
        return this.id;
    }
}
