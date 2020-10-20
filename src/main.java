import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayDeque;
import java.lang.*;
import java.util.Random;
import java.util.concurrent.Callable;
import java.awt.Point;




class Main extends JPanel {                                                        //main hérite de JPanel

    private final int WIDTH = 50;                                                  //taill 1 carrer
    private Deque<SnakePart> snake = new ArrayDeque<>();                           // bouger le snake (pile)
    private int offset = 0;
    private int newDirection = 39;
    private Point apple = new Point(0,0);
    private Random rand = new Random();
    private boolean isGrowing = false;
    private boolean gameLost = false;






    public static void main(String[] args) {


        JFrame frame = new JFrame("Snake");                                  // creation d une fenetre
        Main panel = new Main();                                                 //panel = objet
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                panel.onkeyPressed(e.getKeyCode());
            }
        }
        );
        frame.setContentPane(panel);                                             //introduction du &er carrer
        frame.setSize(13*50,13*50);                                  //taille du fenetre
        frame.setResizable(false);                                               //taille de fentre fixer
        frame.setLocationRelativeTo(null);                                      //fenetre centrer sur l ecran
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                   //si fernetre close = prog close
        frame.setVisible(true);                                                 //visibilite
    }


    public Main() {
        creatApple();
        snake.add(new SnakePart(0, 0, 39));                          // 39 : touche droite du clavier
        setBackground(Color.white);
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    repaint();
                    try {
                        Thread.sleep(10000 / 1000);                                 //60 FPS
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /*public void bot(){                                  //implementer une class bot qui tue le snake a la tete.
                                                        // implementer une autre class pour creer : bot.
    bot.x = rand.nextInt(12);
    bot.y = rand.nextInt(12);
     for (snakePart p : snake){
          if (p.x == bot.x && p.y == bot.y){
        gameLost();
         }
        }

    }
    */
    public void creatApple(){
        boolean positionAviable;
        do {

            apple.x = rand.nextInt(12);
            apple.y = rand.nextInt(12);
            positionAviable = true;
            for (SnakePart p : snake) {
                if (p.x == apple.x && p.y == apple.y) {
                    positionAviable = false;
                    break;
                }
            }
        }while (!positionAviable);
    }

    @Override
    protected void paintComponent(Graphics g) {                                                // creation petit carrer
        super.paintComponent(g);

        if (gameLost){
            g.setColor(Color.cyan);
            g.setFont(new Font("Arial",90,90));
            g.drawString("t'es trop nul <3",13*50/2 - g.getFontMetrics().stringWidth("t'es trop nul <3")/2,13*50/2);
            return;
        }
        offset+=10;                                                                             // vitess snake /50
        SnakePart head = null;
        if (offset == WIDTH){
            offset =0;
            try {
                head = (SnakePart) snake.getFirst().clone();
                head.move();
                head.direction = newDirection;
                snake.addFirst(head);
                if (head.x == apple.x && head.y == apple.y){
                    isGrowing = true;
                    creatApple();
                }

                if (!isGrowing)
                    snake.pollLast();
                else{
                    isGrowing = false;
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        g.setColor(Color.RED);
        g.fillOval(apple.x * WIDTH + WIDTH/4,apple.y*WIDTH + WIDTH/4,WIDTH/2,WIDTH/2);

        g.setColor(Color.darkGray);

        for(SnakePart p : snake){
            if (offset == 0){
                if (p != head){
                    if (p.x == head.x && p.y == head.y){
                    gameLost = true;
                    }
                }
            }
            if (p.direction == 37 || p.direction == 39){
                g.fillRect(p.x * WIDTH + ((p.direction == 37) ? - offset : offset),p.y * WIDTH, WIDTH, WIDTH); // dep horizon
            }else {
                g.fillRect(p.x * WIDTH ,p.y*WIDTH + ((p.direction == 38) ? - offset : offset), WIDTH, WIDTH);
            }
        }

        g.setColor(Color.blue);                                                             //affichage du score
        g.drawString("Score :" + (snake.size() - 1 ),10,20 );
    }

    public void onkeyPressed(int keycode){
        if (keycode >= 37 && keycode <= 40){
            if (Math.abs(keycode - newDirection) != 2){
                newDirection = keycode;
            }
        }
    }

    class SnakePart{
        public int x, y, direction;                                                               //attribus du snake

        public SnakePart(int x, int y, int direction){                                            // constructeur
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        public void move(){
            if (direction == 37 || direction == 39){
                x += (direction == 37) ? - 1 : 1;
                if (x > 13)
                    x = -1;
                else if (x < -1)
                    x = 13;

            }else{
                y+= (direction == 38) ? 1 : -1;
                if (y > 13)
                    y = -1;
                else if (y < -1)
                    y = 13;
            }
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return new SnakePart( x, y, direction);
        }
    }

}

