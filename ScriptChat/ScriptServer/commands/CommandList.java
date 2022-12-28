package ScriptServer.commands;

import ScriptServer.Security;
import ScriptServer.User;

public class CommandList extends Command {
    public CommandList() {
        super("list", "/list", 0, Security.MEMBER);
    }

    @Override
    public boolean execute(User user, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        user.sendLog(user.getServer().getUserNames());
        return true;
    }
}
