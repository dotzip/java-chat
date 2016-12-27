import java.net.*;
import java.io.*;

class printIP{ 

    public static void main(String[] args) {

        try{    
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine(); //you get the IP as a String
            System.out.println(ip);
        }
        catch(IOException ex){
            System.out.println(ex);
        }
    
    }
}