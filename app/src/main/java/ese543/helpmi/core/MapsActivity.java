package ese543.helpmi.core;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;

import ese543.helpmi.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = User.class.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("tasks");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                    double qlatitude,  qlongitude, qitemCost;
                    String qitemName, qitemDescription, qitemOwner;
                    qlatitude = ds.getDouble("latitude");
                    qlongitude = ds.getDouble("longitude");
                    qitemName = ds.getString("title");
                    qitemDescription = ds.getString("description");
                    qitemOwner = ds.getString("owner");
                    qitemCost = ds.getDouble("payment");

                    Log.d(TAG, ds.getId() + " => " + ds.getData().get("latitude")+ " => " + ds.getData().get("longitude")+ " => " + ds.getData().get("title")+ " => " + ds.getData().get("description")+ " => " + ds.getData().get("payment"));

                    LatLng latLng = new LatLng(qlatitude, qlongitude);
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(latLng.toString());
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(qlatitude, qlongitude)).title(qitemName)
                            .snippet("Price: $"+ qitemCost);

                    mMap.addMarker(marker);
                }
            }
        });

//        for(int i=0;i<latitudeCoord.size();i++)
//        {
//            LatLng latLng = new LatLng(latitudeCoord.get(i), longitudeCoord.get(i));
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
//        }

    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);


    }
}


