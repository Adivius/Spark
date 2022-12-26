package ScriptServer.Commands;

import ScriptServer.ScriptServer;
import ScriptServer.User;
import ScriptServer.packets.PacketLog;

public class CommandKick extends Command{

    public CommandKick(){
        super("kick", "/kick <name>", 1, 2);
    }

    @Override
    public boolean execute(User user, ScriptServer server, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length < ARGSLENGHT) {
            server.sendPacket(user,  new PacketLog("Please enter a user!"));
            return false;
        }
        if (!server.hasUserByName(args[0])) {
            server.sendPacket(user, new PacketLog("User " + args[0] + " is not online!"));
            return false;
        }
        server.removeUserById(server.getUserByName(args[0]).getUserId(), ": Kicked by Admin");
        return false;
    }
}
