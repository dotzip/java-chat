package chatserver;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.text.DefaultCaret;

public class ChatServer extends JFrame{
    JPanel myPanel = new JPanel();
    JTextArea output = new JTextArea();
    JScrollPane scroll = new JScrollPane(output);
    private DefaultCaret caret = (DefaultCaret)output.getCaret();
    private ArrayList<Socket> listSocket = null;
    private PrintWriter out = null;
    private Socket s = null;
    
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
            output.setFont(new Font("Calibri", Font.PLAIN, 15));
            
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    listSocket = ListSocket.getListSocket();
                    for(Socket socket : listSocket){
                        if(!socket.equals(s)){
                            out = new PrintWriter(socket.getOutputStream());
                            out.println("=== Server was shutdown ===");
                            out.flush();
                        }
                    }
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }
                
            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });    
    }
    
    public void setOutputText(String outMessage){
        output.append(outMessage + "\n");
    }
    
     public static boolean isWindows(){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("win"));
    }
    
    public static boolean isUnix(){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("nix") || os.contains("nux"));
    }
    
    public static void main(String[] args) throws Exception {
        
        if(isWindows()){
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }else if(isUnix()){
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"); 
        }
        
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

