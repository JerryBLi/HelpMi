package ese543.helpmi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import ese543.helpmi.R;
import ese543.helpmi.core.UserTask;
import ese543.helpmi.fragments.dummy.AllTasks;
import ese543.helpmi.fragments.dummy.DummyContent;
import ese543.helpmi.fragments.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TaskFragment extends Fragment {

    private static final String TAG = TaskFragment.class.getName();

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyTaskRecyclerViewAdapter adapter;
    private ArrayList<UserTask> userTaskList = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TaskFragment newInstance(int columnCount) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            adapter = new MyTaskRecyclerViewAdapter(userTaskList, mListener);
            recyclerView.setAdapter(adapter);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = db.collection("tasks");
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    userTaskList.clear();
                    for (QueryDocumentSnapshot qds : querySnapshot) {

                        // get all Task fields
                        String qowner, qtitle, qdescription, quserAssigned;
                        Date qdeliveryDate;
                        double qlatitude,  qlongitude, qpayment;
                        boolean qisComplete, qisNegotiable;

                        qowner = qds.getString("owner");
                        qtitle = qds.getString("title");
                        qdescription = qds.getString("description");
                        quserAssigned = qds.getString("userAssigned");
                        qisComplete = qds.getBoolean("isComplete");
                        qisNegotiable = qds.getBoolean("isNegotiable");

                        qlatitude = qds.getDouble("latitude");
                        qlongitude = qds.getDouble("longitude");
                        qpayment = qds.getDouble("payment");
                        qdeliveryDate = qds.getDate("deliveryDate");
                        Log.d(TAG, qds.getId() + " => " + qds.getData());

                        // create Task
                        UserTask task = new ese543.helpmi.core.UserTask(qowner, qtitle, qdeliveryDate, qlatitude, qlongitude, qpayment, qisNegotiable, qdescription);

                        // add to list
                        userTaskList.add(task);
                    }
                    // update adapter
                    adapter.notifyDataSetChanged();
                }

            });
        }
        return view;
    }

    public void printUserTaskList(){

        Log.d(TAG, "PRINTING USER TASK LIST:");
        for(UserTask task: userTaskList){

            Log.d(TAG, task.getTitle() + " " + task.getUserOwner() );
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(UserTask item);
    }
}
