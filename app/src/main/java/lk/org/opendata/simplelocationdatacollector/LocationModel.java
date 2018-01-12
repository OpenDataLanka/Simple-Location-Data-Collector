package lk.org.opendata.simplelocationdatacollector;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LocationModel {
    public String place, city, province;
    public double lat, lng;
    //public LocationUtil location;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public LocationModel() {
    }

    public LocationModel(String place, String city, String province, Location location) {
        this.place = place;
        this.city = city;
        this.province = province;
        this.lat = location.getLatitude();
        this.lng = location.getLongitude();

        //this.location = location;
    }
}
