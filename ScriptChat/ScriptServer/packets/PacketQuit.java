package ScriptServer.packets;

public class PacketQuit extends Packet{

    public String MESSAGE;

    public PacketQuit(String message){
        super(PacketIds.QUIT);
        this.MESSAGE = message;
    }

    public PacketQuit(String[] str){
        super(PacketIds.QUIT);
        if (str.length < 2){
            this.MESSAGE = null;
        }else {
            this.MESSAGE = str[1];
        }
    }

    @Override
    public String encode() {
        return PacketIds.QUIT + PacketIds.SEPARATOR + MESSAGE;
    }
}
