package ScriptServer.packets;

public class PacketCommand extends Packet{

    public String COMMAND;

    public PacketCommand(String command){
        super(PacketIds.COMMAND);
        this.COMMAND = command;
    }

    public PacketCommand(String[] str){
        super(PacketIds.COMMAND);
        if (str.length < 2){
            this.COMMAND = null;
        }else {
            this.COMMAND = str[1];
        }
    }

    public String encode() {
        return PacketIds.COMMAND + PacketIds.SEPARATOR + COMMAND;
    }
}
