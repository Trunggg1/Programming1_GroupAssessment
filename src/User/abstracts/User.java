package User.abstracts;

public abstract class User {
    public static final String usersFilePath = "./src/database/portManagers.txt";
    private String type;
    private String username;
    private String password;

    public User(String username, String password, String type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }
    public void changePassword(){
        //Create a menu to ask to input password
        //Change the password on database
        //Update the password: this.password =
        //Println new Username and Password
    }
    public void changeUsername(){
        //Create a menu to ask to input password
        //Change the password on database
        //Update the password: this.username =
        //Println new Username and Password
    }
    public void handleProfileOptions(String option) {
        switch (option){
            case "Change username"->{
               changeUsername();
            }
            case "Change password"->{
                changePassword();
            }
        }
    }
    public void showProfile(){
        System.out.println("Username: " + username);
        System.out.println("Password: " +  password);
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
