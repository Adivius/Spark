public class Main {

    public static void main(String[] args) {
        UI.init();
        try {

            String ip = args[0];
            int port = Integer.parseInt(args[1]);
            String name =args[2];
            int securityCode = Integer.parseInt(args[3]);

            ScriptClient client = new ScriptClient();
            client.start(ip, port, name, securityCode);
            UI.scriptClient = client;

        } catch (Exception e) {
            System.out.println("Error starting client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
