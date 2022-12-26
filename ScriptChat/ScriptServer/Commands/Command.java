package ScriptServer.Commands;

import ScriptServer.ScriptServer;
import ScriptServer.User;
import ScriptServer.packets.PacketLog;

public abstract class Command {
    public String USAGE;
    public String NAME;
    public int ARGSLENGHT;
    public int SECURITYLEVEL;


    public Command(String name, String usage, int argsLength, int securityLevel){
        this.USAGE = usage;
        this.NAME = name;
        this.ARGSLENGHT = argsLength;
        this.SECURITYLEVEL = securityLevel;
    }

    public abstract boolean execute(User user, ScriptServer server, String[] args);

    public boolean hasPermission(User user, int minSecurityLevel){
        return minSecurityLevel <= user.getSecurityLevel();
    }

    public void notAllowed(User user) {
        user.getServer().sendPacket(user, new PacketLog("You don't have the permission to do that!"));
    }
}
