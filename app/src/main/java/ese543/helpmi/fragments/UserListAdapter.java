package ese543.helpmi.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ese543.helpmi.R;
import ese543.helpmi.core.User;

public class UserListAdapter extends ArrayAdapter<String> {


    private Activity context;
    private ArrayList<String> userArrayList;

    public UserListAdapter(Activity context, ArrayList<String> userArrayList) {

        super(context, R.layout.activity_display_task, userArrayList);
        this.context = context;
        this.userArrayList = userArrayList;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.user, null, true);


//        TextView textViewEmail = (TextView) rowView.findViewById(R.id.textViewEmail);
        TextView textViewUsername = (TextView) rowView.findViewById(R.id.textViewUsername);
//        TextView textViewFirstName = (TextView) rowView.findViewById(R.id.textViewFirstName);
//        TextView textViewLastName = (TextView) rowView.findViewById(R.id.textViewLastName);

//        textViewEmail.setText(userArrayList.get(position));
        textViewUsername.setText(userArrayList.get(position));
//        textViewFirstName.setText(userArrayList.get(position).getFirstName());
//        textViewLastName.setText(userArrayList.get(position).getLastName());

        return rowView;
    }
}
