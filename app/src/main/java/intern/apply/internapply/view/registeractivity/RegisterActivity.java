package intern.apply.internapply.view.registeractivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import intern.apply.internapply.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText passwordConfirm;
    private EditText email;
    private EditText emailConfirm;

    private void onInit(){
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        email = findViewById(R.id.email);
        emailConfirm = findViewById(R.id.emailConfirm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        onInit();
    }

    /**
     * Send a new user to the server
     *
     */
    public void addUser(){

    }
}
