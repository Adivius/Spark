package ScriptServer.packets;

public class PacketConnect extends Packet{

    public String USERNAME;

    public PacketConnect(String userName){
        super(PacketIds.CONNECT);
        this.USERNAME = userName;
    }

    public PacketConnect(String[] str){
        super(PacketIds.CONNECT);
        if (str.length < 2){
            this.USERNAME = null;
        }else {
            this.USERNAME = str[1];
        }
    }

    @Override
    public String encode() {
        return PacketIds.CONNECT + PacketIds.SEPARATOR + USERNAME;
    }
}
