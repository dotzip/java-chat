package chatclient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatClient extends JFrame{
    private JFrame connectFrame = new JFrame("Connection");
    // Создание строки главного меню
    private JMenuBar menuBar = new JMenuBar();
    private JButton sendBtn = new JButton("Send");
    private JTextField input = new JTextField();
    private JTextPane output = new JTextPane();
    StyledDocument doc = (StyledDocument) output.getDocument();
    Style style = doc.addStyle("myStyle", null);
    Style style2 = doc.addStyle("myStyle2", null);
    private DefaultCaret caret = (DefaultCaret)output.getCaret();
    private JScrollPane scroll = new JScrollPane(output); // цепляем скролл на текстовое поле
    private static Socket s = null;
    private PrintWriter printwriter = null;
    private JTextField nicknameInput = new JTextField();
    private JLabel nicknameLabel = new JLabel("Enter your name:");
    private JTextField ipServerInput = new JTextField();
    private JLabel ipServerLabel = new JLabel("Enter server's IP:");
    private JButton connectBtn = new JButton("Connect");
    private JButton disconnectBtn = new JButton("Disconnect");
    public static int locationFrameX;
    public static int locationFrameY;
    public static int locationDialogX;
    public static int locationDialogY;

    ChatClient() throws IOException{
        super("ChatClient");
        
        // устанавливаем иконку приложения
        setAppIcon("message_icon.png", ChatClient.this);
        setAppIcon("message_icon.png", connectFrame);

        // запуск окна по центру экрана на любом мониторе
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        locationDialogX = (screenSize.width - 300) / 2;
        locationDialogY = (screenSize.height - 200) / 2;

        // отображение стилизованного текста
        StyleConstants.setForeground(style, new Color(179,122,0));
        StyleConstants.setFontSize(style, 17);
        StyleConstants.setForeground(style2, Color.BLUE);
        StyleConstants.setFontSize(style2, 17);

        this.setBounds((screenSize.width-635)/2, (screenSize.height-500)/2, 635, 500); // расположение и размер фрейма
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
                            doc.insertString(doc.getLength(), nicknameInput.getText()+ ": " + msg + "\n", style);
                        }
                        catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                try{
                   if(s != null){
                        printwriter = new PrintWriter(s.getOutputStream());
                        printwriter.println("=== " + nicknameInput.getText() + " was disconnected ===\n");
                        printwriter.flush();
                        System.exit(0);
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
        
        Image sendImg, connectionImg, disconnectImg, exitImg;
        try {
            exitImg = ImageIO.read(getClass().getResource("exit.png"));
            disconnectImg = ImageIO.read(getClass().getResource("disconnect.png"));
            connectionImg = ImageIO.read(getClass().getResource("connect.png"));
            sendImg = ImageIO.read(getClass().getResource("chat.png"));
            sendBtn.setIcon(new ImageIcon(sendImg));
            connectBtn.setIcon(new ImageIcon(connectionImg));
            disconnectBtn.setIcon(new ImageIcon(disconnectImg));
        } catch (IOException ex) {
           System.out.println(ex);
        }
        
        // конфигурация размеров и расположений элементов
        connectFrame.setBounds((screenSize.width-245)/2, (screenSize.height-250)/2, 245, 270);
        connectFrame.setLayout(null);
        connectFrame.setResizable(false);
        menuBar.add(createConnectionMenu());
        this.setJMenuBar(menuBar);
        sendBtn.setBounds(529, 403, 93, 35);
        sendBtn.addActionListener(new ButtonListener()); // цепляем на кнопку обработчик
        input.setBounds(10, 385, 515, 54);
        input.setFont(new Font("Batang", Font.PLAIN, 16));
        output.setEditable(false); // нельзя менять текст в этом поле
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(10, 10, 610, 365);
        nicknameLabel.setBounds(25, 10, 170, 20);
        nicknameLabel.setFont(new Font("Batang", Font.PLAIN, 14));
        nicknameInput.setBounds(25, 35, 195, 30);
        ipServerLabel.setBounds(25, 75, 170, 20);
        ipServerLabel.setFont(new Font("Batang", Font.PLAIN, 14));
        ipServerInput.setBounds(25, 100, 195, 30);
        connectBtn.setBounds(80, 150, 140, 30);
        connectBtn.addActionListener(new ButtonListener());
        disconnectBtn.setBounds(80, 190, 140, 30);
        disconnectBtn.setEnabled(false);
        disconnectBtn.addActionListener(new ButtonListener());

        // добавляем элементы на фрейм
        this.add(sendBtn);
        this.add(input);
        this.add(scroll);
        connectFrame.add(nicknameInput);
        connectFrame.add(nicknameLabel);
        connectFrame.add(ipServerInput);
        connectFrame.add(ipServerLabel);
        connectFrame.add(connectBtn);
        connectFrame.add(disconnectBtn);

        this.getContentPane().setBackground(new Color(165,217,199));
        connectFrame.getContentPane().setBackground(new Color(165,217,199));
    }

    // Абстрактный класс кнопки выхода
    class ExitAction extends AbstractAction{
        ExitAction() {
            putValue(NAME, "Exit");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                if(s != null){
                    printwriter = new PrintWriter(s.getOutputStream());
                    printwriter.println("=== " + nicknameInput.getText() + " was disconnected ===\n");
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

     private JMenu createConnectionMenu() throws IOException{
         // Создание выпадающего меню
        JMenu connection = new JMenu("Connection");
        // Пункт меню "Connection" с изображением
        JMenuItem connectionItem = new JMenuItem("Join to chat",
                             new ImageIcon(ImageIO.read(getClass().getResource("connect.png"))));
        // Пункт меню из команды с выходом из программы
        JMenuItem exit = new JMenuItem(new ExitAction());
        exit.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("exit.png"))));

        connection.add(connectionItem);
        connection.addSeparator();
        connection.add(exit);

        connectionItem.addActionListener((ActionEvent arg0) -> {
            connectFrame.setVisible(true);
        });

        return connection;
     }

    void aboutConnect(String msg) throws BadLocationException{
        doc.insertString(doc.getLength(), msg + "\n", null);
    }

    void setOutputText(String msg) throws BadLocationException{
        if(msg.equals("=== Server was shutdown ===") | msg.contains("was disconnected")){
            doc.insertString(doc.getLength(), msg + "\n", null);
        }else{
            doc.insertString(doc.getLength(), msg + "\n", style2);
            // окно оповещения о сообщении
            if(!(this.isActive())){
                String quickAnswer = JOptionPane.showInputDialog(null, msg, "You have a new message", JOptionPane.INFORMATION_MESSAGE);
                if(!(quickAnswer.equals(""))){
                        try {
                            printwriter = new PrintWriter(s.getOutputStream());
                            printwriter.println(nicknameInput.getText()+ ": " + quickAnswer);
                            printwriter.flush();
                            input.setText("");
                            doc.insertString(doc.getLength(), nicknameInput.getText()+ ": " + quickAnswer + "\n", style);
                        }
                        catch (Exception ex) {
                            System.out.println(ex);
                        }
                }
            }
        }


    }

    // устанавливаем иконку приложения
    private void setAppIcon(String iconPath, JFrame frm) {
        frm.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(iconPath)));
    }

    class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            // обработчик нажатия кнопки (соединение с сервером)
            if(e.getSource() == connectBtn){
                try{
                    if(!(nicknameInput.getText().equals("") | ipServerInput.getText().equals(""))){
                        if((s = new Socket(ipServerInput.getText(), 22222)).isConnected()){
                            ChatClient.this.aboutConnect("=== You have successfully joined! Welcome! === ");
                            disconnectBtn.setEnabled(true);
                            SocketInputThread threadIn = new SocketInputThread(s, ChatClient.this);
                            Thread t = new Thread(threadIn);
                            t.start();
                            connectBtn.setEnabled(false);
                            connectFrame.setVisible(false);
                        }else{
                            ChatClient.this.aboutConnect("Connection failed");
                        }
                    }
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Connection failed");
                }
            // обработчик нажатия кнопки (запись текста в сокет и вывод его же на экран)
            }else if(e.getSource() == sendBtn){
                try{
                    String msg = input.getText();
                    if(!(msg.equals(""))){
                        printwriter = new PrintWriter(s.getOutputStream());
                        printwriter.println(nicknameInput.getText()+ ": " + msg);
                        printwriter.flush();
                        input.setText("");
                        doc.insertString(doc.getLength(), nicknameInput.getText()+ ": " + msg + "\n", style);
                    }
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }else if(e.getSource() == disconnectBtn){
                try{
                    if(s != null){
                        printwriter = new PrintWriter(s.getOutputStream());
                        printwriter.println("=== " + nicknameInput.getText() + " was disconnected ===\n");
                        printwriter.flush();
                        doc.insertString(doc.getLength(), "=== You was disconnected ===" + "\n", null);
                        disconnectBtn.setEnabled(false);
                        connectBtn.setEnabled(true);
                        s.close();
                        s = null;
                    }
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
        } catch (Exception ex) {
            System.out.println(ex);
        }

        ChatClient app = new ChatClient();
        app.setVisible(true);

    }
}
