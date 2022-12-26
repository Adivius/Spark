package ScriptServer.Commands;

import ScriptServer.ScriptServer;
import ScriptServer.User;
import ScriptServer.packets.PacketLog;

public class CommandListUser extends Command{
    public CommandListUser() {
        super("listuser", "/listuser", 0, 1);
    }

    @Override
    public boolean execute(User user, ScriptServer server, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        user.getServer().sendPacket(user, new PacketLog(server.getUserNames()));
        return true;
    }
}
