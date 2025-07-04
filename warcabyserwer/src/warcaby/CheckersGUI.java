package warcaby;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CheckersGUI extends JPanel {
    BufferedImage board;
    BufferedImage czarnypionek, bialypionek, czarnadamka, bialadamka,forcedtake;
    BufferedImage dot;
    List<Move> possibleMoves;
    Field[][]f;
    Game game;
    boolean status;
    public CheckersGUI(Game game) {
        loadImages();
        f=game.m.fields;
        this.game=game;
        setPreferredSize(new Dimension(8, 8));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int tileSize = getWidth() / 8;
                int x = e.getX() / tileSize;
                int y = e.getY() / tileSize;

                game.handleThings(x,y);
                possibleMoves=game.possibleMoves;
                repaint();
            }
        });

    }

    private void loadImages() {
        try {
            board = ImageIO.read(new File("board.png"));
            czarnypionek = ImageIO.read(new File("czarnypionek.png"));
            bialypionek = ImageIO.read(new File("bialypionek.png"));
            czarnadamka = ImageIO.read(new File("czarnadamka.png"));
            bialadamka = ImageIO.read(new File("bialadamka.png"));
            dot = ImageIO.read(new File("reddot.png"));
            forcedtake = ImageIO.read(new File("forcedtake.png"));
        } catch (IOException e) {
            System.out.println("error loading images");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(board, 0, 0, null);
        int tileSize = board.getWidth() / 8;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (f[x][y].is_pawn_there) {
                    BufferedImage img = null;
                    char who = f[x][y].who;
                    if (who == 'b') img = czarnypionek;
                    else if (who == 'w') img = bialypionek;
                    else if (who == 'B') img = czarnadamka;
                    else if (who == 'W') img = bialadamka;
                    if (img != null) {
                        g.drawImage(img, y * tileSize, x * tileSize, tileSize, tileSize, null);
                    }
                }
            }
        }
        if (game.getselectedX() != -1 && game.getselectedY() != -1) {
            g.drawImage(dot, game.getselectedX() * tileSize, game.getselectedY() * tileSize, tileSize, tileSize, null);
            if(possibleMoves!=null){
                for (Move m : possibleMoves) {
                    g.drawImage(dot, m.x_to * tileSize, m.y_to * tileSize, tileSize, tileSize, null);
                }
            }

        }
        if(game.forcedToCapture.contains(new Point(game.getselectedX(), game.getselectedY()))) {
            for (Point p : game.forcedToCapture) {
                g.drawImage(forcedtake, p.x * tileSize, p.y * tileSize, tileSize, tileSize, null);
            }
        }

    }
}