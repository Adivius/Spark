package ScriptServer;

public class ServerMain {

    public static void main(String[] args) {
        try {
            new ScriptServer(Integer.parseInt(args[0])).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
