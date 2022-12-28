package ScriptServer.commands;

import ScriptServer.Security;
import ScriptServer.User;

public class CommandSetLevel extends Command {

    public CommandSetLevel() {
        super("setlevel", "/setlevel <username> <level>", 2, Security.ADMIN);
    }

    @Override
    public boolean execute(User user, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length < ARGSLENGHT) {
            user.sendLog("Please enter a user and a level!");
            return false;
        }
        if (!user.getServer().hasUserByName(args[0])) {
            user.sendLog("User " + args[0] + " is not online!");
            return false;
        }
        if (!Security.isInt(args[1])) {
            user.sendLog("Please enter a valid level!");
            return false;
        }

        int level = Integer.parseInt(args[1]);
        User change = user.getServer().getUserByName(args[0]);

        if (!hasPermission(user, level)) {
            notAllowed(user);
            return false;
        }
        change.setSecurityLevel(level);
        change.sendLog("Your security level was set to \" + level");
        user.sendLog(" Security level of " + change.getUserName() + " was set to " + level);
        return true;
    }
}
