package ese543.helpmi.core;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import ese543.helpmi.R;

public class DisplayTaskActivity extends AppCompatActivity {

    private EditText editTextTitleDisplayTask;
    private EditText editTextTaskCreatorDisplayTask;
    private EditText editTextDateOfDeliveryDisplayTask;
    private EditText editTextLocationDisplayTask;
    private EditText editTextPaymentDisplayTask;
    private CheckBox checkBoxPaymentNegDisplayTask;
    private EditText editTextTaskDescriptionDisplayTask;
    private CheckBox checkBoxIsTaskCompleteDisplayTask;
    private EditText editTextTaskAssignedToDisplayTask;

    private Button buttonShowLocationDisplayTask;
    private Button buttonTaskCompleteDisplayTask;
    private Button buttonMessagePosterDisplayTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);

        //Initialize all the components
        editTextTitleDisplayTask = findViewById(R.id.editTextTitleDisplayTask);
        editTextTaskCreatorDisplayTask = findViewById(R.id.editTextTaskCreatorDisplayTask);
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

        //If the current user is the poster, he can mark the task complete
        //otherwise, it's someone else so they can message the poster
        int currentUser = 1;
        int poster = 1;
        if(currentUser == poster /* TODO */)
        {
            buttonTaskCompleteDisplayTask.setVisibility(View.VISIBLE);
            buttonMessagePosterDisplayTask.setVisibility(View.GONE);
        }
        else
        {
            buttonTaskCompleteDisplayTask.setVisibility(View.GONE);
            buttonMessagePosterDisplayTask.setVisibility(View.VISIBLE);
        }
        Intent i = getIntent();
        UserTask t = (UserTask)i.getParcelableExtra("task");
        //TODO - populate task
        populateFields(t);
    }

    public void onClickShowLocation(View view)
    {
        //TODO - show the location of task on map
    }

    public void onClickTaskComplete(View view)
    {
        //TODO - mark the task complete
    }

    public void onClickMessagePoster(View view)
    {
        //TODO - message the poster
    }

    private void populateFields(UserTask t)
    {
        if(t == null)
            return;

        editTextTitleDisplayTask.setText(t.getTitle());
        editTextTaskCreatorDisplayTask.setText(t.getUserOwner());
        editTextDateOfDeliveryDisplayTask.setText(t.getDeliveryDate().toString());
        editTextLocationDisplayTask.setText(t.getLongitude()+","+t.getLatitude());
        editTextPaymentDisplayTask.setText(t.getPayment()+"");
        checkBoxPaymentNegDisplayTask.setChecked(t.getIsNegotiable());
        editTextTaskDescriptionDisplayTask.setText(t.getDescription());
        checkBoxIsTaskCompleteDisplayTask.setChecked(t.getIsComplete());
        editTextTaskAssignedToDisplayTask.setText(t.getUserAssigned());
    }
}
