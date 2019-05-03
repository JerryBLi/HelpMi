package ese543.helpmi.startup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ese543.helpmi.R;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private TextView textViewErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewErrorMsg = (TextView)findViewById(R.id.textViewErrorMsg);
    }

    public void onclick_login(View view)
    {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        if(isInputFieldsValid(username, password))
        {
            //TODO
            //connect to DB and check if user exists. If exists, populate the user field
        }
        else
        {
            textViewErrorMsg.setText("Error: Invalid Username or Password!");
        }

    }

    public void onclick_createUser(View view)
    {
        Intent createUserIntent = new Intent(this,CreateNewUserActivity.class);
        startActivity(createUserIntent);
    }

    private boolean isInputFieldsValid(String username, String password)
    {
        boolean valid = true;
        if(username == null || password == null)
            return false;

        return valid;
    }
}
