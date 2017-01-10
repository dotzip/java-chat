package chatserver;

import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import javax.swing.text.DefaultCaret;

public class ChatServer extends JFrame{
    JPanel myPanel = new JPanel();
    JTextArea output = new JTextArea();
    JScrollPane scroll = new JScrollPane(output);
    private DefaultCaret caret = (DefaultCaret)output.getCaret();

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
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.getContentPane().add(myPanel);
            this.setBounds(200, 200, 400, 350);
            this.setResizable(false);
            output.setEditable(false);
            
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
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

