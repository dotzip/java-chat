package chatserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

class SocketThread implements Runnable{
    private Socket s = null;
    private Scanner in = null;
    private PrintWriter out = null;
    private String inMessage = null;
    private String outMessage = null;
    private boolean exit = true;
    private ArrayList<Socket> listSocket = null;
    private final ChatServer serverApp;
    
    SocketThread(Socket s, ChatServer serverApp) {
       this.s = s;
       this.serverApp = serverApp;
    }
    
    @Override
    public void run(){
        try{
            serverApp.setOutputText(" < + New client was connected >");
            ListSocket.addSocketToList(s);
            in = new Scanner(s.getInputStream());
            while (exit){
                inMessage = in.nextLine();
                listSocket = ListSocket.getListSocket();
                for(Socket socket : listSocket){
                    if(!socket.equals(s)){
                        out = new PrintWriter(socket.getOutputStream());
                        out.println(inMessage);
                        out.flush();
                    }
                }
                if(inMessage.contains("was disconnected")){
                    exit = false;
                }
            }
            ListSocket.removeSocketWithList(s);
            serverApp.setOutputText(" < - Client was disconnected >");
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    
    }
}
