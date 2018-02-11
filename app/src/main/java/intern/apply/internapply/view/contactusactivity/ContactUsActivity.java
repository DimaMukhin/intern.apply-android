package intern.apply.internapply.view.contactusactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.ServerError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Contact us activity
 * contact display and message to developers form
 */

public class ContactUsActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etTitle;
    private EditText etMessage;
    private InternAPI api;

    private void onInit() {
        etEmail = findViewById(R.id.etEmail);
        etTitle = findViewById(R.id.etTitle);
        etMessage = findViewById(R.id.etMessage);
        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test)
            api = InternAPI.getAPI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        onInit();
    }

    public void setApi(InternAPI api) {
        this.api = api;
    }

    /**
     * Send message button click listener
     * sends a message to the server.
     * displays success/error messages
     */
    public void sendMessage(View view) {
        String email = etEmail.getText().toString();
        String title = etTitle.getText().toString();
        String message = etMessage.getText().toString();

        ContactMessage cm = new ContactMessage(email, title, message);
        api.sendContactMessage(cm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> Toast.makeText(this, "Message was sent successfully", Toast.LENGTH_LONG).show()
                        , error -> {
                            List<ServerError> errors = ServerError.getErrorsFromServerException(error);

                            if (errors.size() == 0 || errors.get(0).getCode() == 0)
                                Toast.makeText(this, "Internal server error, please try again later", Toast.LENGTH_LONG).show();
                            else {
                                if (errors.get(0).getCode() == 1)
                                    Toast.makeText(this, "Invalid email address", Toast.LENGTH_LONG).show();
                                else if (errors.get(0).getCode() == 2)
                                    Toast.makeText(this, "Invalid title (max 25 characters)", Toast.LENGTH_LONG).show();
                                else if (errors.get(0).getCode() == 3)
                                    Toast.makeText(this, "Invalid message body (max 300 characters)", Toast.LENGTH_LONG).show();
                            }
                        });
    }
}
