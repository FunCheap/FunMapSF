
package com.funcheap.funmapsf.commons.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.funcheap.funmapsf.commons.database.MyDatabase;
import com.funcheap.funmapsf.commons.utils.DateRange;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Table(database = MyDatabase.class)
@org.parceler.Parcel(analyze = {Events.class})
public class Events extends BaseModel implements Parcelable, ClusterItem {
    private static final String TAG = "Events";

    @PrimaryKey
    @Column
    long id;
    @Column
    String eventId;
    @Column
    String title;
    @Column
    String permalink;
    @Column
    String content;
    @Column
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
    @ForeignKey(saveForeignKeyModel = false)
    Venue venue;
    @Column
    String thumbnail;
    @Column
    String categories = null;
    @Column
    String tags = null;
    @Column
    boolean bookmark;

    LatLng position;
    List<String> categoriesList = null;
    List<String> tagsList = null;
    String header = null;


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
        this.eventId = ((String) in.readValue((String.class.getClassLoader())));
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
        this.position = in.readParcelable(LatLng.class.getClassLoader());
        this.categoriesList = in.readArrayList(String.class.getClassLoader());
        this.tagsList = in.readArrayList(String.class.getClassLoader());
        this.bookmark = in.readByte() != 0;
        this.header = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Events() {
    }

