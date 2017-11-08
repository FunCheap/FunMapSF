package com.funcheap.funmapsf.features.filter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.funcheap.funmapsf.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/11/2017.
 *
 * Dialog that shows up when the user wants to save a filter. It should
 * allow users to name the filter and save or cancel.
 */

public class SaveFilterDialogFragment extends DialogFragment {

    public static final String EXTRA_FILTER_NAME = "filter_name";
    public static final int RESULT_SAVE = 1;
    public static final int REAULT_CANCEL = 0;

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

        save.setOnClickListener(view -> {
            if(!edit_filter_name.getText().toString().isEmpty()) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_FILTER_NAME, edit_filter_name.getText().toString());
                getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
                dismiss();
            }
            else{
                Toast.makeText((getActivity().getApplicationContext()), "Cannot Save ! Filter name is empty", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void prepareCancelClick(){

        cancel.setOnClickListener(view -> {
            getTargetFragment().onActivityResult(getTargetRequestCode(), 0, null);
            dismiss();
        });
    }
}
