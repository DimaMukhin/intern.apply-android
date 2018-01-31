package intern.apply.internapply.model;


import org.json.JSONObject;

public class User {
    private String username;
    private String password;
    private String email;

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public void setUsername(String newUsername){
        username = newUsername;
    }

    public void setPassword(String newPassword){
        password = newPassword;
    }

    public void setEmail(String newEmail){
        email = newEmail;
    }

    /*
    public JSONObject getJSON(){
        retrun new JSONObject()
    }
    */
}
