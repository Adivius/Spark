package ScriptServer.commands;

import ScriptServer.ScriptServer;
import ScriptServer.Security;
import ScriptServer.User;
import ScriptServer.packets.PacketLog;

public class CommandName extends Command {

    public CommandName() {
        super("name", "/name <name>", 1, Security.ADMIN);
    }

    @Override
    public boolean execute(User user, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length == 0) {
            user.sendLog("Your name: " + user.getUserName());
            return false;
        }
        String newName = args[0].toLowerCase();
        ScriptServer server = user.getServer();
        if (server.hasUserByName(newName)) {
            user.sendLog("This name is occupied!");
            return false;
        }
        if (!Security.nameAllowed(newName)) {
            user.sendLog("This name is blocked!");
            return false;
        }
        server.broadcast(new PacketLog(user.getUserName() + "'s name was changed to " + newName), null);
        user.setUserName(newName);
        return true;
    }
}
