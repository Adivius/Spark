package ScriptServer;

import ScriptServer.commands.*;
import ScriptServer.packets.PacketLog;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    public static HashMap<String, Command> commands = new HashMap<>();

    public static void init() {
        registerCommand(new CommandHelp());
        registerCommand(new CommandListUser());
        registerCommand(new CommandGetLevel());
        registerCommand(new CommandSetLevel());
        registerCommand(new CommandQuit());
        registerCommand(new CommandStop());
        registerCommand(new CommandMsg());
        registerCommand(new CommandKick());
        registerCommand(new CommandKickAll());
    }

    public static void registerCommand(Command command) {
        commands.put(command.NAME, command);
    }

    public static void handleCommand(User sender, String commandName, ScriptServer server, String[] args) {
        if (commands.containsKey(commandName)) {
            try {
                commands.get(commandName).execute(sender, server, args);
            } catch (Exception ex) {
                server.sendPacket(sender, new PacketLog("Command " + commandName + " can't be executed!"));
                ex.printStackTrace();
            }
        } else {
            server.sendPacket(sender, new PacketLog("Command " + commandName + " was invalid!"));
        }
    }

    public static String getHelp() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Command> commandEntry : commands.entrySet()) {
            stringBuilder.append("*").append(commandEntry.getValue().USAGE);
        }
        return stringBuilder.toString();
    }
}
