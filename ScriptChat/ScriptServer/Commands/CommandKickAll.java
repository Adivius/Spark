package ScriptServer.Commands;

import ScriptServer.ScriptServer;
import ScriptServer.User;

public class CommandKickAll extends Command{

    public CommandKickAll(){
        super("kickall", "/kickall", 0, 3);
    }

    @Override
    public boolean execute(User user, ScriptServer server, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        for (String id : server.getUsers().keySet()){
            if (!(id.equals(user.getUserId()))){
                server.removeUserById(id, ": Kicked by Admin");
            }
        }
        return true;
    }
}
