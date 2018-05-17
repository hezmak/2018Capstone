package com.hardcopy.blechat.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.hardcopy.blechat.R;

/**
 * Created by 503 on 2018-05-17.
 */

public class addDialogFragment extends DialogFragment{
    private EditText mName;
    private NameInputListener listener;

    public static addDialogFragment newInstance(NameInputListener listener) {
        addDialogFragment fragment = new addDialogFragment();
        fragment.listener = listener;
        return fragment;
    }

    public interface NameInputListener
    {
        void onNameInputComplete(String name);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add, null);
        mName = (EditText)view.findViewById(R.id.id_txt_input);
        builder.setView(view)
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                listener.onNameInputComplete(mName
                                        .getText().toString());
                            }
                        }).setNegativeButton("취소", null);
        return builder.create();
    }

}
