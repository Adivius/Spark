package ScriptServer.commands;

import ScriptServer.ScriptServer;
import ScriptServer.Security;
import ScriptServer.User;
import ScriptServer.packets.PacketLog;

public class CommandSetLevel extends Command {

    public CommandSetLevel() {
        super("setlevel", "/setlevel <name> <level>", 2, Security.ADMIN);
    }

    @Override
    public boolean execute(User user, ScriptServer server, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length < ARGSLENGHT) {
            server.sendPacket(user, new PacketLog("Please enter a user and a level!"));
            return false;
        }
        if (!server.hasUserByName(args[0])) {
            server.sendPacket(user, new PacketLog("User " + args[0] + " is not online!"));
            return false;
        }
        if (!Security.isInt(args[1])) {
            server.sendPacket(user, new PacketLog("Please enter a valid level!"));
            return false;
        }

        int level = Integer.parseInt(args[1]);
        User change = server.getUserByName(args[0]);

        if (!hasPermission(user, level)) {
            notAllowed(user);
            return false;
        }
        change.setSecurityLevel(level);
        server.sendPacket(change, new PacketLog("Your security level was set to " + level));
        server.sendPacket(user, new PacketLog(" Security level of " + change.getUserName() + " was set to " + level));
        return true;
    }
}
