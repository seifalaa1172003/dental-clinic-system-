import java.util.List;

public abstract class User {
    // common attributes
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String mobileNumber;



    // super constructor for common data
    public User(String username, String password, String firstName, String lastName, String email, String mobileNumber) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    // the common function of login
    public static <T extends User> T login(String email, String password, List<T> userList) {
//        if (userList == null || userList.isEmpty()) {
//            System.out.println("No registered users found.");
//            return null;
//        }
        return userList.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email) &&
                        user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public abstract boolean signup();

    public abstract String toString();

}
