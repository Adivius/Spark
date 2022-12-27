package ScriptServer.commands;

import ScriptServer.ScriptServer;
import ScriptServer.Security;
import ScriptServer.User;
import ScriptServer.packets.PacketLog;

public class CommandGetLevel extends Command {

    public CommandGetLevel() {
        super("getlevel", "/getlevel <?name>", 0, Security.MEMBER);
    }

    @Override
    public boolean execute(User user, ScriptServer server, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length == 0) {
            user.getServer().sendPacket(user, new PacketLog("Your securitylevel: " + user.getSecurityLevel()));
            return true;
        }
        if (!server.hasUserByName(args[0])) {
            server.sendPacket(user, new PacketLog("User " + args[0] + " is not online!"));
            return false;
        }
        user.getServer().sendPacket(user, new PacketLog(args[0] + "'s securitylevel: " + server.getUserByName(args[0]).getSecurityLevel()));
        return true;
    }
}
