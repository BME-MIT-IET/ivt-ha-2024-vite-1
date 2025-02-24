package AsteroidMining;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import AsteroidMining.*;


public class Asteroid extends Place {

    private boolean hollow=false;
    protected int depth;
    protected Resource resource=null;
    private BufferedImage img = null;
    private boolean _isPerihelion;
    private int rad;
    public int coreX, coreY;


    public Asteroid(int x, int y, Resource r, int depth) {
        super(x, y, ID.Asteroid);

        if(r!=null) {
            addResource(r);
        }
        else {
            this.hollow = true;

        }

        this.depth = depth;
        this.rad = depth;

        width =rad*12;
        height = rad*12;
        coreX = getX()+width/2-5;
        coreY = getY()+height/2;



        try{
                img = ImageIO.read(new File("Assets/asteroid1.png"));
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

        if(depth==rad) {
            try{
                    img = ImageIO.read(new File("Assets/Asteroid1.png"));
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }else if(depth >= rad*0.75){
            try{
                    img = ImageIO.read(new File("Assets/drilled1.png"));
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }

        }
        else if(depth >= rad*0.5){
        try{
            img = ImageIO.read(new File("Assets/drilled2.png"));
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        }
        else if(depth==0){
        try{
            img = ImageIO.read(new File("Assets/drilled3.png"));
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
        g.drawImage(img, x, y, width, height, null);
        if(resource!=null && depth<=0){
            g.drawImage(resource.getImg(), coreX, coreY, 30,30, null);
        }

    }



    public void deepenHole(int n) {
        this.depth -= n;
    }

    public Resource getResource(){
        return this.resource;
    }

    public void addResource(Resource r){
        if(resource instanceof Uranium)
            this.setId(ID.RadioActiveAsteroid);
        this.resource = r;
        this.hollow = false;
    }
    public void removeResource(){
        this.resource = null;
        hollow= true;
    }

    public boolean isHollow(){
        return hollow;
    }
    public int getDepth(){
        return this.depth;
    }

    public boolean isPerihelion(){
        return _isPerihelion;
    }
    public boolean isFullyDrilled(){
        if(depth<=0){
            return true;
        }
        return false;
    }
    public void inPerihelion(boolean exploded){
        this._isPerihelion = true;
        if(this.isFullyDrilled()) {
            if (this.getId() == ID.RadioActiveAsteroid) {
                explode(exploded);
            }
            if (this.resource.getId() == ID.WaterIce) {
                WaterIce wIce = (WaterIce) resource;
                wIce.sublime(this);
            }
        }
    }

    public void explode(boolean ex) {
        for (Visitor visitor : visitors) {
            if (visitor.getId().equals(ID.Settler)) {
                visitor.die();
            }
            if (visitor.getId().equals(ID.Robot)) {
                Asteroid a2 = (Asteroid) this.getNeighbour();
                a2.addVisitor(visitor);
                this.removeVisitor(visitor);
            }

        }
    }

}


