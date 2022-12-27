package ScriptServer.packets;

import ScriptServer.Security;

public class PacketConnect extends Packet {

    public String USERNAME;

    public int LEVEL;

    public PacketConnect(String userName) {
        super(PacketIds.CONNECT);
        this.USERNAME = userName;
        this.LEVEL = Security.VISITOR;
    }

    public PacketConnect(String userName, int level) {
        super(PacketIds.CONNECT);
        this.USERNAME = userName;
        this.LEVEL = level;
    }

    public PacketConnect(String[] str) {
        super(PacketIds.CONNECT);
        if (str.length >= 3) {
            this.USERNAME = str[1];
            this.LEVEL = Integer.parseInt(str[2]);

        } else if (str.length == 2) {
            this.USERNAME = str[1];
            this.LEVEL = Security.VISITOR;
        } else {
            this.USERNAME = null;
            this.LEVEL = Security.VISITOR;
        }
    }

    @Override
    public String encode() {
        return PacketIds.CONNECT + PacketIds.SEPARATOR + USERNAME + PacketIds.SEPARATOR + LEVEL;
    }
}
