package chatclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.DefaultCaret;

public class ChatClient extends JFrame{
    private JButton sendBtn = new JButton("Send");
    private JTextField input = new JTextField(); 
    private JTextArea output = new JTextArea();
    private DefaultCaret caret = (DefaultCaret)output.getCaret();
    private JScrollPane scroll = new JScrollPane(output); // цепляем скролл на текстовое поле
    private static Socket s = null;
    private PrintWriter printwriter = null;
    private JTextField nicknameInput = new JTextField();
    private JLabel nicknameLabel = new JLabel("Enter your name:");
    private JTextField ipServerInput = new JTextField();
    private JLabel ipServerLabel = new JLabel("Enter server's IP:");
    private JButton exitBtn = new JButton("Exit");
    private JButton connectBtn = new JButton("Connect");
    
    ChatClient(){
        super("ChatClient");
        
        setIcon(); // устанавливаем иконку приложения
        
        this.setBounds(200, 100, 825, 490); // расположение и размер фрейма
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null); // чистим слои, чтобы расположение элементов во фрейме было по координатам 
        this.setResizable(false); // размер фрейма фиксирован 
        
        input.addKeyListener(new KeyListener() { // вешаем обработчик на поле ввода
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) { // метод при нажатии клавиш
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String msg = input.getText();
                    if(!(msg.equals(""))){
                        try {
                            printwriter = new PrintWriter(s.getOutputStream());
                            printwriter.println(nicknameInput.getText()+ ": " + msg);
                            printwriter.flush();
                            input.setText("");
                            output.append(nicknameInput.getText()+ ": " + msg + "\n");
                        }
                        catch (IOException ex) {
                            System.out.println(ex);
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        // конфигурация размеров и расположений элементов
        sendBtn.setBounds(540, 410, 80, 40);
        sendBtn.addActionListener(new ButtonSendListener()); // цепляем на кнопку обработчик
        input.setBounds(10, 410, 530, 40);
        output.setLineWrap(true); // перенос текста
        output.setEditable(false); // нельзя менять текст в этом поле
        output.setFont(new Font("Calibri", Font.PLAIN, 18));
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(10, 10, 610, 390);
        nicknameLabel.setBounds(635, 10, 170, 20);
        nicknameInput.setBounds(635, 35, 170, 30);
        ipServerLabel.setBounds(635, 75, 170, 20);
        ipServerInput.setBounds(635, 100, 170, 30);
        exitBtn.setBounds(725, 410, 80, 40);
        exitBtn.addActionListener(new ButtonExitListener());
        connectBtn.setBounds(725, 150, 80, 30);
        connectBtn.addActionListener(new ButtonConnectListener());
        
        // добавляем элементы на фрейм
        this.getContentPane().add(sendBtn); 
        this.getContentPane().add(input);
        this.getContentPane().add(scroll);
        this.getContentPane().add(nicknameInput);
        this.getContentPane().add(nicknameLabel);
        this.getContentPane().add(ipServerInput);
        this.getContentPane().add(ipServerLabel);
        this.getContentPane().add(exitBtn);
        this.getContentPane().add(connectBtn);
        
        this.getContentPane().setBackground(new Color(204, 255, 204));
    }
    
    void setFriendText(String msg){
        output.append(msg + "\n");
    }
    
    void setOutputText(String msg){
        output.append(msg + "\n");
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("message_icon.png")));
    }
    
    class ButtonConnectListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                if(!(nicknameInput.getText().equals("") | ipServerInput.getText().equals(""))){
                    s = new Socket(ipServerInput.getText(), 22222);
                    if(s.isConnected()){
                        ChatClient.this.setOutputText("=== You have successfully joined! Welcome! === ");
                        SocketInputThread threadIn = new SocketInputThread(s, ChatClient.this);
                        Thread t = new Thread(threadIn);
                        t.start();
                    }else{
                        ChatClient.this.setOutputText("Connection failed");
                    }
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Connection failed");
            }
        }
    }
    
    // обработчик нажатия кнопки (выход из программы при нажатии)
    class ButtonExitListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                if(s != null){
                    printwriter = new PrintWriter(s.getOutputStream());
                    printwriter.println(nicknameInput.getText() + " was disconnected\n");
                    printwriter.flush();
                    System.exit(0);
                }else{
                    System.exit(0);
                }
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }
    }
    
    // обработчик нажатия кнопки (запись текста в сокет и вывод его же на экран)
    class ButtonSendListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                String msg = input.getText();
                if(!(msg.equals(""))){
                    printwriter = new PrintWriter(s.getOutputStream());
                    printwriter.println(nicknameInput.getText()+ ": " + msg);
                    printwriter.flush();
                    input.setText("");
                    output.append(nicknameInput.getText()+ ": " + msg + "\n");
                }
            }
            catch(IOException ex){
                System.out.println(ex);
            }
        }
            
    }
    
    public static boolean isWindows(){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("win"));
    }
    
    public static boolean isUnix(){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("nix") || os.contains("nux"));
    }
    
    public static void main(String[] args) {
        try {
            if(isWindows()){
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }else if(isUnix()){
               UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"); 
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        ChatClient app = new ChatClient();
        app.setVisible(true);
        
    }
}
