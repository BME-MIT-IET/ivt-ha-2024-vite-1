package AsteroidMining;
import AsteroidMining.Resources.STATE;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;

public class Game extends Canvas implements Runnable{

    private Thread thread;
    public static boolean running = true;

    public static int WIDTH = 1000;
    public static int HEIGHT = WIDTH / 12 * 9;
    public Handler handler;
    BufferedImage backImg = null;
    Settler settler;
    public HashMap<Resource, Integer> nResources;
    public SunStorm sunStorm;
    public static STATE gameState = STATE.Menu;
    public boolean paused = false;
    public Menu menu;

    public Game(){

        menu = new Menu(this);

        handler = new Handler();
        nResources = new HashMap<Resource, Integer>();
        nResources.put(new Carbon(), 1);
        nResources.put(new Iron(), 1);
        nResources.put(new Uranium(), 1);
        nResources.put(new WaterIce(), 1);

        Settler settler = new Settler(300, 400, handler);
        Asteroid a1 = new Asteroid(100, 500, null, 10);
        //settler.setPlace(a1);
        //a1.addVisitor(settler);

        handler.addObject(new Asteroid(100, 150, new Carbon(), 10));
        handler.addObject(new Asteroid(400, 220, new Iron(), 10));
        handler.addObject(new Asteroid(700, 250, new WaterIce(), 10));
        handler.addObject(new Asteroid(600, 500, new Uranium(), 10));
        handler.addObject(a1);
        handler.addObject(settler);

        this.addMouseListener(menu);
        this.addKeyListener(new KeyHandler(handler, this));

        new Window(WIDTH, HEIGHT, "Asteroid Mining", this);

        this.requestFocusInWindow();

    }
    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000/ amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running) {
            long now = System.nanoTime();
            delta += (now- lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                tick();
                delta--;

            }
            if(running) {
                try {
                    render();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                //System.out.println("FPS:" + frames);
                frames = 0;
            }
        }
        stop();
    }
    /*Ending the game, exittting*/
    public void endGame(){
        System.exit(1);
    }

    /*Creating sunstorm for fixed time*/
    public void createSunStorm(int x, int y, int time){
        sunStorm = new SunStorm(x,y, time);
        for(GameObject obj : handler.objects){
            if(obj instanceof Place){
                Asteroid a1 = (Asteroid) obj;
                sunStorm.collisionWith(a1);
            }
        }
    }
    public void determinePerihelion() {
        if(handler!=null){
            handler.checkAsteroids();
        }

    }

    public void tick() {
        handler.tick();
    }

    public void render() throws IOException {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs==null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        BufferedImage op=null;
        if(gameState==STATE.Game) {
            try {
                backImg = ImageIO.read(new File("Assets/space.png"));
                op = ImageIO.read(new File("Assets/operations.png"));
                g.drawImage(backImg, 0, 0, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.render(g);
            g.drawImage(op, 670, 550, 400, 150, null);
        }
        else if(gameState==STATE.Menu){
            menu.render(g);
        }

        g.dispose();
        bs.show();
    }

    public static void main(String args[]){
        Game game = new Game();
    }
}
