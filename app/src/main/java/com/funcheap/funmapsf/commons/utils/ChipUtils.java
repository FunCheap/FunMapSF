package com.funcheap.funmapsf.commons.utils;

import android.support.v4.content.ContextCompat;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Filter;
import com.vpaliy.chips_lover.ChipBuilder;
import com.vpaliy.chips_lover.ChipView;

import java.util.ArrayList;
import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by Jayson on 10/25/2017.
 *
 * Utils for generating ChipViews
 */

public class ChipUtils {

    /**
     * Creates Chips for {@link Filter}s objects
     * @param filter filter object to create ChipViews from.
     */
    public static List<ChipView> chipsFromFilter(Filter filter){
        ArrayList<ChipView> list = new ArrayList<>();

        list.add(createChip(whatString(filter)));
        list.add(createChip(costString(filter)));
        list.add(createChip(whereString(filter)));
        list.add(createChip(whenString(filter)));
        list.addAll(createChip(categoryStrings(filter)));

        return list;
    }

    private static ChipView createChip(String string) {
        ChipBuilder cb = ChipBuilder.create(getContext());
        cb.setText(string);
        ChipView chipView = cb.build();
        chipView.setClickable(false);
        chipView.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_text_inverse));
        chipView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));

        return chipView;
    }

    private static List<ChipView> createChip(List<String> string) {
        List<ChipView> list = new ArrayList<>();
        for (String s : string) {
            list.add(createChip(s));
        }
        return list;
    }

    private static String whatString(Filter filter) {
        if ("".equals(filter.getQuery())) {
            return "Any Event";
        } else {
            return filter.getQuery();
        }
    }

    private static String costString(Filter filter) {
        if (filter.isFree()) {
            return "Free";
        } else {
            return "Any Price";
        }
    }

    private static String whereString(Filter filter) {
        if ("".equals(filter.getVenueQuery())) {
            return "Anywhere";
        } else {
            return filter.getVenueQuery();
        }
    }

    private static String whenString(Filter filter) {
        if ("".equals(filter.getWhenDate())){
            return "Any Time";
        } else {
            return filter.getWhenDate();
        }
    }

    private static List<String> categoryStrings(Filter filter) {
        ArrayList<String> list = new ArrayList<>();
        // TODO Parse Categories into Strings
        list.add("Category 1");
        list.add("Category 2");
        return list;
    }

}
