package com.funcheap.funmapsf.commons.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.funcheap.funmapsf.commons.database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Jayson on 10/19/2017.
 */
@Table(database = MyDatabase.class)
@org.parceler.Parcel(analyze = {Filter.class})
public class Filter extends BaseModel implements Parcelable {
    @PrimaryKey(autoincrement = true)
    @Column
    public int id;  // autoincrement
    @Column
    public String filterName;  // name of Filter
    @Column
    public String query; // populated from Search EditText
    @Column
    public String whenDate; // Today, Tomorrow, etc...
    @Column
    public String explicitStartDate; // Not functional
    @Column
    public String explicitEndDate; // Not functional
    @Column
    public boolean free;
    @Column
    public String venueQuery; // This is populated from your "Address" EditText
    @Column
    public String categories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getWhenDate() {
        return whenDate;
    }

    public void setWhenDate(String whenDate) {
        this.whenDate = whenDate;
    }

    public String getExplicitStartDate() {
        return explicitStartDate;
    }

    public void setExplicitStartDate(String explicitStartDate) {
        this.explicitStartDate = explicitStartDate;
    }

    public String getExplicitEndDate() {
        return explicitEndDate;
    }

    public void setExplicitEndDate(String explicitEndDate) {
        this.explicitEndDate = explicitEndDate;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getVenueQuery() {
        return venueQuery;
    }

    public void setVenueQuery(String venueQuery) {
        this.venueQuery = venueQuery;
    }

    public String getCategories() {
        return categories;
    }

    public List<String> getCategoriesList() {
        List<String> categoriesList = new ArrayList<>();
        if(!"".equals(categories)) {
            StringTokenizer st = new StringTokenizer(categories.substring(1, categories.length()-1), ",");
            while (st.hasMoreTokens()) {
                categoriesList.add(st.nextToken());
            }
        }
        return categoriesList;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    protected Filter(Parcel in) {
        id = in.readInt();
        filterName = in.readString();
        query = in.readString();
        whenDate = in.readString();
        explicitStartDate = in.readString();
        explicitEndDate = in.readString();
        free = in.readByte() != 0;
        venueQuery = in.readString();
        categories = in.readString();
    }

    public Filter() {
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(filterName);
        parcel.writeString(query);
        parcel.writeString(whenDate);
        parcel.writeString(explicitStartDate);
        parcel.writeString(explicitEndDate);
        parcel.writeByte((byte) (free ? 1 : 0));
        parcel.writeString(venueQuery);
        parcel.writeString(categories);
    }

    //This is the default filter with all default Values set
    public static Filter getDefaultFilter() {
        Filter filter = new Filter();

        filter.filterName = "Default";
        filter.query = "";


        filter.whenDate = "Today";
        filter.explicitStartDate = "";
        filter.explicitEndDate = "";
        filter.free = false;
        filter.venueQuery = "";
        filter.categories = "";

        return filter;
    }

    public static List<Filter> getSavedFilters() {
        return SQLite.select().from(Filter.class).queryList();
    }

    public static void saveDummyFilter(String name) {
        Filter filter = new Filter();
        filter.setFilterName(name);
        filter.setQuery("Party");
        filter.setCategories("Top Pick");
        filter.setFree(true);
        filter.setWhenDate("Today");
        filter.setVenueQuery("Mission");
        filter.save();
    }
}
