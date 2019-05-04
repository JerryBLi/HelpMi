package ese543.helpmi.core;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Task {
    private static final String TAG = Task.class.getClass().getSimpleName();
    private String userOwner;
    private String userAssigned;
    private String title;
    private Date deliveryDate;
    private double latitude;
    private double longitude;
    private double payment;
    private boolean isNegotiable;
    private boolean isComplete;
    private String description;

    public Task(String userOwner, String title, Date deliveryDate, double latitude, double longitude, double payment, boolean isNegotiable, String description)
    {
        this.userOwner = userOwner;
        this.title = title;
        this.deliveryDate = deliveryDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.payment = payment;
        this.isNegotiable = isNegotiable;
        this.description = description;
        isComplete = false;
        userAssigned = "";
    }

    public void uploadToDatabase()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("owner",userOwner);
        user.put("title",title);
        user.put("deliveryDate", deliveryDate);
        user.put("latitude", latitude);
        user.put("longitude", longitude);
        user.put("payment", payment);
        user.put("isNegotiable", isNegotiable);
        user.put("description", description);
        user.put("isComplete", isComplete);
        user.put("userAssigned", userAssigned);
        // Add a new document with a generated ID
        db.collection("tasks")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                        else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
