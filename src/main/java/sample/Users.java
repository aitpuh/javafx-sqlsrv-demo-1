package sample;

public class Users {
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int user_id;
    private String name;

    public String toString() {
        return String.format("%s", name);
    }
}
