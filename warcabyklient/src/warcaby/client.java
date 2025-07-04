package warcaby;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client{
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8888);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            Game g = new Game();
            JFrame frame = new JFrame("Warcaby - client");
            CheckersGUI ui = new CheckersGUI(g);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(650,650);
            frame.add(ui);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            Scanner scanner = new Scanner(System.in);
            info serverres= null;
            while (!g.gameover) {
                serverres = (info) in.readObject();
                System.out.printf("czas serwera: %.2f s",serverres.time);
                g.updateMap(serverres.fields);
                ui.repaint();
                if(serverres.status==1){
                    System.out.println("czarny wygrywa!");
                    break;
                }else if(serverres.status==2){
                    System.out.println("bialy wygrywa!");
                    break;
                }
                serverres = null;

                while(!g.moveEnded){
                    Thread.sleep(10);//muj ruch
                }
                info i = new info(g.m.fields,g.myTime,g.status);
                out.writeObject(i);
                out.reset();
                out.flush();

            }
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

