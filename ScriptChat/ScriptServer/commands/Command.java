package ScriptServer.commands;

import ScriptServer.User;

public abstract class Command {
    public String USAGE;
    public String NAME;
    public int ARGS_LENGTH;
    public int SECURITY_LEVEL;


    public Command(String name, String usage, int argsLength, int securityLevel) {
        this.USAGE = usage;
        this.NAME = name;
        this.ARGS_LENGTH = argsLength;
        this.SECURITY_LEVEL = securityLevel;
    }

    public abstract boolean execute(User user, String[] args);

    public boolean hasPermission(User user, int minSecurityLevel){
        return minSecurityLevel <= user.getSecurityLevel();
    }

    public void notAllowed(User user) {
        user.sendLog("You don't have the permission to do that!");
    }
}
