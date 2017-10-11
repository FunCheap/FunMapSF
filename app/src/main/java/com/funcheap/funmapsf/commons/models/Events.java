
package com.funcheap.funmapsf.commons.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Events implements Parcelable {

    private long id;
    private String title;
    private String permalink;
    private String content;
    private String excerpt;
    private String publishDate;
    private String modifiedDate;
    private String author;
    private String startDate;
    private String endDate;
    private String cost;
    private String costDetails;
    private String bartStation;
    private Venue venue;
    private String thumbnail;
    private List<String> categories = null;
    private List<Object> tags = null;
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
        in.readList(this.categories, (java.lang.String.class.getClassLoader()));
        in.readList(this.tags, (java.lang.Object.class.getClassLoader()));
    }

    public Events() {
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

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
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
        dest.writeList(categories);
        dest.writeList(tags);
    }

    public int describeContents() {
        return 0;
    }


    public static ArrayList<Events> fromJSON(JSONArray response) throws JSONException {
        ArrayList<Events> eventList = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {
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
            event.startDate = object.getString("startDate");
            event.endDate = object.getString("endDate");
            event.cost = object.getString("cost");
            event.costDetails = object.getString("costDetails");
            event.bartStation = object.getString("bartStation");
            event.venue = Venue.fromJSON(object.getJSONObject("venue"));
            event.thumbnail = object.getString("thumbnail");
            event.categories = getCategoryList(object.getJSONArray("categories"));
            event.tags = null;

            eventList.add(event);
        }

        return eventList;
    }

    public static ArrayList<String> getCategoryList(JSONArray array) throws JSONException {
        ArrayList<String> categoryList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            categoryList.add(array.getString(i));
        }

        return categoryList;
    }

}



