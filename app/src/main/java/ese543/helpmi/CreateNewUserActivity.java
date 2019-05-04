package ese543.helpmi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import ese543.helpmi.R;
import ese543.helpmi.core.User;

public class CreateNewUserActivity extends AppCompatActivity {
    private static final String TAG = CreateNewUserActivity.class.getName();

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
            // Get User fields
            String userName = editTextUsername.getText().toString();
            String email = editTextEmail.getText().toString();
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            String password = editTextPassword.getText().toString();

            // Create User
            final User user = new User(userName, email, firstName, lastName, password);

            // Upload User to database
            user.checkUserExists(new User.UserAlreadyExists() {
                @Override
                public void onCallback(boolean exists) {

                    Log.d(TAG, "onCallback...userExists:" + exists);
                    if(exists){
                        Log.d(TAG, "onCallback...userExists else:" + exists);
                        Toast toast = Toast.makeText(getApplicationContext(),"Email already exists!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        Log.d(TAG, "onCallback...userExists if:" + exists);
                        user.uploadToDatabase();
                        Toast toast = Toast.makeText(getApplicationContext(),"Registered User!",Toast.LENGTH_SHORT);
                        toast.show();

                        Intent loginIntent = new Intent(CreateNewUserActivity.this,LoginActivity.class);
                        startActivity(loginIntent);

                    }

                }
            });


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
        {
            valid = false;
            sb.append("Username Not filled in!\n");
        }

        //TODO - make sure username is unique

        //Authenticate email
        String email = editTextEmail.getText().toString();
        if(!email.contains("@") || !email.contains("."))
        {
            valid = false;
            sb.append("Invalid Email!\n");

        }


        //TODO - make sure email is unique

        //Authenticate First Name
        if(editTextFirstName.getText().toString().equals(""))
        {
            valid = false;
            sb.append("First Name Not filled in!\n");
        }

        //Authenticate Last Name
        if(editTextLastName.getText().toString().equals(""))
        {
            valid = false;
            sb.append("Last Name Not filled in!\n");
        }

        //Authenticate Password
        String p1 = editTextPassword.getText().toString();
        String p2 = editTextPassword2.getText().toString();

        if(!p1.equals(p2))
        {
            valid = false;
            sb.append("Passwords do not match!\n");
        }


        if(p1.length() <= 4)
        {
            valid = false;
            sb.append("Password too short!\n");
        }


        if(!valid)
            textViewError.setText(sb.toString());
        return valid;

    }

}
