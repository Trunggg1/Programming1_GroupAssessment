package User;

import User.abstracts.User;

public class PortManager extends User {
    public PortManager(String username, String password) {
        super(username, password, "portManager");
    }
}