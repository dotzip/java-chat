package chatserver;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer extends JFrame{
    JPanel myPanel = new JPanel();
    JTextArea output = new JTextArea();
    JScrollPane scroll = new JScrollPane(output);

    public ChatServer(){
        super("Server");
        try {
           URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader readip = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            final String IP = readip.readLine();
            myPanel.setBorder(BorderFactory.createTitledBorder("Server IP address:  " + IP));
            myPanel.setLayout(new GridLayout(1, 2));
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            myPanel.add(scroll);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.getContentPane().add(myPanel);
            this.setBounds(200, 200, 400, 350);
            this.setResizable(false);
            output.setEditable(false);
            
        } 
        catch (Exception ex) {
            System.out.println("Something went wrong: " + ex);
        }
        
    }
    
    public void setOutputText(String outMessage){
        output.append(outMessage + "\n");
    }
    
    public static void main(String[] args) throws IOException {
        ChatServer serverApp = new ChatServer();
        serverApp.setVisible(true);
        
        ServerSocket ss = new ServerSocket(22222);
        serverApp.setOutputText(" < Server is running !>");        
        while(true){
            Socket s = ss.accept();
            SocketThread socketThread = new SocketThread(s, serverApp);
            Thread t = new Thread(socketThread);
            t.start();
        }
    }
    
}
