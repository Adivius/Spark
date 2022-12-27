package ScriptServer.packets;

public class Packet {

    int ID;

    public Packet(int id) {
        ID = id;
    }

    public final int getID() {
        return ID;
    }

    public String encode() {
        return ID + "";
    }

    public static String[] decode(String packet) {
        return packet.split(PacketIds.SEPARATOR);
    }

    public static String merge(Object[] objects) {
        String out = "";

        for (Object obj : objects) {
            out += PacketIds.SEPARATOR + obj;
        }

        return out;
    }
}

