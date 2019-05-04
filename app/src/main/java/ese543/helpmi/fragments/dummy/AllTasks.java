package ese543.helpmi.fragments.dummy;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ese543.helpmi.core.UserTask;

public class AllTasks {
    private static final String TAG = AllTasks.class.getClass().getSimpleName();
    public static final List<UserTask> ITEMS = new ArrayList<>();

    //Populate the list
    static {
        // Add some sample items.
        for (int i = 1; i <= 10; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(UserTask t)
    {
        ITEMS.add(t);
    }

    private static UserTask createDummyItem(int position)
    {

        return new ese543.helpmi.core.UserTask("TestUser" + position, "Title" + position, new Date(), 10, 15, Math.random() * 100, (Math.random() > 0.5 ? true : false), "Test Description" + position);
    }

    private static void RetreiveItemsFromDB()
    {

    }
}
