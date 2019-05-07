package ese543.helpmi.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ese543.helpmi.R;
import ese543.helpmi.core.UserTask;
import ese543.helpmi.fragments.TaskFragment.OnListFragmentInteractionListener;
import ese543.helpmi.fragments.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder> {

    private final List<UserTask> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyTaskRecyclerViewAdapter(List<UserTask> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getTitle());
        holder.owner.setText(mValues.get(position).getUserOwner());
        holder.payment.setText("$"+ mValues.get(position).getPayment() + "");
        holder.description.setText(mValues.get(position).getDescription());
        holder.datePosted.setText(mValues.get(position).getDatePosted().toString());
        holder.deliveryDate.setText(mValues.get(position).getDeliveryDate().toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public final TextView owner;
        public final TextView payment;
        public final TextView description;
        //public final TextView userAssigned;
        public final TextView datePosted;
        public final TextView deliveryDate;
        public UserTask mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.task_title);
            owner = (TextView) view.findViewById(R.id.task_owner);
            payment = (TextView) view.findViewById(R.id.task_payment);
            description = (TextView) view.findViewById(R.id.task_description);
            //userAssigned = (TextView) view.findViewById(R.id.task_userAssigned);
            datePosted = view.findViewById(R.id.task_datePosted);
            deliveryDate = view.findViewById(R.id.task_deliveryDate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }
}
