package chatclient;

import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.DefaultCaret;

public class ChatClient extends JFrame{
    private JButton btn = new JButton("Send");
    private JTextField input = new JTextField(); 
    private JTextArea output = new JTextArea();
    private DefaultCaret caret = (DefaultCaret)output.getCaret();
    private JScrollPane scroll = new JScrollPane(output); // цепляем скролл на текстовое поле
    private static Socket s = null;
    private PrintWriter printwriter = null;
    
    ChatClient(){
        super("ChatClient");
        this.setBounds(200, 100, 650, 500); // расположение и размер фрейма
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null); // чистим слои, чтобы расположение элементов во фрейме было по координатам 
        this.setResizable(false); // размер фрейма фиксирован 
        
        input.addKeyListener(new KeyListener() { // вешаем обработчик на поле ввода
            @Override
            public void keyTyped(KeyEvent e) {
              
            }

            @Override
            public void keyPressed(KeyEvent e) { // метод при нажатии клавиш
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String msg = input.getText();
                    if((msg.equals("/exit"))){
                        try{
                            printwriter = new PrintWriter(s.getOutputStream());
                            printwriter.println(msg);
                            printwriter.flush();
                            System.exit(0);
                        }
                         catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        }
                    }
                    if(!(msg.equals(""))){
                        try {
                            printwriter = new PrintWriter(s.getOutputStream());
                            printwriter.println(msg);
                            printwriter.flush();
                            input.setText("");
                            output.append("ME: "+ msg + "\n");
                        }
                        catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        
        // конфигурация размеров и расположений элементов
        btn.setBounds(540, 410, 100, 50);
        btn.addActionListener(new ButtonEventListener()); // цепляем на кнопку обработчик
        input.setBounds(10, 410, 530, 50);
        output.setLineWrap(true); // перенос текста
        output.setEditable(false); // нельзя менять текст в этом поле
        output.setFont(new Font("Calibri", Font.PLAIN, 18));
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(10, 10, 630, 390);
        
        // добавляем элементы на фрейм
        this.getContentPane().add(btn); 
        this.getContentPane().add(input);
        this.getContentPane().add(scroll);
    }
    
    void setFriendText(String msg){
        output.append("FRIEND: " + msg + "\n");
    }
    
    void setOutputText(String msg){
        output.append(msg + "\n");
    }
    
    // обработчик нажатия кнопки (запись текста в сокет и вывод его же на экран)
    class ButtonEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                String msg = input.getText();
                if((msg.equals("/exit"))){
                    printwriter = new PrintWriter(s.getOutputStream());
                    printwriter.println(msg);
                    printwriter.flush();
                    System.exit(0);
                }
                if(!(msg.equals(""))){
                    printwriter = new PrintWriter(s.getOutputStream());
                    printwriter.println(msg);
                    printwriter.flush();
                    input.setText("");
                    output.append("ME: "+ msg + "\n");
                }
            }
            catch(IOException ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        }
            
    }
    
    public static void main(String[] args) {
        ChatClient app = new ChatClient();
        app.setVisible(true);
        
        try{
            app.setOutputText("Connecting to the server...");
            s = new Socket("192.168.0.59", 22222);
            if(s.isConnected()){
                app.setOutputText("You have successfully joined!");
                SocketInputThread threadIn = new SocketInputThread(s, app);
                Thread t = new Thread(threadIn);
                t.start();
            }else{
                app.setOutputText("Connection is closed");
            }
            
            
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
}
