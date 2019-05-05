package ese543.helpmi.core;

import android.os.Parcel;
import android.os.Parcelable;
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


public class UserTask implements Parcelable {
    private static final String TAG = UserTask.class.getClass().getSimpleName();
    private String userOwner;
    private String userAssigned;
    private String title;
    private Date datePosted;
    private Date deliveryDate;
    private double latitude;
    private double longitude;
    private double payment;
    private boolean isNegotiable;
    private boolean isComplete;
    private String description;
    private String taskHash;

    public UserTask(String userOwner, String title, Date datePosted,Date deliveryDate, double latitude, double longitude, double payment, boolean isNegotiable, String description)
    {
        this.userOwner = userOwner;
        this.title = title;
        this.datePosted = datePosted;
        this.deliveryDate = deliveryDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.payment = payment;
        this.isNegotiable = isNegotiable;
        this.description = description;
        isComplete = false;
        userAssigned = "";
        taskHash = "";
    }

    public UserTask(String userOwner, String title, Date deliveryDate, Date datePosted, double latitude, double longitude, double payment, boolean isNegotiable, String description, boolean isComplete, String userAssigned, String taskHash)
    {
        this.userOwner = userOwner;
        this.title = title;
        this.datePosted = datePosted;
        this.deliveryDate = deliveryDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.payment = payment;
        this.isNegotiable = isNegotiable;
        this.description = description;
        this.isComplete = isComplete;
        this.userAssigned = userAssigned;
        this.taskHash = taskHash;
    }

    protected UserTask(Parcel in) {
        userOwner = in.readString();
        userAssigned = in.readString();
        title = in.readString();
        datePosted = new Date(in.readLong());
        deliveryDate = new Date(in.readLong());
        latitude = in.readDouble();
        longitude = in.readDouble();
        payment = in.readDouble();
        isNegotiable = in.readByte() != 0;
        isComplete = in.readByte() != 0;
        description = in.readString();
        taskHash = in.readString();
    }

    public static final Creator<UserTask> CREATOR = new Creator<UserTask>() {
        @Override
        public UserTask createFromParcel(Parcel in) {
            return new UserTask(in);
        }

        @Override
        public UserTask[] newArray(int size) {
            return new UserTask[size];
        }
    };

    public void uploadToDatabase()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("owner",userOwner);
        user.put("title",title);
        user.put("datePosted",datePosted);
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

    // Getters
    public String getUserOwner(){return userOwner;}
    public String getUserAssigned(){return userAssigned;}
    public String getTitle(){ return title;}
    public Date getDatePosted(){return datePosted;}
    public Date getDeliveryDate(){return deliveryDate;}
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}
    public double getPayment(){return payment;}
    public boolean getIsNegotiable(){return isNegotiable;}
    public boolean getIsComplete(){return isComplete;}
    public String getDescription(){return description;}
    public String getTaskNum(){return taskHash;}

    //Setters
    public void setUserOwner(String userOwner){this.userOwner = userOwner;}
    public void setUserAssigned(String userAssigned){this.userAssigned = userAssigned;}
    public void setTitle(String title){this.title = title;}
    public void setDatePosted(Date datePosted){this.datePosted = datePosted;}
    public void setDeliveryDate(Date deliveryDate){this.deliveryDate = deliveryDate;}
    public void setLatitude(double latitude){this.latitude = latitude;}
    public void setLongitude(double longitude){this.longitude = longitude;}
    public void setPayment(double payment){this.payment=payment;}
    public void setIsNegotiable(boolean isNegotiable){this.isNegotiable=isNegotiable;}
    public void setIsComplete(boolean isComplete){this.isComplete = isComplete;}
    public void setDescription(String description){this.description=description;}
    public void setTaskNum(String taskNum){this.taskHash = taskHash;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userOwner);
        parcel.writeString(userAssigned);
        parcel.writeString(title);
        parcel.writeLong(datePosted.getTime());
        parcel.writeLong(deliveryDate.getTime());
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(payment);
        parcel.writeByte((byte) (isNegotiable ? 1 : 0));
        parcel.writeByte((byte) (isComplete ? 1 : 0));
        parcel.writeString(description);
        parcel.writeString(taskHash);
    }
}
