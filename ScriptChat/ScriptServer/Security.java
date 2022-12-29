package ScriptServer;

public abstract class Security {

    public static final int VISITOR = 0, MEMBER = 1, ADMIN = 2, OPERATOR = 3;
    public static final String[] FORBIDDEN_NAMES = {"system", "server", "operator", "admin", "penis", "console"};
    public static final int NAME_MAX_LENGTH = 12;

    public static boolean hasPermission(User user, int minSecurityLevel) {
        return user.getSecurityLevel() >= minSecurityLevel;
    }

    public static boolean isInvalidInt(String str) {
        try {
            Integer.parseInt(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static boolean nameDenied(String name) {
        boolean out = false;
        for (String forbiddenName : FORBIDDEN_NAMES) {
            if (name.toLowerCase().contains(forbiddenName)) {
                out = true;
                break;
            }
        }
        return out;
    }

    public static int switchLevel(User user, String levelString) {
        if (isInvalidInt(levelString)) {
            user.getServer().removeUserById(user.getUserId(), "Invalid level!");
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
