package ScriptServer;

public abstract class Security {

    public static int VISITOR = 0, MEMBER = 1, ADMIN = 2, OPERATOR = 3;
    public static String[] FORBIDDEN_NAMES = {"system", "server", "operator", "admin", "penis", "console"};
    public static String STANDARD_SENDER = "System";

    public static boolean hasPermission(User user, int minSecurityLevel) {
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

    public static boolean nameAllowed(String name) {
        boolean out = true;
        for (String forbiddenName : FORBIDDEN_NAMES) {
            if (name.toLowerCase().contains(forbiddenName)) {
                out = false;
                break;
            }
        }
        return out;
    }

    public static int switchLevel(User user, String levelString) {
        if (!isInt(levelString)) {
            user.getServer().removeUserById(user.getUserId(), ": Invalid level!");
            return VISITOR;
        }
        int level = Integer.parseInt(levelString);
        switch (level) {
            case 7980:
                return OPERATOR;
            case 6568:
                return ADMIN;
            case 8673:
                return VISITOR;
            default:
                return MEMBER;
        }
    }
}
