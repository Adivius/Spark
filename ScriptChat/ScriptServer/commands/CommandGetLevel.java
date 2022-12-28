package ScriptServer.commands;

import ScriptServer.Security;
import ScriptServer.User;
public class CommandGetLevel extends Command {

    public CommandGetLevel() {
        super("getlevel", "/getlevel <?username>", 0, Security.MEMBER);
    }

    @Override
    public boolean execute(User user, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length == 0) {
            user.sendLog("Your security level: " + user.getSecurityLevel());
            return true;
        }
        if (!user.getServer().hasUserByName(args[0])) {
            user.sendLog("User " + args[0] + " is not online!");
            return false;
        }
        user.sendLog(args[0] + "'s security level: " + user.getServer().getUserByName(args[0]).getSecurityLevel());
        return true;
    }
}
