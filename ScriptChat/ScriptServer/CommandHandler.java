package ScriptServer;

import ScriptServer.commands.*;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    public static HashMap<String, Command> commands = new HashMap<>();

    public static void init() {
        registerCommand(new CommandHelp());
        registerCommand(new CommandList());
        registerCommand(new CommandGetLevel());
        registerCommand(new CommandSetLevel());
        registerCommand(new CommandQuit());
        registerCommand(new CommandStop());
        registerCommand(new CommandMsg());
        registerCommand(new CommandKick());
        registerCommand(new CommandKickAll());
        registerCommand(new CommandName());
    }

    public static void registerCommand(Command command) {
        commands.put(command.NAME, command);
    }

    public static void handleCommand(User sender, String commandName, String[] args) {
        if (commands.containsKey(commandName)) {
            try {
                commands.get(commandName).execute(sender, args);
            } catch (Exception ex) {
                sender.sendLog("Command " + commandName + " can't be executed!");
                ex.printStackTrace();
            }
        } else {
            sender.sendLog("Command " + commandName + " was invalid!");
        }
    }

    public static String getHelp() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Command> commandEntry : commands.entrySet()) {
            stringBuilder.append("\n").append(commandEntry.getValue().USAGE);
        }
        return stringBuilder.toString();
    }
}
