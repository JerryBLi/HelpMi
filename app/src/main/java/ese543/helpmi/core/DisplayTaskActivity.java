package ese543.helpmi.core;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.annotation.Nullable;

import ese543.helpmi.R;
import ese543.helpmi.fragments.UserListAdapter;

public class DisplayTaskActivity extends AppCompatActivity {

    private static final String TAG = DisplayTaskActivity.class.getName();
    private User user;
    private UserTask t;
    private EditText editTextTitleDisplayTask;
    private EditText editTextTaskCreatorDisplayTask;
    private EditText editTextDatePostedDisplayTask;
    private EditText editTextDateOfDeliveryDisplayTask;
    private EditText editTextLocationDisplayTask;
    private EditText editTextPaymentDisplayTask;
    private CheckBox checkBoxPaymentNegDisplayTask;
    private EditText editTextTaskDescriptionDisplayTask;
    private CheckBox checkBoxIsTaskCompleteDisplayTask;
    private EditText editTextTaskAssignedToDisplayTask;

    private TextView textViewInterestedUsers;

    private ImageButton buttonShowLocationDisplayTask;
    private Button buttonInterested;
    private Button buttonTaskCompleteDisplayTask;
    private Button buttonMessagePosterDisplayTask;
    private ImageButton imageButtonViewImages;


    //private ArrayList<User> interestedUsers = new ArrayList<>();
    private ArrayList<String> interestedUsers = new ArrayList<>();
    private ListView listViewInterestedUsers;
    private UserListAdapter userListAdapter;

