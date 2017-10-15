
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

    @PrimaryKey
    @Column
    long id;
    @Column
    String name;
    @Column
    String address;
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
        this.id = ((long) in.readValue((long.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));

    }

    public Venue() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(address);
    }

    public int describeContents() {
        return 0;
    }


    public static Venue fromJSON(JSONObject response) throws JSONException {
        Venue venue = new Venue();
        venue.id = response.getLong("id");
        venue.name = response.getString("name");
        venue.address = response.getString("address");
        venue.save();
        return venue;

    }

}