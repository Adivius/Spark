import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        UI.init();
        try {
            String ip = args[0];
            int port = Integer.parseInt(args[1]);
            String name = args[2];
            String pw_hash = Security.hashMd5(args[3]);

            SparkClient client = new SparkClient();
            client.start(ip, port, name, pw_hash);
            UI.sparkClient = client;

        } catch (Exception e) {
            UI.print("Error starting client: " + Arrays.toString(e.getStackTrace()));
            System.out.println("Error starting client: " + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            try {
                Thread.sleep(5000);
            } catch (Exception ex) {
                System.exit(0);
            }
            System.exit(0);
        }
    }
}
