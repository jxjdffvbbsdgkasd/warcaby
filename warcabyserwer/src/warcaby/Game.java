package warcaby;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import warcaby.*;

public class Game {
    public int status;//0 trwa 2 bialy wygral 1 czarny wygral
    public boolean gameover;
    public Map m;
    public List<Move> possibleMoves;
    int selectedX;
    int selectedY;
    List<Point> forcedToCapture;
    char current_player;
    boolean sameplayer=false;
    public double myTime=0;
    boolean timerStarted=false;
    double timeStart;
    char myColor;

    Move lastMove;
    public boolean moveEnded=false;
    public Game(){
        m = new Map();
        m.initMap();
        forcedToCapture = new ArrayList<>();
        current_player = 'w';
        myColor = 'w';
        gameover = false;
        status = 0;
        selectedX = -1;
        selectedY = -1;
    }
    public boolean handleThings(int x, int y) {////    DZIALA NIE RUSZAC !!!!!!!!!!!!!!!!!!!!!!!!
        if(gameover) return false;
        if((status==1 || status==2) && !gameover){
            System.out.println(status== 1 ? "black win" : "white win");
            gameover=true;
            selectedX = -1;
            selectedY = -1;
            possibleMoves = null;
            return false;
        }
        if(!timerStarted){
            timerStarted=true;
            timeStart = System.currentTimeMillis();

        }
        if(myColor!= current_player){
            System.out.println("teraz kolej klienta!");
            return false;
        }
        moveEnded=false;
        if(!m.fields[y][x].is_pawn_there && selectedX == -1 && selectedY == -1) return false;
        possibleMoves = utils.findPossibleMoves(x, y, m.fields);
        possibleMoves = utils.filterMoves(possibleMoves);
        System.out.println("-----------------------------------------");
        if(current_player!= Character.toLowerCase(m.fields[y][x].who) && selectedX == -1 && selectedY == -1){
            System.out.println("nie twoje pionki");
            return false;
        }
        hasForcedCaptures(current_player);
        if (selectedX == -1 && selectedY == -1) {
            if (m.fields[y][x].is_pawn_there) {
                if(!forcedToCapture.isEmpty()){
                    if(!forcedToCapture.contains(new Point(x, y))){
                        System.out.println("zly pionek - musisz bic!!!!!!!!!!!");
                        return false;
                    }
                }
                selectedX = x;
                selectedY = y;
                System.out.println("Wybrano pionek na: " + x + ", " + y);
                printMoves(possibleMoves);
            }
        } else {
            possibleMoves = utils.findPossibleMoves(selectedX, selectedY, m.fields);
            possibleMoves = utils.filterMoves(possibleMoves);
            Move move = findMove(possibleMoves, selectedX, selectedY, x, y);
            if (move != null) {
                if (move.take) {
                    utils.takeMove(move, m.fields);
                    List<Move> followUp = utils.findPossibleMoves(move.x_to, move.y_to, m.fields);
                    followUp = utils.filterOnlyTakes(followUp);
                    if(!followUp.isEmpty()){
                        sameplayer=true;
                    }else{
                        sameplayer=false;
                    }
                }else{
                    utils.normalMove(move, m.fields);
                }
                lastMove = move;
                if(!sameplayer){
                    changePlayer();//tutaj koniec liczenia czasu bo zmiana gracza
                    myTime = System.currentTimeMillis() - timeStart;
                    myTime/=1000;
                    System.out.printf("czas ruchu = %.2f s\n",myTime);
                    moveEnded=true;
                }
            } else {
                System.out.println("wrong move!!!!!!!!!!!!!!");
            }
            selectedX = -1;
            selectedY = -1;
            m.printMap();
        }
        status = checkWin();
        return true;
    }
    public Move findMove(List<Move> m, int xf, int yf,int xt, int yt) {
        for (Move move : m) {
            if(move.x_from == xf && move.y_from == yf && move.x_to == xt && move.y_to == yt) return move;
        }
        return null;
    }
    public void printMoves(List<Move> moves) {
        if(moves==null) return;
        for (Move move : moves) {
            System.out.printf("x %d, y %d -> x %d, y %d %b\n", move.x_from, move.y_from, move.x_to, move.y_to, move.take);
        }
    }
    public int getselectedX() {
        return selectedX;
    }
    public int getselectedY() {
        return selectedY;
    }
    public void hasForcedCaptures(char playerColor) {
        forcedToCapture.clear(); // wyczyść poprzednie
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Field field = m.fields[y][x];
                if (field.is_pawn_there && Character.toLowerCase(field.who) == playerColor) {
                    List<Move> moves = utils.findPossibleMoves(x, y, m.fields);
                    if (moves != null) {
                        for (Move move : moves) {
                            if (move.take) {
                                forcedToCapture.add(new Point(x, y));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    public void changePlayer() {
        current_player = current_player =='w' ? 'b' : 'w';
    }
    public int checkWin() { // 1 czarny 2 bialy 0 nikt
        int countWhite = 0;
        int countBlack = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (m.fields[i][j].who == 'w' || m.fields[i][j].who == 'W') {
                    countWhite++;
                } else if (m.fields[i][j].who == 'b' || m.fields[i][j].who == 'B') {
                    countBlack++;
                }
            }
        }
        if (countWhite == 0) {
            return 1;
        }else if (countBlack == 0) {
            return 2;
        }else{
            return 0;
        }
    }
    public void updateMap(Field[][] f){
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                m.fields[y][x].who = f[y][x].who;
                m.fields[y][x].is_pawn_there = f[y][x].is_pawn_there;
            }
        }
        moveEnded = false;
        changePlayer();
        timeStart=System.currentTimeMillis();
    }
}