package ese543.helpmi;

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
        if(authenticateFields())
        {
            //TODO
            //Do database stuff
        }

    }

    private boolean authenticateFields()
    {
        textViewError.setText("");
        StringBuilder sb = new StringBuilder();
        boolean valid = true;
        sb.append("Error:");
        sb.append("\n");
        //Authenticate username
        if(editTextFirstName.getText().toString().equals(""))
            sb.append("Username Not filled in!\n");
        //TODO - make sure username is unique

        //Authenticate email
        String email = editTextEmail.getText().toString();
        if(!email.contains("@") || !email.contains("."))
            sb.append("Invalid Email!\n");
        //TODO - make sure email is unique

        //Authenticate First Name
        if(editTextFirstName.getText().toString().equals(""))
            sb.append("First Name Not filled in!\n");
        //Authenticate Last Name
        if(editTextLastName.getText().toString().equals(""))
            sb.append("Last Name Not filled in!\n");
        //Authenticate Password
        String p1 = editTextPassword.getText().toString();
        String p2 = editTextPassword2.getText().toString();

        if(!p1.equals(p2))
           sb.append("Passwords do not match!\n");

        if(p1.length() <= 4)
            sb.append("Password too short!\n");

        if(!valid)
            textViewError.setText(sb.toString())
                    ;
        return valid;

    }

}
