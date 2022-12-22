package ScriptServer.packets;

public class PacketJoin extends Packet{

    public String MESSAGE;

    public PacketJoin(String message){
        super(PacketIds.JOIN);
        this.MESSAGE = message;
    }

    public PacketJoin(String[] str){
        super(PacketIds.JOIN);
        if (str.length < 2){
            this.MESSAGE = null;
        }else {
            this.MESSAGE = str[1];
        }
    }

    @Override
    public String encode() {
        return PacketIds.JOIN + PacketIds.SEPARATOR + MESSAGE;
    }
}