    public boolean isBookmarked() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(eventId);
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
        dest.writeParcelable(this.position, flags);
        dest.writeList(categoriesList);
        dest.writeList(tagsList);
        dest.writeByte((byte) (bookmark ? 1 : 0));
        dest.writeValue(header);
    }

    public int describeContents() {
        return 0;
    }


    public static ArrayList<Events> fromJSON(JSONArray response, Context context) throws JSONException {
        ArrayList<Events> eventList = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {
            JSONObject object = response.getJSONObject(i);
            Events event = new Events();
            event.id = object.getLong("id");
            event.eventId = object.getString("eventId");
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
            event.categories = getCategoryString(object.getJSONArray("categories"));
            event.tags = null;
            LatLng sfo = new LatLng(37.7749, -122.4194); // setting default values with SFO latlng
            event.position = sfo;
            event.categoriesList = new ArrayList<>();
            event.tagsList = new ArrayList<>();
            event.bookmark = false;
            event.save();
            eventList.add(event);
        }

        return eventList;
    }

    public static String getCategoryString(JSONArray array) throws JSONException {
        StringBuilder categoryList = new StringBuilder();
        int len = array.length();
        for (int i = 0; i < len; i++) {
            categoryList.append(array.getString(i));
            if (len > 1 && i < len - 1) {
                categoryList.append(",");
            }
        }

        return categoryList.toString();
    }

    public static ArrayList<Events> eventsDBQuery() {
        ArrayList<Events> list = new ArrayList<>();
        ArrayList<Venue> venueList = new ArrayList<>();

        list = (ArrayList<Events>) SQLite.select().from(Events.class).queryList();
        venueList = (ArrayList<Venue>) SQLite.select().from(Venue.class).queryList();

        for (int i = 0; i < list.size(); i++) {
            Events event = list.get(i);
            Venue venue = venueList.get(i);
            event.venue = venue;
            event.setPosition(new LatLng(Double.parseDouble(event.venue.getLatitude()), Double.parseDouble(event.venue.getLongitude())));
            event.categoriesList = new ArrayList<String>(Arrays.asList(event.getCategories().split(",")));
            event.tagsList = new ArrayList<>();

            //Removing and Inserting the event after filling position, categoriesList, tagsList
            list.remove(i);
            list.add(i, event);
        }

        return list;

    }

    public static Events getEventById(String eventId) {
        Events event = SQLite
                .select()
                .from(Events.class)
                .where(Events_Table.eventId.eq(eventId))
                .querySingle();

        int id = (int) event.getId();

        Venue venue = SQLite.select().from(Venue.class).where(Venue_Table.id.eq(id)).querySingle();

        event.setVenue(venue);
        event.setPosition(new LatLng(Double.parseDouble(event.venue.getLatitude()), Double.parseDouble(event.venue.getLongitude())));
        event.categoriesList = new ArrayList<>(Arrays.asList(event.getCategories().split(",")));
        event.tagsList = new ArrayList<>();

        return event;
    }

    public static ArrayList<Events> bookmarkedEventsDBQuery() {
        ArrayList<Events> list;
        ArrayList<Venue> venueList;

        list = (ArrayList<Events>) SQLite.
                select()
                .from(Events.class)
                .where(Events_Table.bookmark.eq(true))
                .orderBy(Events_Table.startDate, true)
                .queryList();
        venueList = (ArrayList<Venue>) SQLite.select().from(Venue.class).queryList();

        for (int i = 0; i < list.size(); i++) {
            Events event = list.get(i);
            event.venue = venueList.get((int) (event.getId() - 1));
            event.setPosition(new LatLng(Double.parseDouble(event.venue.getLatitude()), Double.parseDouble(event.venue.getLongitude())));
            event.categoriesList = new ArrayList<>(Arrays.asList(event.getCategories().split(",")));
            event.tagsList = new ArrayList<>();

            //Removing and Inserting the event after filling position, categoriesList, tagsList
            list.remove(i);
            list.add(i, event);
        }

        return list;
    }


    public static List<Events> getFilteredEvents(Filter filter) {

        List<Events> eventsList;

        String dateRange = DateRange.getDateRange(filter.getWhenDate());

        if (filter.whenDate.equalsIgnoreCase("Today") || filter.whenDate.equalsIgnoreCase("Tomorrow")) {
            // Query with specific start date
            eventsList = SQLite.select().from(Events.class)
                    .where(Events_Table.title.like("%" + filter.getQuery() + "%"))
                    .and(Events_Table.startDate.like("%" + dateRange + "%"))
                    .and(filter.isFree() ? Events_Table.cost.is("0") : Events_Table.cost.like("%" + "" + "%"))
                    .orderBy(Events_Table.startDate, true)
                    .queryList();
        } else {
            // Query all dates and filter dates later
            eventsList = SQLite.select().from(Events.class)
                    .where(Events_Table.title.like("%" + filter.getQuery() + "%"))
                    .and(filter.isFree() ? Events_Table.cost.is("0") : Events_Table.cost.like("%" + "" + "%"))
                    .orderBy(Events_Table.startDate, true)
                    .queryList();
        }

        // Get categories as list
        String temp = filter.getCategories();
        temp = temp.replace("[", "").replace("]", "");
        List<String> filterCategories = Arrays.asList(temp.split(","));
        Log.i(TAG, "" + filter.getCategories());

        // Get all venues as list
        ArrayList<Venue> venueList = (ArrayList<Venue>) SQLite.select().from(Venue.class).queryList();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        /*
         * This loop will remove events that don't match venue, category, and date range filters
         * while also attaching venue objects. This is intended to run in one pass.
         */
        Iterator it = eventsList.iterator();
        while (it.hasNext()) {

            Events event = (Events) it.next();

            // Remove dates out of range
            if (!filter.whenDate.equalsIgnoreCase("Today") && !filter.whenDate.equalsIgnoreCase("Tomorrow")) {
                String[] dates = DateRange.getStartAndEndDate(dateRange);

                try {
                    Date date = df.parse(event.getStartDate());
                    Date start = df.parse(dates[0]);
                    Date end = df.parse(dates[1]);

                    if (start.after(date) || end.before(date)) {
                        // Remove the event and process it no further.
                        it.remove();
                        continue;
                    }
                } catch (ParseException e) {
                    System.out.println(e.toString());
                }
            }

            // Retrieve corresponding venue
            Venue venue = venueList.get((int) event.getId() - 1);
            event.venue = venue;
            event.setPosition(new LatLng(Double.parseDouble(event.venue.getLatitude()), Double.parseDouble(event.venue.getLongitude())));
            event.categoriesList = new ArrayList<>(Arrays.asList(event.getCategories().split(",")));
            event.tagsList = new ArrayList<>(); // Don't know why this is here

            // Remove non-matching venues
            if (!event.venue.getVenueAddress().contains(filter.getVenueQuery())) {
                it.remove();
                continue;
            }

            // Remove if categories don't match
            if (!"".equals(filterCategories.get(0))
                    && !event.categoriesList.containsAll(filterCategories)) {
                it.remove();
            }
        }

        return eventsList;
    }
}



