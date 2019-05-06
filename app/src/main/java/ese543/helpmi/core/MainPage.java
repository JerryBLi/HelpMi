package ese543.helpmi.core;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ese543.helpmi.R;
import ese543.helpmi.fragments.DatePickerFragment;
import ese543.helpmi.fragments.MessagesFragment;
import ese543.helpmi.fragments.NewTaskFragment;
import ese543.helpmi.fragments.TaskFragment;
import ese543.helpmi.fragments.dummy.DummyContent;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainPage extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener, MessagesFragment.OnListFragmentInteractionListener, NewTaskFragment.OnFragmentInteractionListener, TaskFragment.OnListFragmentInteractionListener{


    private static final String TAG = MainPage.class.getName();
    private final int REQUEST_LOCATION_PERMISSION = 1;
    public static final int PICK_IMAGE = 2;
    private ArrayList<Uri> images = new ArrayList<>();

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
    private EditText editTextImages;

    private String userName;
    private User user;
    private UserTask task;
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
        user = (User)getIntent().getParcelableExtra("user");

        Log.d(TAG, "onCreate..userID: " + user.getUserID() + "userName: " + userName);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        buttonShowMaps = findViewById(R.id.buttonShowMaps);
        toolbar = getSupportActionBar();
        toolbar.setTitle("Task Map");
        loadFragment(new TaskFragment());

        requestLocationPermission();
    }

    public void openDatePicker(View v){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.editTextDateDeliver);
        textView.setText(currentDateString);
    }


    public void openMap(View view) {
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

    public void uploadImages(View view)
    {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {

            ClipData clip = data.getClipData();
            ArrayList<Uri> uris = new ArrayList<>();
            if(clip == null)
            {
                uris.add(data.getData());
            }
            else if (clip.getItemCount() > 0) {

                for (int i = 0; i < clip.getItemCount(); i++) {
                    ClipData.Item item = clip.getItemAt(i);
                    Uri uri = item.getUri();
                    uris.add(uri);
                }


            }
            editTextImages = findViewById(R.id.editTextImages);
            images = uris;
            if(images != null)
            {
                StringBuilder sb = new StringBuilder();
                for(Uri uri : images)
                {
                    String result = uri.getLastPathSegment();
                    sb.append(result +"\n");
                }
                editTextImages.setText(sb.toString());
            }

            return;
        }
    }

    /* METHOD TO CREATE NEW TASK */
    public void createNewTask()
    {
        double latitude;
        double longitude;
        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextDateDeliver = findViewById(R.id.editTextDateDeliver);
        CheckBox checkBoxCurrentPosition = findViewById(R.id.checkBoxCurrentPosition);
        if(checkBoxCurrentPosition.isChecked())
        {
            LatLng currentLocation = getCurrentLocation();
            if(currentLocation != null)
            {
                latitude = currentLocation.latitude;
                longitude = currentLocation.longitude;
            }
            else
            {
                latitude = 0; //default if can't find position
                longitude = 0; //defaut if can't find position
            }
        }
        else
        {
            EditText editTextLocation = findViewById(R.id.editTextLocation);
            latitude = getLatitudeFromInput(""); //TODO
            longitude = getLongitudeFromInput(""); //TODO
        }
        EditText editTextPayment = findViewById(R.id.editTextPayment);
        CheckBox checkBoxNegotiable = findViewById(R.id.checkBoxNegotiable);
        EditText editTextDescription = findViewById(R.id.editTextDescription);

        String title = editTextTitle.getText().toString();
        //Date date = getDateFromInput(editTextDateDeliver.getText().toString());
        Date deliverDate = getDateFromInput(editTextDateDeliver.getText().toString());

        double payment = 0;
        try{
            payment = Double.parseDouble(editTextPayment.getText().toString());
        }catch(Exception e)
        {

        }
        boolean isNegotiable = checkBoxNegotiable.isChecked();
        String description = editTextDescription.getText().toString();

        task = new UserTask(user.getUserName(),title,new Date(),deliverDate,latitude,longitude,payment,isNegotiable,description);
        task.setNumImages(images.size());
        uploadImages(task.getUserOwner(),task.getTitle());
        task.uploadToDatabase();



        Toast toast = Toast.makeText(this, "Successfully added task!",Toast.LENGTH_SHORT);
        toast.show();

        Log.d(TAG, task.getTaskID() + " " + task.getTitle() + " " + task.getUserOwner() );
        //set to home after user creates new post
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_ListView);
    }

    private void uploadImages(String userName, String userTask)
    {
        if(images != null)
        {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            int counter = 0;
            for(Uri uri : images)
            {

                StorageReference riversRef = storageRef.child(userName+"-"+userTask + counter);
                riversRef.putFile(uri)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.d(TAG,"Image upload failed" );
                            }
                        });;
                counter++;

            }
            images = null;
        }
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

    public LatLng getCurrentLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location == null) {

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng latLng = new LatLng(latitude, longitude);

        return latLng;

    }

    @Override
    public void onListFragmentInteraction(UserTask task) {
        Intent i = new Intent(this,DisplayTaskActivity.class);

        i.putExtra("currentUser",user);
        i.putExtra("task",task);

        Log.d(TAG, "onListFragmentInteraction..." + task.getTaskID() + " " + task.getTitle() + " " + task.getUserOwner());
        Log.d(TAG, "onListFragmentInteraction..." + user.getUserID() + " " + user.getUserName() + " " + user.getEmail());

        startActivity(i);
        return;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            //Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

}
