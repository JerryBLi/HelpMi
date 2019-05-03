package ese543.helpmi.startup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ese543.helpmi.R;

public class CreateNewUserActivity extends AppCompatActivity {


    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPassword;
    private EditText editTextPassword2;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword2 = findViewById(R.id.editTextPassword2);
        textViewError = findViewById(R.id.textViewError);
    }

    public void createUser(View view)
    {

        //Authenticate the fields

        //TODO
        //Do database stuff
    }

    private boolean authenticateFields()
    {
        //Authenticate username

        //Authenticate email

        //Authenticate First Name

        //Authenticate Last Name

        //Authenticate Password
        String p1 = editTextPassword.getText().toString();
        String p2 = editTextPassword2.getText().toString();
        if(!p1.equals(p2) && p1.length() <= 4)
        {
            textViewError.setText("Error!");
        }
        return true;

    }

}
