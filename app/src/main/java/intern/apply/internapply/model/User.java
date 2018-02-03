package intern.apply.internapply.model;


import org.json.JSONObject;

public class User {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String confirmEmail;

    public User(String username, String password, String confirmPassword, String email, String confirmEmail){
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.confirmEmail = confirmEmail;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getConfirmPassword() { return confirmPassword; }

    public String getEmail(){
        return email;
    }

    public String getConfirmEmail() { return confirmEmail; }

    public void setUsername(String newUsername){
        username = newUsername;
    }

    public void setPassword(String newPassword){
        password = newPassword;
    }

    public void setEmail(String newEmail){
        email = newEmail;
    }
}
