
package com.funcheap.funmapsf.commons.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.funcheap.funmapsf.commons.database.MyDatabase;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(database = MyDatabase.class)
@org.parceler.Parcel(analyze={Events.class})
public class Events extends BaseModel implements Parcelable,ClusterItem
{

    @PrimaryKey
    @Column
    long id;
    @Column
    String title;
    @Column
    String permalink;
    @Column
    String content;
    String excerpt;
    @Column
    String publishDate;
    @Column
    String modifiedDate;
    @Column
    String author;
    @Column
    String startDate;
    @Column
    String endDate;
    @Column
    String cost;
    @Column
    String costDetails;
    @Column
    String bartStation;
    @Column
    @ForeignKey(saveForeignKeyModel = true)
    Venue venue;
    @Column
    String thumbnail;
    @Column
    String categories = null;
    @Column
    String tags = null;
    @Column
    double latitude;
    @Column
    double longitude;

    LatLng position;
    List<String> categoriesList = null;
    List<String> tagsList = null;



    public final static Parcelable.Creator<Events> CREATOR = new Creator<Events>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        public Events[] newArray(int size) {
            return (new Events[size]);
        }

    };

    protected Events(Parcel in) {
        this.id = ((long) in.readValue((long.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.permalink = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.excerpt = ((String) in.readValue((String.class.getClassLoader())));
        this.publishDate = ((String) in.readValue((String.class.getClassLoader())));
        this.modifiedDate = ((String) in.readValue((String.class.getClassLoader())));
        this.author = ((String) in.readValue((String.class.getClassLoader())));
        this.startDate = ((String) in.readValue((String.class.getClassLoader())));
        this.endDate = ((String) in.readValue((String.class.getClassLoader())));
        this.cost = ((String) in.readValue((String.class.getClassLoader())));
        this.costDetails = ((String) in.readValue((String.class.getClassLoader())));
        this.bartStation = ((String) in.readValue((String.class.getClassLoader())));
        this.venue = ((Venue) in.readValue((Venue.class.getClassLoader())));
        this.thumbnail = ((String) in.readValue((String.class.getClassLoader())));
        this.categories = ((String) in.readValue((String.class.getClassLoader())));
        this.tags = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((double) in.readValue((double.class.getClassLoader())));
        this.longitude = ((double) in.readValue((double.class.getClassLoader())));
        this.position = in.readParcelable(LatLng.class.getClassLoader());
        this.categoriesList = in.readArrayList(String.class.getClassLoader());
        this.tagsList = in.readArrayList(String.class.getClassLoader());
    }

    public Events() {
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPosition(LatLng mPosition) {
        this.position = mPosition;
    }

    public LatLng getPosition() {
        return position;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCostDetails() {
        return costDetails;
    }

    public void setCostDetails(String costDetails) {
        this.costDetails = costDetails;
    }

    public String getBartStation() {
        return bartStation;
    }

    public void setBartStation(String bartStation) {
        this.bartStation = bartStation;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<String> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<String> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(permalink);
        dest.writeValue(content);
        dest.writeValue(excerpt);
        dest.writeValue(publishDate);
        dest.writeValue(modifiedDate);
        dest.writeValue(author);
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeValue(cost);
        dest.writeValue(costDetails);
        dest.writeValue(bartStation);
        dest.writeValue(venue);
        dest.writeValue(thumbnail);
        dest.writeValue(categories);
        dest.writeValue(tags);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeParcelable(this.position, flags);
        dest.writeList(categoriesList);
        dest.writeList(tagsList);
    }

    public int describeContents() {
        return 0;
    }


    public static ArrayList<Events> fromJSON(JSONArray response, Context context) throws JSONException{
        ArrayList<Events> eventList = new ArrayList<>();

        for(int i =0;i<response.length();i++){
            JSONObject object = response.getJSONObject(i);
            Events event = new Events();
            event.id = object.getLong("id");
            event.title = object.getString("title");
            event.permalink = object.getString("permalink");
            event.content = object.getString("content");
            event.excerpt = object.getString("excerpt");
            event.publishDate = object.getString("publishDate");
            event.modifiedDate = object.getString("modifiedDate");
            event.author = object.getString("author");
            event.startDate= object.getString("startDate");
            event.endDate= object.getString("endDate");
            event.cost= object.getString("cost");
            event.costDetails= object.getString("costDetails");
            event.bartStation= object.getString("bartStation");
            event.venue = Venue.fromJSON(object.getJSONObject("venue"));
            event.thumbnail= object.getString("thumbnail");
            event.categories = getCategoryString(object.getJSONArray("categories"));
            event.tags = null;
            event.latitude = 37.7749; // setting default values with SFO latitude
            event.longitude = -122.4194; // setting default values with SFO longitude
            LatLng sfo = new LatLng(37.7749,-122.4194); // setting default values with SFO latlng
            event.position = sfo;
            event.categoriesList = new ArrayList<>();
            event.tagsList = new ArrayList<>();
            event.save();
            eventList.add(event);
        }

        return eventList;
    }

    public static String getCategoryString(JSONArray array) throws JSONException{
        StringBuilder categoryList = new StringBuilder();
        int len = array.length();
        for(int i = 0;i<len;i++){
            categoryList.append(array.getString(i));
            if(len>1 && i<len-1){
                categoryList.append(",");
            }
        }

        return categoryList.toString();
    }

    public static ArrayList<Events> eventsDBQuery(){
        ArrayList<Events> list = new ArrayList<>();

        list = (ArrayList<Events>) SQLite.select().from(Events.class).queryList();

        for(int i=0;i<list.size();i++){
            Events event = list.get(i);
            event.setPosition(new LatLng(event.getLatitude(),event.getLongitude()));
            event.categoriesList = new ArrayList<>();
            event.tagsList = new ArrayList<>();

            //Removing and Inserting the event after filling position, categoriesList, tagsList
            list.remove(i);
            list.add(i,event);
        }

        return list;
    }

    public static Events getEventById(long eventId){
        return SQLite.select().from(Events.class).where(Events_Table.id.eq(eventId)).querySingle();
    }

}



