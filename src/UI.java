import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class UI {
    public static final int SCREEN_HEIGHT = 550, SCREEN_WIDTH = 900;
    public static JFrame screen;
    public static JTextPane chatArea;
    public static JTextField messageArea;
    public static SparkClient sparkClient;
    public static JScrollPane scrollPane;

    public static void init() {
        screen = new JFrame("ScriptChat");
        screen.setLayout(new BorderLayout());
        screen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        screen.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        screen.requestFocus();
        screen.setLocationRelativeTo(null);

        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        scrollPane = new JScrollPane(
                chatArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        screen.add(scrollPane, BorderLayout.CENTER);

        messageArea = new JTextField();
        messageArea.addActionListener(e -> sparkClient.prepareSendMessage(messageArea.getText()));
        messageArea.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        messageArea.setMargin(new Insets(10, 10, 10, 10));
        messageArea.setPreferredSize(new Dimension(SCREEN_WIDTH, 50));
        screen.add(messageArea, BorderLayout.SOUTH);
        screen.setVisible(true);
        messageArea.requestFocusInWindow();
        screen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    sparkClient.shutdown(": You Disconnected!");
                } catch (Exception ignored) {
                }
                System.exit(0);
            }
        });
    }

    public static void print(String message) {
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        chatArea.setText(chatArea.getText() + message + "\n\n");
    }
}
