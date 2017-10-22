
package com.funcheap.funmapsf.commons.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.funcheap.funmapsf.commons.database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

@Table(database = MyDatabase.class)
@org.parceler.Parcel(analyze={Venue.class})
public class Venue extends BaseModel implements Parcelable
{

    @PrimaryKey(autoincrement = true)
    @Column
    int id;
    @Column
    String venueId;
    @Column
    String venueName;
    @Column
    String venueAddress;
    @Column
    String latitude;
    @Column
    String longitude;
    public final static Parcelable.Creator<Venue> CREATOR = new Creator<Venue>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        public Venue[] newArray(int size) {
            return (new Venue[size]);
        }

    }
            ;

    protected Venue(Parcel in) {
        this.id = ((int) in.readValue((Integer.class.getClassLoader())));
        this.venueId = ((String) in.readValue((String.class.getClassLoader())));
        this.venueName = ((String) in.readValue((String.class.getClassLoader())));
        this.venueAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));

    }

    public Venue() {
    }

    public void setUniqueId(int uniqueId) {
        this.id = uniqueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getUniqueId() {

        return id;
    }

    public String getVenueId() {
        return venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(venueId);
        dest.writeValue(venueName);
        dest.writeValue(venueAddress);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
    }

    public int describeContents() {
        return 0;
    }


    public static Venue fromJSON(JSONObject response) throws JSONException {
        Venue venue = new Venue();
        venue.venueId = response.getString("id");
        venue.venueName = response.getString("name");
        venue.venueAddress = response.getString("address");
        venue.save();
        return venue;

    }

}