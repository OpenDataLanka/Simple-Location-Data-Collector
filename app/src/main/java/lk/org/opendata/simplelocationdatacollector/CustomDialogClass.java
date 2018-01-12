package lk.org.opendata.simplelocationdatacollector;

import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private Dialog d;
    private Button save_btn;
    private Location location;
    private String province, city, place;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;

    CustomDialogClass(Activity a, Location loc) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.location = loc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        save_btn = findViewById(R.id.btn_yes);
        save_btn.setOnClickListener(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("locations");
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");
                String appTitle = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("onCancelled", "Failed to read app title value.", error.toException());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            province = ((EditText) findViewById(R.id.province_name)).getText().toString();
            city = ((EditText) findViewById(R.id.city_name)).getText().toString();
            place = ((EditText) findViewById(R.id.place_name)).getText().toString();
            Log.d("Save", province + city + place);
            createLoc(place, city, province, location);
            dismiss();
        }
    }

    private void createLoc(String place, String city, String province, Location location) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        LocationModel loc = new LocationModel(place, city, province, location);

        mFirebaseDatabase.child(userId).setValue(loc);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LocationModel loc = dataSnapshot.getValue(LocationModel.class);

                // Check for null
                if (loc == null) {
                    Log.e(TAG, "Location data is null!");
                    return;
                }

                Log.e(TAG, "Location data is changed!");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }
}