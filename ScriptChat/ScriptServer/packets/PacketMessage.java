package ScriptServer.packets;

public class PacketMessage extends Packet{

    public String MESSAGE;

    public PacketMessage(String message){
        super(PacketIds.MESSAGE);
        this.MESSAGE = message;
    }

    public PacketMessage(String[] str){
        super(PacketIds.MESSAGE);
        if (str.length < 2){
            this.MESSAGE = null;
        }else {
            this.MESSAGE = str[1];
        }
    }

    @Override
    public String encode() {
        return PacketIds.MESSAGE + PacketIds.SEPARATOR + MESSAGE;
    }
}
