package intern.apply.internapply.view.registeractivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText passwordConfirm;
    private EditText email;
    private EditText emailConfirm;
    private InternAPI api;

    private void onInit() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        email = findViewById(R.id.email);
        emailConfirm = findViewById(R.id.emailConfirm);
        api = InternAPI.getAPI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        onInit();
    }

    /**
     * Send a new user to the server
     */
    public void addUser(View view) {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();
        String passwordConfirm = this.passwordConfirm.getText().toString();
        String email = this.email.getText().toString();
        String emailConfirm = this.emailConfirm.getText().toString();


        User newUser = new User(username, password, passwordConfirm, email, emailConfirm);

        api.register(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Toast.makeText(this, "You were registered successfully", Toast.LENGTH_LONG).show();
                }, error -> {
                    Log.i("error", error.toString());
                    Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_LONG).show();
                });
    }
}
