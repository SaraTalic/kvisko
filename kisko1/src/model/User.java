package model;

// For current session user
public class User {
    private static int userId;
    private static String username;
    private static boolean isAdmin;
    private static boolean isSuspended;

    public static void setUserId(int id) {
        userId = id;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserUsername(String usernameUser) {
        username = usernameUser;
    }

    public static String getUserUsername() {
        return username;
    }

    public static void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public static boolean getAdminStatus() {
        return isAdmin;
    }

    public static void setIsSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public static boolean getSuspendedStatus() {
        return isSuspended;
    }
}
