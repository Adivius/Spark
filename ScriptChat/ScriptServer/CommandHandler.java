package ScriptServer;

import ScriptServer.packets.PacketMessage;

import java.util.Arrays;

public class CommandHandler {

    public void handleCommand(User sender, String command, ScriptServer server, String[] args) {
        switch (command) {
            case "listuser":
                if (!Security.hasPermission(sender, Security.MEMBER)) {
                    notAllowed(sender);
                    return;
                }
                server.sendMessage(sender, server.getUserNames());
                break;
            case "quit":
                if (!Security.hasPermission(sender, Security.VISITOR)) {
                    notAllowed(sender);
                    return;
                }
                server.removeUserById(sender.getUserId(), ": Quit");

                break;
            case "kick":
                if (!Security.hasPermission(sender, Security.ADMIN)) {
                    notAllowed(sender);
                    return;
                }
                if (args.length == 0) {
                    server.sendMessage(sender, "Please enter a user!");
                    return;
                }
                if (!server.hasUserByName(args[0])) {
                    server.sendMessage(sender, "User " + args[0] + " is not online!");
                    return;
                }
                server.removeUserByName(args[0], ": Kicked by Admin");
                break;
            case "kickall":
                if (!Security.hasPermission(sender, Security.ADMIN)) {
                    notAllowed(sender);
                    return;
                }
                server.removeUsers(sender, ": Kicked by Admin");
                break;

            case "stop":
                if (!Security.hasPermission(sender, Security.OPERATOR)) {
                    notAllowed(sender);
                    return;
                }
                server.shutdown();
                break;
            case "msg":
                if (!Security.hasPermission(sender, Security.MEMBER)) {
                    notAllowed(sender);
                    return;
                }
                if (args.length < 2) {
                    server.sendMessage(sender, "Please enter a user and a message!");
                    return;
                }
                if (!server.hasUserByName(args[0])) {
                    server.sendMessage(sender, "User " + args[0] + " is not online!");
                    return;
                }

                User recipient = server.getUserByName(args[0]);
                String[] message = Arrays.copyOfRange(args, 1, args.length);
                server.sendMessage(recipient, sender.getUserName() + " whispers: " + String.join(" ", message));
                break;
            case "kartoffel":
                server.sendPacket(sender, new PacketMessage("Es sind " + server.getUserCount() + " Kartoffeln online"));
                break;
            case "setlevel":
                if (!Security.hasPermission(sender, Security.ADMIN)) {
                    notAllowed(sender);
                    return;
                }
                if (args.length < 2) {
                    server.sendMessage(sender, "Please enter a user and a level!");
                    return;
                }
                if (!server.hasUserByName(args[0])) {
                    server.sendMessage(sender, "User " + args[0] + " is not online!");
                    return;
                }
                if (!Security.isInt(args[1])) {
                    server.sendMessage(sender, "Please enter a valid level!");
                    return;
                }
                int level = Integer.parseInt(args[1]);
                User change = server.getUserByName(args[0]);
                if (sender.getSecurityLevel() < level) {
                    notAllowed(sender);
                    return;
                }
                change.setSecurityLevel(level);
                server.sendMessage(change, "Your security level was set to " + level);
                server.sendMessage(sender, " Security level of " + change.getUserName() + " was set to " + level);
                break;
            default:
                server.sendMessage(sender, "Command " + command + " was invalid!");
                break;
        }
    }

    public void notAllowed(User user) {
        user.getServer().sendMessage(user, "You don't have the permission to do that!");
    }

}
