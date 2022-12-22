package ScriptServer.packets;

public class PacketMessage extends Packet{

    public String MESSAGE;

    public String SENDER;

    public PacketMessage(String message, String sender){
        super(PacketIds.MESSAGE);
        this.MESSAGE = message;
        this.SENDER = sender;
    }

    public PacketMessage(String message){
        super(PacketIds.MESSAGE);
        this.MESSAGE = message;
        this.SENDER = null;
    }

    public PacketMessage(String[] str){
        super(PacketIds.MESSAGE);
        if (str.length == 3){
            this.MESSAGE = str[1];
            this.SENDER = str[2];

        }else if (str.length == 2){
            this.MESSAGE = str[1];
            this.SENDER = null;
        } else {
            this.MESSAGE = null;
            this.SENDER = null;
        }
    }

    @Override
    public String encode() {
        return PacketIds.MESSAGE + PacketIds.SEPARATOR + MESSAGE + PacketIds.SEPARATOR + SENDER;
    }
}
