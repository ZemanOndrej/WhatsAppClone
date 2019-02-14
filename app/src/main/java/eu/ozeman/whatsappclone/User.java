package eu.ozeman.whatsappclone;

public class User {
    private String name, phone;

    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }


    public String getPhone() {
        return phone;
    }
}
