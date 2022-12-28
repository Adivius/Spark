package ScriptServer.packets;

public class Packet {

    int ID;

    public Packet(int id) {
        ID = id;
    }

    public String encode() {
        return ID + "";
    }
}

