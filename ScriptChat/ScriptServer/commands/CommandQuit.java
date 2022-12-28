package ScriptServer.commands;

import ScriptServer.ScriptServer;
import ScriptServer.Security;
import ScriptServer.User;

public class CommandQuit extends Command {

    public CommandQuit() {
        super("quit", "/quit", 0, Security.VISITOR);
    }

    @Override
    public boolean execute(User user, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        user.getServer().removeUserById(user.getUserId(), ": Quit");
        return true;
    }
}
