package ScriptServer.packets;

public class PacketLog extends Packet{

    public String MESSAGE;

    public PacketLog(String message){
        super(PacketIds.LOG);
        this.MESSAGE = message;
    }

    public PacketLog(String[] str){
        super(PacketIds.LOG);
        if (str.length < 2){
            this.MESSAGE = null;
        }else {
            this.MESSAGE = str[1];
        }
    }

    public String encode() {
        return PacketIds.LOG + PacketIds.SEPARATOR + MESSAGE;
    }
}
