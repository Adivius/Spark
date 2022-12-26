package ScriptServer.Commands;

import ScriptServer.ScriptServer;
import ScriptServer.User;
import ScriptServer.packets.PacketLog;
import ScriptServer.packets.PacketMessage;

import java.util.Arrays;

public class CommandMsg extends Command{

    public CommandMsg(){
        super("msg", "/msg <name> <message>", 2, 1);
    }

    @Override
    public boolean execute(User user, ScriptServer server, String[] args) {
        if (!hasPermission(user, SECURITYLEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length < ARGSLENGHT) {
            server.sendPacket(user, new PacketLog("Please enter a user and a message!"));
            return false;
        }
        if (!server.hasUserByName(args[0])) {
            server.sendPacket(user, new PacketLog("User " + args[0] + " is not online!"));
            return false;
        }

        User recipient = server.getUserByName(args[0]);
        String[] message = Arrays.copyOfRange(args, 1, args.length);
        server.sendPacket(recipient, new PacketMessage(user.getUserName() + " whispers: " + String.join(" ", message), "System"));
        return true;
    }
}
