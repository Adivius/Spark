import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean running;
    public String nickname;


    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    @Override
    public void run() {
        try {
            running = true;
            client = new Socket("192.168.178.38", 1243);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inputHandler = new InputHandler();
            Thread thread = new Thread(inputHandler);
            thread.start();

            String inMessage;
            while ((inMessage = in.readLine()) != null){
                System.out.println(inMessage);
            }
        } catch (Exception e) {
            shutdown();
        }
    }

    public void shutdown(){
        try {
            running = false;
            in.close();
            out.close();
            if (!client.isClosed()){
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                while (running) {
                    String message = in.readLine();
                    out.println(message);
                    if (message.equals("/quit")) {
                        inputReader.close();
                        shutdown();
                    }
                }
            } catch (Exception e) {
                shutdown();
            }
        }
    }


}
