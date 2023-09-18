package users;

import Resources.TableInterface;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Account {
    // Attributes
    private String cID;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String customerType;
    private String username;
    private String password;
    private Long totalSpending;
    private Long totalPoint;

    // Constructor
    public Account(String cID, String name, String email, String address, String phone,
                   String customerType, String userName, String password, Long totalSpending, Long totalPoint) {
        this.cID = cID;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.customerType = customerType;
        this.username = userName;
        this.password = password;
        this.totalSpending = totalSpending;
        this.totalPoint = totalPoint;
    }

    // Constructor
    public Account() {
    }


    // Hashing the customer's password
    public String hashing(String password) {
        try {
            // MessageDigest instance for MD5.
            MessageDigest m = MessageDigest.getInstance("MD5");

            // Add plain-text password bytes to digest using MD5 update() method.
            m.update(password.getBytes());

            // Convert the hash value into bytes
            byte[] bytes = m.digest();

            // The bytes array has bytes in decimal form. Converting it into hexadecimal format.
            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            // Complete hashed password in hexadecimal format
            return s.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
