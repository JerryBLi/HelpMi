package ese543.helpmi.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class User  implements Parcelable {

    private static final String TAG = User.class.getClass().getSimpleName();
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    private boolean exists;
    private boolean login;

    public User(String userName, String email, String firstName, String lastName,  String password)
    {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public User(String email, String password)
    {
        this.email = email;
        this.password = password;

    }

    protected User(Parcel in) {
        userName = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        password = in.readString();
        exists = in.readByte() != 0;
        login = in.readByte() != 0;
    }



    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(email);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(password);
        parcel.writeByte((byte) (exists ? 1 : 0));
        parcel.writeByte((byte) (login ? 1 : 0));
    }

    public interface UserAlreadyExists{
        void onCallback(boolean exists);
    }

    public interface UserLogin{
        void onCallback(boolean exists);
    }

    public void checkUserExists(final UserAlreadyExists callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        exists = false;
        CollectionReference collectionReferencere = db.collection("users");
        Query q1 = collectionReferencere.whereEqualTo("email", email);
        q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                Log.d(TAG, "checkUserExists...onSuccess BEFORE userExists:" + exists);
                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                    String quserName, qemail;
                    quserName = ds.getString("userName");
                    qemail = ds.getString("email");

                    Log.d(TAG, ds.getId() + " => " + ds.getData().get("email"));
                    if (qemail.equals(email)) {
                        exists = true;
                    }
                }

                Log.d(TAG, "checkUserExists...onSuccess AFTER userExists:" + exists);
                callback.onCallback(exists);
            }
        });
    }

    public void loginUser(final UserLogin callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        login = false;
        CollectionReference collectionReferencere = db.collection("users");
        Query q1 = collectionReferencere.whereEqualTo("email", email).whereEqualTo("password", password);
        q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                Log.d(TAG, "loginUser...onSuccess BEFORE login:" + login);
                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                    String qemail, qpassword;
                    qemail = ds.getString("email");
                    qpassword = ds.getString("password");
                    Log.d(TAG, ds.getId() + " => " + ds.getData().get("email")  + ", " + ds.getData().get("password"));
                    if (qemail.equals(email) && qpassword.equals(password)) {
                        login = true;
                        userName = ds.getString("userName");
                        firstName = ds.getString("firstName");
                        lastName = ds.getString("lastName");

                    }
                }

                Log.d(TAG, "checkUserExists...onSuccess AFTER login:" + login);
                callback.onCallback(login);
            }
        });
    }

    public void uploadToDatabase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("userName", userName);
        user.put("email", email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("password", password);
        db.collection("users")
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

    }

    public String getUserName(){return userName;}
}




