package chatserver;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer extends JFrame{
    JPanel myPanel = new JPanel();
    JTextArea output = new JTextArea();
    
    public ChatServer(){
        super("Server");
        try {
            final String IP = Inet4Address.getLocalHost().getHostAddress();
            myPanel.setBorder(BorderFactory.createTitledBorder("Server IP address:  " + IP));
            output.setEditable(false);
            myPanel.add(output);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.getContentPane().add(myPanel);
            this.setBounds(200, 200, 400, 350);
            this.setResizable(false);
        } 
        catch (Exception ex) {
            System.out.println("Something went wrong: " + ex);
        }
        
    }
    
    public void setOutputText(){
        
    }
    
    public static void main(String[] args) {
        ChatServer serverApp = new ChatServer();
        serverApp.setVisible(true);
    }
    
}
