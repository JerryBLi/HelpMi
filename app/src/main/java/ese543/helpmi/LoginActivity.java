package ese543.helpmi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import ese543.helpmi.core.MainPage;
import ese543.helpmi.core.User;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();

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

    public void login(View view)
    {
        final String userName = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();
        if(isInputFieldsValid(userName, password))
        {
            //connect to DB and check if user exists. If exists, login the user field
            final User user = new User(userName, password);
            user.loginUser(new User.UserLogin() {
                @Override
                public void onCallback(boolean exists) {

                    Log.d(TAG, "onCallback...userExists:" + exists);
                    if(exists){
                        Log.d(TAG, "onCallback...userExists if:" + exists);
                        Toast toast = Toast.makeText(getApplicationContext(),"Logged in as " + userName,Toast.LENGTH_SHORT);
                        toast.show();

                        Intent i = new Intent(LoginActivity.this, MainPage.class);
                        i.putExtra("userName", userName);
                        i.putExtra("user",user);
                        startActivity(i);
                    }
                    else{
                        Log.d(TAG, "onCallback...userExists else:" + exists);
                        Toast toast = Toast.makeText(getApplicationContext(),"Username or Password is incorrect",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });



        }
        else
        {
            textViewErrorMsg.setText("Error: Invalid Username or Password!");
        }

    }

    public void createUser(View view)
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
