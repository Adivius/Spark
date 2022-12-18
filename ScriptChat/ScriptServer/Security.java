package ScriptServer;

public abstract class Security {
    public static int VISITOR = 0, MEMBER = 1, ADMIN = 2, OPERATOR = 3;

    public static boolean hasPermission(User user, int minSecurityLevel){
        return user.getSecurityLevel() >= minSecurityLevel;
    }
}
