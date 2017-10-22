package com.funcheap.funmapsf.features.filter.edit;

import android.content.Context;
import com.funcheap.funmapsf.R;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anushree on 10/21/2017.
 */

public class GridButtonAdapter extends BaseAdapter {
    Context mCtx;
    ArrayList<String> mList;


    public GridButtonAdapter(Context context, ArrayList<String> list ){
        mCtx = context;
        mList = list;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater li = LayoutInflater.from(mCtx);

        ViewHolder vh;
        Button btn;

        if (view == null) {  // if it's not recycled, initialize some attributes
            view = li.inflate(R.layout.item_grid,viewGroup,false);
            vh = new ViewHolder(view);
            view.setTag(vh);
            vh.tv.setText(mList.get(mList.size()-1));
        } else {
            vh = (ViewHolder) view.getTag();
            vh.tv.setText(mList.get(i));
        }
        return view;
    }

    class ViewHolder {
        @BindView(R.id.grid_text)
        TextView tv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
