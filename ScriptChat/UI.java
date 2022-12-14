import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public abstract class UI {
    public static JFrame screen;
    public static JTextPane chatArea;
    public static JTextField messageArea;
    public static final int SCREEN_HEIGHT = 450, SCREEN_WIDTH = 800;

    public static void init(){
        screen = new JFrame("ScriptChat");
        screen.setLayout(new BorderLayout());
        screen.setLocationRelativeTo(null);
        screen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        screen.setResizable(false);
        screen.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        screen.requestFocus();

        chatArea = new JTextPane();
        chatArea.setEditable(false);
        screen.add(chatArea, BorderLayout.CENTER);

        messageArea = new JTextField();
        messageArea.addActionListener(e -> sendMessage());
        messageArea.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        messageArea.setMargin(new Insets(10, 10, 10, 10));
        messageArea.setPreferredSize(new Dimension(SCREEN_WIDTH, 50));
        screen.add(messageArea, BorderLayout.SOUTH);

        screen.setVisible(true);
    }

    public static void print(String message){
        System.out.println(message);
        chatArea.setText(chatArea.getText() + message + '\n');
    }

    public static void sendMessage(){
        System.out.println(messageArea.getText());
        ChatClient.writer.println(messageArea.getText());
        messageArea.setText("");
    }
}
