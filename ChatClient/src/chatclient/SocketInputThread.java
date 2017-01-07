package chatclient;

import java.net.*;
import java.util.*;

class SocketInputThread implements Runnable{
    private Socket s = null;
    private Scanner in = null;
    private String msg;
    private final ChatClient app;

    SocketInputThread(Socket s, ChatClient app) {
        this.s = s;
        this.app = app;
    }
    
    @Override
    public void run(){
        try{
            in = new Scanner(s.getInputStream());
            while(true){
                if(in.hasNext()){
                    msg = in.nextLine();
                    app.setFriendText(msg);
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }
    
}
