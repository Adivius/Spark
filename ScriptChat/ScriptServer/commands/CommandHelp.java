package ScriptServer.commands;

import ScriptServer.CommandHandler;
import ScriptServer.Security;
import ScriptServer.User;
import ScriptServer.packets.PacketLog;

public class CommandHelp extends Command {


    public CommandHelp() {
        super("help", "/help <?command>", 0, Security.VISITOR);
    }

    @Override
    public boolean execute(User user, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length == 0) {
            user.sendLog(CommandHandler.getHelp());
            return true;
        } else {
            if (!CommandHandler.commands.containsKey(args[0])) {
                user.sendLog("Command " + args[0] + " was invalid!");
                return false;
            }
            user.sendLog(CommandHandler.commands.get(args[0]).USAGE);
            return true;
        }
    }
}
