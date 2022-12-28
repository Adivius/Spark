package ScriptServer.commands;

import ScriptServer.Security;
import ScriptServer.User;

import java.util.Arrays;

public class CommandMsg extends Command {

    public CommandMsg() {
        super("msg", "/msg <username> <message>", 2, Security.MEMBER);
    }

    @Override
    public boolean execute(User user, String[] args) {
        if (!hasPermission(user, SECURITY_LEVEL)) {
            notAllowed(user);
            return false;
        }
        if (args.length < ARGS_LENGTH) {
            user.sendLog("Please enter a user and a message!");
            return false;
        }
        if (!user.getServer().hasUserByName(args[0])) {
            user.sendLog("User " + args[0] + " is not online!");
            return false;
        }

        User recipient = user.getServer() .getUserByName(args[0]);
        String[] message = Arrays.copyOfRange(args, 1, args.length);
        recipient.sendMessage(user.getUserName() + " whispers: " + String.join(" ", message), "System");
        return true;
    }
}
