package ScriptServer.commands;

import ScriptServer.ScriptServer;
import ScriptServer.Security;
import ScriptServer.User;
import ScriptServer.packets.PacketLog;

public class CommandKickAll extends Command {

    public CommandKickAll() {
        super("kickall", "/kickall", 0, Security.OPERATOR);
    }

    @Override
    public boolean execute(User user, ScriptServer server, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        try {
            for (String id : server.getUsers().keySet()) {
                if (!(id.equals(user.getUserId()))) {
                    server.removeUserById(id, ": Kicked by Admin");
                }
            }
            server.sendPacket(user, new PacketLog(""));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
