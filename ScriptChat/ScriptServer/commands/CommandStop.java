package ScriptServer.commands;

import ScriptServer.ScriptServer;
import ScriptServer.Security;
import ScriptServer.User;

public class CommandStop extends Command {

    public CommandStop() {
        super("stop", "/stop", 0, Security.OPERATOR);
    }

    @Override
    public boolean execute(User user, ScriptServer server, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        server.shutdown();
        return true;
    }
}
