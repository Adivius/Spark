import javax.swing.*;
import java.awt.*;

public abstract class UI {
    public static JFrame screen;
    public static JTextPane chatArea;
    public static JButton sendButton;
    public static JTextField messageArea;
    public static JButton connectButton;
    public static final int SCREEN_HEIGHT = 900, SCREEN_WIGHT = 1600;

    public static void init(){
        screen = new JFrame("ScriptChat");
        screen.setLayout(new BorderLayout());
        screen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        screen.setResizable(false);
        screen.setBounds(0, 0, SCREEN_WIGHT, SCREEN_HEIGHT);
        screen.requestFocus();


        connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(100, 30));
        connectButton.addActionListener(e -> showStartDialog());
        screen.add(connectButton, BorderLayout.PAGE_START);

        screen.setVisible(true);
    }


    public static void showStartDialog(){
        try {
            int port = Integer.parseInt(JOptionPane.showInputDialog("Please enter port"));
            Main.start(port);
        }catch (Exception e){
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Please enter a valid port");
        }


    }
}
