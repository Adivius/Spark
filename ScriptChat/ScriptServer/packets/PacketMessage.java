package ScriptServer.packets;


public class PacketMessage extends Packet {

    public String MESSAGE;

    public String SENDER;

    public long TIMESTAMP;

    public PacketMessage(String message, String sender, long timestamp) {
        super(PacketIds.MESSAGE);
        this.MESSAGE = message;
        this.SENDER = sender;
        this.TIMESTAMP = timestamp;
    }


    public PacketMessage(String[] str) {
        super(PacketIds.MESSAGE);
        if (str.length >= 4) {
            this.MESSAGE = str[1];
            this.SENDER = str[2];
            this.TIMESTAMP = Long.parseLong(str[3]);
        } else if (str.length == 3) {
            this.MESSAGE = str[1];
            this.SENDER = str[2];
            this.TIMESTAMP = 0;

        } else if (str.length == 2) {
            this.MESSAGE = str[1];
            this.SENDER = null;
            this.TIMESTAMP = 0;
        } else {
            this.MESSAGE = null;
            this.SENDER = null;
            this.TIMESTAMP = 0;
        }
    }

    @Override
    public String encode() {
        return PacketIds.MESSAGE + PacketIds.SEPARATOR + MESSAGE + PacketIds.SEPARATOR + SENDER + PacketIds.SEPARATOR + TIMESTAMP;
    }
}
