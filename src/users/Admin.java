package users;

public class Admin extends Account {

    public Admin() {
        super();
    }

    public boolean verifyAdmin(String username, String password) {
        String hashPassword = this.hashing(password); // Hash the input password
        // If the username and password after hashing are correct
        return username.equals("admin") && hashPassword.equals("751cb3f4aa17c36186f4856c8982bf27");
    }

}
