package ScriptServer.commands;

import ScriptServer.Security;
import ScriptServer.User;

public class CommandKick extends Command {

    public CommandKick() {
        super("kick", "/kick <username>", 1, Security.ADMIN);
    }

    @Override
    public boolean execute(User user, String[] args) {
        if (!hasPermission(user, SECURITY_LEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length < ARGS_LENGTH) {
            user.sendLog("Please enter a user!");
            return false;
        }
        if (!user.getServer().hasUserByName(args[0])) {
            user.sendLog("User " + args[0] + " is not online!");
            return false;
        }
        user.getServer().removeUserById(user.getServer().getUserByName(args[0]).getUserId(), "Kicked by Admin");
        return false;
    }
}
