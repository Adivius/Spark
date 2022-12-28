package ScriptServer.packets;

public class PacketName extends Packet{

    public String NAME;

    public PacketName(String name) {
        super(PacketIds.NAME);
        this.NAME = name;
    }

    public PacketName(String[] str) {
        super(PacketIds.NAME);
        if (str.length < 2) {
            this.NAME = null;
        } else {
            this.NAME = str[1];
        }
    }

    public String encode() {
        return PacketIds.NAME + PacketIds.SEPARATOR + NAME;
    }
}
