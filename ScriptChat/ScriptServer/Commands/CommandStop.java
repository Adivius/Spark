package ScriptServer.Commands;

import ScriptServer.ScriptServer;
import ScriptServer.User;

public class CommandStop extends Command{

    public CommandStop(){
        super("stop", "/stop", 0, 3);
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
