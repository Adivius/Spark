import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * This thread is responsible for reading user's input and send it
 * to the server.
 * It runs in an infinite loop until the user types 'bye' to quit.
 *
 * @author Adivius
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            UI.print("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        try {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(UI.messageArea.getText().getBytes(StandardCharsets.UTF_8))));
        UI.print("\nEnter your name: ");
        String userName = "";
        while ((userName = bufferedReader.readLine()) == null){}
        client.setUserName(userName);
        writer.println(userName);
        String text;

        do {
            //UI.print("[" + userName + "]: ");
            text = bufferedReader.readLine();
            writer.println(text);

        } while (!text.equals("bye"));


            socket.close();
            bufferedReader.close();
        } catch (IOException ex) {

            UI.print("Error writing to server: " + ex.getMessage());
        }
    }

    public void sendMessage(String message){
        writer.println(message);
    }
}