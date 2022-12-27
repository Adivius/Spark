package ScriptServer.commands;

import ScriptServer.CommandHandler;
import ScriptServer.ScriptServer;
import ScriptServer.Security;
import ScriptServer.User;
import ScriptServer.packets.PacketLog;

public class CommandHelp extends Command {


    public CommandHelp() {
        super("help", "/help <?command>", 0, Security.VISITOR);
    }

    @Override
    public boolean execute(User user, ScriptServer server, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length == 0) {
            server.sendPacket(user, new PacketLog(CommandHandler.getHelp()));
            return true;
        } else {
            if (!CommandHandler.commands.containsKey(args[0])) {
                server.sendPacket(user, new PacketLog("Command " + args[0] + " was invalid!"));
                return false;
            }
            server.sendPacket(user, new PacketLog(CommandHandler.commands.get(args[0]).USAGE));
            return true;
        }
    }
}
