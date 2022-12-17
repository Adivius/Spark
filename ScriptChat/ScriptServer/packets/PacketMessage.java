package ScriptServer.packets;

public class PacketMessage extends Packet{

    public String MESSAGE;

    public PacketMessage(String message){
        super(PacketIds.MESSAGE);
        this.MESSAGE = message;
    }

    public PacketMessage(String[] str){
        super(PacketIds.MESSAGE);
        this.MESSAGE = str[1];
    }

    @Override
    public String encode() {
        return Integer.toString(PacketIds.MESSAGE) + PacketIds.SEPARATOR + MESSAGE;
    }
}
