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

        if (!"".equals(whatString(filter)))
            list.add(createSimpleChip(whatString(filter)));
        if (!"".equals(whenString(filter)))
            list.add(createSimpleChip(whenString(filter)));
        if (!"".equals(whereString(filter)))
            list.add(createSimpleChip(whereString(filter)));
        if (!"".equals(costString(filter)))
            list.add(createSimpleChip(costString(filter)));
        if (!categoryStrings(filter).isEmpty())
            list.addAll(createSimpleChip(categoryStrings(filter)));

        return list;
    }

    public static ChipView createSimpleChip(String string) {
        ChipBuilder cb = ChipBuilder.create(getContext());
        cb.setText(string);
        ChipView chipView = cb.build();
        chipView.setClickable(false);
        chipView.setTextColor(ContextCompat.getColor(getContext(), R.color.chips_text));
        chipView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.chips_background));

        return chipView;
    }

    public static ChipView createRemovableChip(String string) {
        ChipBuilder cb = ChipBuilder.create(getContext());
        cb.setText(string);
        cb.setCloseable(false);
        cb.setEndIconDrawable(getContext().getResources().getDrawable(R.drawable.ic_close));
        cb.setEndIconColor(getContext().getResources().getColor(R.color.chips_close));
        ChipView chipView = cb.build();
        chipView.setClickable(true);
        chipView.setTextColor(ContextCompat.getColor(getContext(), R.color.chips_text));
        chipView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.chips_background));

        return chipView;
    }

    private static List<ChipView> createSimpleChip(List<String> string) {
        List<ChipView> list = new ArrayList<>();
        for (String s : string) {
            list.add(createSimpleChip(s));
        }
        return list;
    }

    private static String whatString(Filter filter) {
        if ("".equals(filter.getQuery())) {
            return "";
        } else {
            return "\"" + filter.getQuery() + "\"";
        }
    }

    private static String costString(Filter filter) {
        if (filter.isFree()) {
            return "Free";
        } else {
            return "";
        }
    }

    private static String whereString(Filter filter) {
        if ("".equals(filter.getVenueQuery())) {
            return "";
        } else {
            return "In " + filter.getVenueQuery();
        }
    }

    private static String whenString(Filter filter) {
        if ("".equals(filter.getWhenDate())){
            return "";
        } else {
            return filter.getWhenDate();
        }
    }

    private static List<String> categoryStrings(Filter filter) {
        return filter.getCategoriesList();
    }

}
