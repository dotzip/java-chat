import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;


public class Test2 extends JFrame { // расширяем класс JFrame для удобства (можно обращаться к нашему фрейму с помощью this)
    private JButton btn = new JButton("Send"); // кнопка, при нажатии на которую, наше сообщение будет уходить
    private JTextField input = new JTextField(); // поле для ввода 
    private JTextArea output = new JTextArea(); // поле для вывода
    
    public Test2(){
        super("myApplication"); // название фрейма
        this.setBounds(200, 100, 650, 500); // расположение и размер фрейма
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // действие при выходе юзера из приложения
        this.setLayout(null); // чистим слои, чтобы расположение элементов во фрейме было по координатам 
        this.setResizable(false); // размер фрейма фиксирован 
        
        // добавляем элементы на фрейм
        this.getContentPane().add(btn); 
        this.getContentPane().add(input);
        this.getContentPane().add(output);
        
		// конфигурация размеров и расположений элементов
        btn.setBounds(540, 410, 100, 50);
        btn.addActionListener(new ButtonEventListener()); // цепляем на кнопку обработчик
        input.setBounds(10, 410, 530, 50);
        output.setBounds(10, 10, 630, 390);
        output.setEditable(false); // нельзя менять текст в этом поле
    }
    
    // обработчик нажатия кнопки
    class ButtonEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String msg = input.getText();
            input.setText("");
            output.append(msg+"\n");
        }
    }
    
    public static void main(String[] args){
        Test2 app = new Test2();
        app.setVisible(true);
    }
}
