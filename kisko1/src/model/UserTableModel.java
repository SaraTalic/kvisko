package model;

// For User Table in admin panel
public class UserTableModel {
    private String username;
    private String name;
    private String surname;
    private String email;
    private boolean isAdmin;
    private boolean isSuspended;

    public UserTableModel(String username, String name, String surname, String email, boolean isAdmin,
            boolean isSuspended) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isAdmin = isAdmin;
        this.isSuspended = isSuspended;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public boolean getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

}
