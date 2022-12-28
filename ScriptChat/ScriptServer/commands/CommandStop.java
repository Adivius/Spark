package ScriptServer.commands;

import ScriptServer.Security;
import ScriptServer.User;

public class CommandStop extends Command {

    public CommandStop() {
        super("stop", "/stop", 0, Security.OPERATOR);
    }

    @Override
    public boolean execute(User user, String[] args) {
        if (!hasPermission(user, SECURITY_LEVEL)) {
            notAllowed(user);
            return false;
        }
        user.getServer().shutdown();
        return true;
    }
}
