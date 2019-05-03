package ese543.helpmi.core;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import ese543.helpmi.R;
import ese543.helpmi.fragments.MapFragment;
import ese543.helpmi.fragments.MessagesFragment;
import ese543.helpmi.fragments.NewTaskFragment;
import ese543.helpmi.fragments.TaskFragment;
import ese543.helpmi.fragments.dummy.DummyContent;

public class MainPage extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener, MessagesFragment.OnListFragmentInteractionListener, NewTaskFragment.OnFragmentInteractionListener, TaskFragment.OnListFragmentInteractionListener{

    private ActionBar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_mapView:
                    toolbar.setTitle("Task Map");
                    loadFragment(new MapFragment());
                    return true;
                case R.id.navigation_ListView:
                    toolbar.setTitle("Task List");
                    loadFragment(new TaskFragment());
                    return true;
                case R.id.navigation_Messages:
                    toolbar.setTitle("My Messages");
                    loadFragment(new MessagesFragment());
                    return true;
                case R.id.nagivation_NewPost:
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Task Map");
        loadFragment(new MapFragment());
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
