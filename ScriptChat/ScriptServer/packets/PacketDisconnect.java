package ScriptServer.packets;

public class PacketDisconnect extends Packet {

    public String REASON;

    public PacketDisconnect() {
        super(PacketIds.DISCONNECT);
        this.REASON = null;
    }

    public PacketDisconnect(String str) {
        super(PacketIds.DISCONNECT);
        this.REASON = str;
    }

    public PacketDisconnect(String[] str) {
        super(PacketIds.DISCONNECT);
        if (str.length > 0) {
            this.REASON = str[1];
        } else {
            this.REASON = null;
        }
    }

    @Override
    public String encode() {
        return ID + PacketIds.SEPARATOR + REASON;
    }
}
