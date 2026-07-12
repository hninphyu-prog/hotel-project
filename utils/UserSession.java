// UserSession.java
package utils;

public class UserSession {
    private static int employeeId;
    private static String userName;
    private static String userRole;

    public static void setLoggedInUser(int id, String name, String role) {
        employeeId = id;
        userName = name;
        userRole = role;
    }

    public static int getEmployeeId() {
        return employeeId;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserRole() {
        return userRole;
    }

    public static void clearSession() {
        employeeId = 0;
        userName = null;
        userRole = null;
    }
}