    private ArrayList<File> pictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);

        //Initialize all the components
        editTextTitleDisplayTask = findViewById(R.id.editTextTitleDisplayTask);
        editTextTaskCreatorDisplayTask = findViewById(R.id.editTextTaskCreatorDisplayTask);
        editTextDatePostedDisplayTask= findViewById(R.id.editTextDatePostedDisplayTask);
        editTextDateOfDeliveryDisplayTask = findViewById(R.id.editTextDateOfDeliveryDisplayTask);
        editTextLocationDisplayTask = findViewById(R.id.editTextLocationDisplayTask);
        editTextPaymentDisplayTask = findViewById(R.id.editTextPaymentDisplayTask);
        checkBoxPaymentNegDisplayTask = findViewById(R.id.checkBoxPaymentNegDisplayTask);
        editTextTaskDescriptionDisplayTask = findViewById(R.id.editTextTaskDescriptionDisplayTask);
        checkBoxIsTaskCompleteDisplayTask = findViewById(R.id.checkBoxIsTaskCompleteDisplayTask);
        editTextTaskAssignedToDisplayTask = findViewById(R.id.editTextTaskAssignedToDisplayTask);

        buttonShowLocationDisplayTask = findViewById(R.id.buttonShowLocationDisplayTask);
        buttonTaskCompleteDisplayTask = findViewById(R.id.buttonTaskCompleteDisplayTask);
        buttonMessagePosterDisplayTask = findViewById(R.id.buttonMessagePosterDisplayTask);
        buttonInterested = findViewById(R.id.buttonInterested);
        textViewInterestedUsers = findViewById(R.id.textViewInterestedUsers);
        listViewInterestedUsers = findViewById(R.id.listViewInterestedUsers);

        imageButtonViewImages = findViewById(R.id.imageButtonViewImages);

        buttonTaskCompleteDisplayTask.setVisibility(View.GONE);
        buttonMessagePosterDisplayTask.setVisibility(View.GONE);
        buttonInterested.setVisibility(View.GONE);
        listViewInterestedUsers.setVisibility(View.GONE);
        textViewInterestedUsers.setVisibility(View.GONE);
        Intent i = getIntent();

        t = (UserTask)i.getParcelableExtra("task");
        if(t.getNumImages() == 0)
            imageButtonViewImages.setVisibility(View.GONE);
        Log.d(TAG, "taskID: " + t.getTaskID() + " title: " + t.getTitle());


        user = (User)i.getParcelableExtra("currentUser");
        Log.d(TAG, "userID: " + user.getUserID() + " userName: " + user.getUserName());


        //TODO - populate task
        populateFields();
        getPictures();
    }

    public void onClickShowLocation(View view)
    {
        //TODO - show the location of task on map
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("TASK_TITLE", editTextTitleDisplayTask.getText().toString());
        startActivity(i);
    }

    public void onClickTaskComplete(View view)
    {
        //TODO - mark the task complete
    }

    public void onClickMessagePoster(View view)
    {
        //TODO - message the poster
    }

    public void viewImages(View view)
    {
        pictures.size();
        return;
    }

    private void getPictures()
    {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        for(int i =0; i < t.getNumImages(); i++)
        {
            try
            {
                StorageReference ref = storageRef.child(t.getUserOwner()+"-"+t.getTitle() + i);
                final File localFile = File.createTempFile("images", "jpg");
                ref.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                pictures.add(localFile);
                            }}
                        );

            }catch(Exception e)
            {

            }
        }
    }
    public void onClickInterested(View view)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference  tasksRef = db.collection("tasks").document(t.getTaskID());
        // Add a new document with a generated ID
        Log.d(TAG, "onClickInterested..." + t.getTaskID() + " " + t.getTitle() + " " + t.getUserOwner() );
        Log.d(TAG, "onClickInterested..."+ user.getUserName() + " " + user.getEmail() + " " +user.getFirstName() + " " + user.getLastName());
        User currentUser = new User(user.getUserName(), user.getEmail(), user.getFirstName(), user.getLastName());
        currentUser.setUserID(user.getUserID());

        tasksRef.update("interestedUsers", FieldValue.arrayUnion(currentUser.getUserName()));

        Toast toast = Toast.makeText(getApplicationContext(), t.getUserOwner() + " has been notified of your interest!",Toast.LENGTH_SHORT);
        toast.show();
    }

    private void populateFields()
    {
        if(t == null)
            return;

        editTextTitleDisplayTask.setText(t.getTitle());
        editTextTaskCreatorDisplayTask.setText(t.getUserOwner());
        editTextDatePostedDisplayTask.setText(t.getDatePosted().toString());
        editTextDateOfDeliveryDisplayTask.setText(t.getDeliveryDate().toString());
        editTextLocationDisplayTask.setText(t.getLongitude()+","+t.getLatitude());
        editTextPaymentDisplayTask.setText(t.getPayment()+"");
        checkBoxPaymentNegDisplayTask.setChecked(t.getIsNegotiable());
        editTextTaskDescriptionDisplayTask.setText(t.getDescription());
        checkBoxIsTaskCompleteDisplayTask.setChecked(t.getIsComplete());
        editTextTaskAssignedToDisplayTask.setText(t.getUserAssigned());

        userListAdapter = new UserListAdapter(DisplayTaskActivity.this, interestedUsers);
        listViewInterestedUsers.setAdapter(userListAdapter);
        listViewInterestedUsers.setClickable(true);
        listViewInterestedUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                String userName = listViewInterestedUsers.getItemAtPosition(position).toString();

                Intent i = new Intent(DisplayTaskActivity.this, ChatActivity.class);
                i.putExtra("userNameFrom",user.getUserName());
                i.putExtra("userNameTo", userName);
                i.putExtra("task",t);
                startActivity(i);

            }
        });

        String currentUser = user.getUserName();
        String poster = t.getUserOwner();
        //If the current user is the poster, he can mark the task complete
        //otherwise, it's someone else so they can message the poster
        if(currentUser.equals(poster))
        {

            buttonMessagePosterDisplayTask.setVisibility(View.GONE);
            buttonInterested.setVisibility(View.GONE);
            buttonTaskCompleteDisplayTask.setVisibility(View.VISIBLE);
            textViewInterestedUsers.setVisibility(View.VISIBLE);
            listViewInterestedUsers.setVisibility(View.VISIBLE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference tasksRef = db.collection("tasks");

            tasksRef.document(t.getTaskID());


            tasksRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    interestedUsers.clear();
                    for (QueryDocumentSnapshot qds : querySnapshot) {

                        Log.d(TAG, "onEventListener...id: " + qds.getId() + " title: " + qds.getString("title") + " " + t.getUserOwner() );
                        // only the current task
                        if (qds.getId().equals(t.getTaskID())) {

                            // iterate through this task's interestedUsers list and add to ArrayList
                            ArrayList<String> taskList = (ArrayList<String>) qds.get("interestedUsers");
                            for (String interestedUser : taskList) {
                                interestedUsers.add(interestedUser);
                            }
                        }
                        // update adapter
                        userListAdapter.notifyDataSetChanged();
                    }
                }
            });


        }
        else // not the user that posted
        {
            buttonInterested.setVisibility(View.VISIBLE);
            buttonMessagePosterDisplayTask.setVisibility(View.VISIBLE);
            buttonTaskCompleteDisplayTask.setVisibility(View.GONE);
            textViewInterestedUsers.setVisibility(View.GONE);
            listViewInterestedUsers.setVisibility(View.GONE);
        }
    }


}
