package ese543.helpmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean loggedIn = false;
        if(loggedIn)
        {
        }
        else
        {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);

        }

    }

}
