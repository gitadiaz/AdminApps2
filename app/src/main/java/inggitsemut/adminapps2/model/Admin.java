package inggitsemut.adminapps2.model;

public class Admin {

    private int id;
    private String name;
    private String user_email;
    private String user_password;

    public Admin(int id, String name, String user_email, String user_password) {
        this.id = id;
        this.name = name;
        this.user_email = user_email;
        this.user_password = user_password;
    }

    public Admin(int id, String user_email, String user_password) {
        this.id = id;
        this.user_email = user_email;
        this.user_password = user_password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_password() {
        return user_password;
    }
}
