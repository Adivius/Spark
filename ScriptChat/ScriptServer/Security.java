package ScriptServer;

public abstract class Security {
    public static int VISITOR = 0, MEMBER = 1, ADMIN = 2, OPERATOR = 3;

    public static boolean hasPermission(User user, int minSecurityLevel){
        return user.getSecurityLevel() >= minSecurityLevel;
    }
    public static boolean isInt(String str) {
        try {
            int x = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
