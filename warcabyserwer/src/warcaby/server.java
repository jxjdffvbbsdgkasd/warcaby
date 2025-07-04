package warcaby;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import warcaby.*;


public class server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Czekam na klienta...");
            Socket client = serverSocket.accept();
            System.out.println("Połączono!");

            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            Game g = new Game();
            JFrame frame = new JFrame("Warcaby - serwer");
            CheckersGUI ui = new CheckersGUI(g);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(650,650);
            frame.add(ui);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            info clientres=null;
            while (!g.gameover) {
                while(!g.moveEnded){
                    Thread.sleep(10);//muj ruch
                }

                info i = new info(g.m.fields,g.myTime,g.status);
                out.writeObject(i);
                out.reset();
                out.flush();
                if(g.status==1){
                    System.out.printf("czarny wygrywa!");
                    break;
                }else if(g.status==2){
                    System.out.printf("bialy wygrywa!");
                    break;
                }
                clientres = (info) in.readObject();
                System.out.println("wyslano obiekt========================");
                System.out.printf("czas klienta: %.2f s\n",clientres.time);
                g.updateMap(clientres.fields);
                ui.repaint();
                if(clientres.status==1){
                    System.out.printf("czarny wygrywa!");
                    break;
                }else if(clientres.status==2){
                    System.out.printf("bialy wygrywa!");
                    break;
                }
                clientres=null;
            }
            client.close();
            serverSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

