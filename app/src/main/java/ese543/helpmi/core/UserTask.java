package ese543.helpmi.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class UserTask implements Parcelable {
    private static final String TAG = UserTask.class.getName();

    private String taskID;
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
    private int numImages;

    private ArrayList<String> interestedUsers;

    public UserTask(String userOwner, String title, Date datePosted,Date deliveryDate, double latitude, double longitude, double payment, boolean isNegotiable, String description, boolean isComplete, String userAssigned)
    {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference ref = db.collection("tasks").document();
//        this.taskID = ref.getId();
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
        taskHash = "";
        interestedUsers = new ArrayList<String>();
        numImages = 0;
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
        interestedUsers = new ArrayList<>();
    }

    protected UserTask(Parcel in) {
        taskID = in.readString();
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
        //interestedUsers = in.readArrayList(User.class.getClassLoader());
        interestedUsers = in.readArrayList(null);
        numImages = in.readInt();
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


    public interface UserTaskUpload{
        void onCallback(String taskID);
    }

//    public void uploadToDatabase(final UserTaskUpload callback)
    public void uploadToDatabase()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("tasks").document();
        this.taskID = ref.getId();
        // Create a new user with a first and last name
        Map<String, Object> task = new HashMap<>();
        task.put("owner",userOwner);
        task.put("title",title);
        task.put("datePosted",datePosted);
        task.put("deliveryDate", deliveryDate);
        task.put("latitude", latitude);
        task.put("longitude", longitude);
        task.put("payment", payment);
        task.put("isNegotiable", isNegotiable);
        task.put("description", description);
        task.put("isComplete", isComplete);
        task.put("userAssigned", userAssigned);
        task.put("interestedUsers", interestedUsers);
        task.put("numImages",numImages);

        // Add a new document with a generated ID
        db.collection("tasks")
                .document(taskID)
                .set(task);
    }

    public void editTask(Map<String, Object> newValue){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("tasks").document(taskID);
        ref.update(newValue);

    }

    // Getters
    public String getTaskID(){return taskID;}
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
    public ArrayList<String> getInterestedUsers(){return interestedUsers;};
    public int getNumImages(){return  numImages;}

    //Setters
    public void setTaskID(String taskID){this.taskID = taskID;}
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
    public void setInterestedUsers(ArrayList<String> interestedUsers){this.interestedUsers = interestedUsers;};
    public void setNumImages(int numImages){this.numImages = numImages;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(taskID);
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
        parcel.writeList(interestedUsers);
        parcel.writeInt(numImages);
    }
}
