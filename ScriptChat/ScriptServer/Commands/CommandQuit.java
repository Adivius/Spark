package ScriptServer.Commands;

import ScriptServer.ScriptServer;
import ScriptServer.User;

public class CommandQuit extends Command {

    public CommandQuit() {
        super("quit", "/quit", 0, 0);
    }

    @Override
    public boolean execute(User user, ScriptServer server, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        server.removeUserById(user.getUserId(), ": Quit");
        return true;
    }
}
