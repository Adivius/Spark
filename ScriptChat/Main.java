public class Main {

    public static void main(String[] args) {
        try {
            UI.init();
            ScriptClient client = new ScriptClient(args[0], Integer.parseInt( args[1]));
            client.start();
            UI.scriptClient = client;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
