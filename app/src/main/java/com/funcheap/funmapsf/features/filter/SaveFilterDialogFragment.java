package com.funcheap.funmapsf.features.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.funcheap.funmapsf.R;
import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/11/2017.
 *
 * Dialog that shows up when the user wants to save a filter. It should
 * allow users to name the filter and save or cancel.
 */

public class SaveFilterDialogFragment extends DialogFragment {

    public interface SaveFilterListener{
        public void saveFilter(String filterName);
    }

    private static final String TAG = "SaveFilterDialogFragment";

    @BindView(R.id.save) Button save;
    @BindView(R.id.cancel) Button cancel;
    @BindView(R.id.edit_filter_name) EditText edit_filter_name;

    public static SaveFilterDialogFragment newInstance() {
        Bundle args = new Bundle();
        // Set any input args here
        SaveFilterDialogFragment fragment = new SaveFilterDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_save_filter, container, false);
        ButterKnife.bind(this, view);
        prepareSaveClick();
        prepareCancelClick();

        return view;
    }

    private void prepareSaveClick(){

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edit_filter_name.getText().toString().isEmpty()) {
                    SaveFilterListener listener = (SaveFilterListener) getActivity();
                    listener.saveFilter(edit_filter_name.getText().toString());
                    dismiss();
                }
                else{
                    Toast.makeText((getActivity().getApplicationContext()), "Cannot Save ! Filter name is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void prepareCancelClick(){

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
