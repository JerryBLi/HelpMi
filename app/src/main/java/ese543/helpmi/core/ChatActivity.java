package ese543.helpmi.core;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import ese543.helpmi.R;


public class ChatActivity extends AppCompatActivity {


    private static final String TAG = ChatActivity.class.getName();

    private LinearLayout layout;
    private RelativeLayout layout_2;
    private ImageView sendButton;
    private EditText messageArea;
    private ScrollView scrollView;
    private FirebaseFirestore db;
    private CollectionReference messagesRef;
    private CollectionReference messageRefFrom, messageRefTo;
    private SimpleDateFormat sdf;
    private User user;
    private UserTask t;
    private String userNameFrom, messageIDFrom;
    private String userNameTo, messageIDTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sdf = new SimpleDateFormat("EEE, MMM d 'AT' HH:mm a");

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.fullScroll(View.FOCUS_DOWN);


        Intent i = getIntent();
        t = (UserTask) i.getParcelableExtra("task");

        userNameFrom = i.getExtras().get("userNameFrom").toString();
        userNameTo = i.getExtras().get("userNameTo").toString();

        Log.w(TAG, "userNameFrom: " + userNameFrom + " userNameTo: " + userNameTo);
        db = FirebaseFirestore.getInstance();
        messagesRef = db.collection("messages");
//
//        Firebase.setAndroidContext(this);
//        reference1 = new Firebase("https://chatapp-dc05b.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
//        reference2 = new Firebase("https://chatapp-dc05b.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    String currentDateandTime = sdf.format(new Date());
                    map.put("message", messageText);
                    map.put("userName", userNameFrom);
                    map.put("time", currentDateandTime);
                    //messagesRef.add(map);

                    messageRefFrom = messagesRef.document(userNameFrom).collection(userNameTo);
                    messageIDFrom = messageRefFrom.getId();
                    messageRefFrom.add(map);

                    messageRefTo = messagesRef.document(userNameTo).collection(userNameFrom);
                    messageIDTo = messageRefTo.getId();
                    messageRefTo.add(map);
                    messageArea.setText("");
                    messagesRef.add(map);
                }
            }
        });

        messagesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                for (DocumentChange doc : querySnapshot.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Log.d("userNameFrom ", doc.getDocument().getId());
                        doc.getDocument().getReference().collection(userNameFrom).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.d("", "Error : " + e.getMessage());
                                }

                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                        Log.d("userNameTo: ", doc.getDocument().getId());
                                }

                            }
                        });
                    }

                }
                for(QueryDocumentSnapshot qds : querySnapshot )
                {
                    Map map = qds.getData();

                    Log.w(TAG, qds.getData().toString());
                    String message = map.get("message").toString();
                    String userName = map.get("userName").toString();
                    String time = map.get("time").toString();

                    if(userName.equals(userNameFrom)){
                        addMessageBox("You " , message,time, 1);
                    }
                    else{
                        addMessageBox(userNameTo, message,time, 2);
                    }
                }
            }
        });
//        reference1.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Map map = dataSnapshot.getValue(Map.class);
//                String message = map.get("message").toString();
//                String userName = map.get("user").toString();
//                String time = map.get("time").toString();
//
//                if(userName.equals(UserDetails.username)){
//                    addMessageBox("You " , message,time, 1);
//                }
//                else{
//                    addMessageBox(UserDetails.chatWith , message,time, 2);
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
    }

    public void addMessageBox(String name, String message, String time, int type){

        TextView textmsg = new TextView(ChatActivity.this);
        TextView textname = new TextView(ChatActivity.this);
        TextView texttime = new TextView(ChatActivity.this);

        textname.setText(name);
        textname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        textmsg.setText(message);
        texttime.setText(time);
        texttime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp1.gravity = Gravity.RIGHT;
            lp2.gravity = Gravity.RIGHT;
            lp3.gravity = Gravity.RIGHT;
            textmsg.setBackgroundResource(R.drawable.text_in);

        }
        else{
            lp1.gravity = Gravity.LEFT;
            lp2.gravity = Gravity.LEFT;
            lp3.gravity = Gravity.LEFT;
            textmsg.setBackgroundResource(R.drawable.text_out);
        }


        textname.setLayoutParams(lp1);
        textmsg.setLayoutParams(lp2);
        texttime.setLayoutParams(lp3);

        layout.addView(textname);
        layout.addView(textmsg);
        layout.addView(texttime);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


}
