import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class ServerUI {
    public static JFrame screen;
    public static JTextArea textArea;
    public static JButton connectButton;

    public static void init() {
        screen = new JFrame("ScriptServer");
        screen.setLayout(new BorderLayout());
        screen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        screen.setBounds(0, 0, 600, 600);
        screen.requestFocus();


        connectButton = new JButton("Start");
        connectButton.setPreferredSize(new Dimension(100, 600));
        screen.add(connectButton, BorderLayout.LINE_START);

        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(100, 600));
        textArea.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        textArea.setEditable(false);
        screen.add(textArea, BorderLayout.CENTER);

        connectButton.addActionListener(e -> showStartDialog());

        screen.setVisible(true);

        screen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (ServerMain.server != null) {
                    ServerMain.stop();
                }
            }
        });
    }

    public static void showStartDialog() {
        if (ServerMain.server == null) {
            try {
                int port = Integer.parseInt(JOptionPane.showInputDialog("Please enter port"));
                ServerMain.start(port);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Please enter a valid port");
            }
        }else {
            ServerMain.stop();
        }


    }
}
