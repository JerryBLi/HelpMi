package ese543.helpmi.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

import java.util.Calendar;
import java.util.Date;

import ese543.helpmi.R;
import ese543.helpmi.fragments.MessagesFragment;
import ese543.helpmi.fragments.NewTaskFragment;
import ese543.helpmi.fragments.TaskFragment;
import ese543.helpmi.fragments.dummy.DummyContent;

public class MainPage extends AppCompatActivity implements MessagesFragment.OnListFragmentInteractionListener, NewTaskFragment.OnFragmentInteractionListener, TaskFragment.OnListFragmentInteractionListener{


    private static final String TAG = MainPage.class.getName();

    private ActionBar toolbar;
    private Button buttonShowMaps;
    //editTextTitle
    //editTextDateDeliver
    //editTextLocation
    //checkBoxCurrentPosition
    //editTextPayment
    //checkBoxNegotiable
    //editTextDescription
    private EditText editTextTite;
    private EditText editTextDateDeliver;
    private EditText editTextLocation;
    private EditText editTextPayment;
    private EditText editTextDescription;

    private String userName;

    final Calendar myCalendar = Calendar.getInstance();

    //This is for the navigation bar. This chooses which fragment to do stuff
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_ListView:
                    buttonShowMaps.setVisibility(View.VISIBLE);
                    toolbar.setTitle("Task List");
                    loadFragment(new TaskFragment());
                    return true;
                case R.id.navigation_Messages:
                    buttonShowMaps.setVisibility(View.GONE);
                    toolbar.setTitle("My Messages");
                    loadFragment(new MessagesFragment());
                    return true;
                case R.id.nagivation_NewPost:
                    buttonShowMaps.setVisibility(View.GONE);
                    toolbar.setTitle("New Task");
                    loadFragment(new NewTaskFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        userName = getIntent().getExtras().get("userName").toString();

        Log.d(TAG, "onCreate..userName: " + userName);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        buttonShowMaps = findViewById(R.id.buttonShowMaps);
        toolbar = getSupportActionBar();
        toolbar.setTitle("Task Map");
        loadFragment(new TaskFragment());
    }

    public void openMap(View view) {
        TaskFragment.printUserTaskList();
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    private void loadFragment(Fragment fragment) {

        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void submitNewTask(View view)
    {
        createNewTask();
        TextView tv = findViewById(R.id.textViewNewTask);
        tv.setText("Hello!!!!");
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    /* **********************************************************************
    *  **********************************************************************
    *  **********************************************************************
    *  THIS PART IS DEDICATED TO THE NEW TASK FRAGMENT. ALL METHODS UNDER HERE
    *  HELP WITH CREATING A NEW TASK.
    *  **********************************************************************
    *  **********************************************************************
    *  **********************************************************************
     */


    /* METHOD TO CREATE NEW TASK */
    public void createNewTask()
    {

        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextDateDeliver = findViewById(R.id.editTextDateDeliver);
        CheckBox checkBoxCurrentPosition = findViewById(R.id.checkBoxCurrentPosition);
        if(checkBoxCurrentPosition.isChecked())
        {

        }
        else
        {
            EditText editTextLocation = findViewById(R.id.editTextLocation);
        }
        EditText editTextPayment = findViewById(R.id.editTextPayment);
        CheckBox checkBoxNegotiable = findViewById(R.id.checkBoxNegotiable);
        EditText editTextDescription = findViewById(R.id.editTextDescription);

        String title = editTextTitle.getText().toString();
        Date date = getDateFromInput(editTextDateDeliver.getText().toString());
        double latitude = getLatitudeFromInput(""); //TODO
        double longitude = getLongitudeFromInput(""); //TODO
        double payment = 0;
        try{
            payment = Double.parseDouble(editTextPayment.getText().toString());
        }catch(Exception e)
        {

        }
        boolean isNegotiable = checkBoxNegotiable.isChecked();
        String description = editTextDescription.getText().toString();

        UserTask task = new UserTask(userName,title,date,latitude,longitude,payment,isNegotiable,description);
        task.uploadToDatabase();

        //set to home after user creates new post
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_ListView);
    }

    //if user click on the checkbox, disable the EditText for Location
    public void onCurrentPositionCheckboxClicked(View view)
    {
        EditText editTextLocation = findViewById(R.id.editTextLocation);
        boolean checked = ((CheckBox) view).isChecked();
        if(checked)
            editTextLocation.setEnabled(false);
        else
            editTextLocation.setEnabled(true);
    }

    public Date getDateFromInput(String s)
    {
        myCalendar.set(2019,5,4);
        return myCalendar.getTime();
        //TODO
    }
    public double getLongitudeFromInput(String s)
    {
        return 10;
        //TODO
    }
    public double getLatitudeFromInput(String s)
    {
        return 15;
        //TODO
    }

    @Override
    public void onListFragmentInteraction(UserTask item) {

        return;
    }
}
