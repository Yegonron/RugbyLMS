package com.yegonron.rugbylms;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

public class MyDialog extends DialogFragment {
    public static final String TEAM_ADD_DIALOG = "addTeam";
    public static final String TEAM_UPDATE_DIALOG = "updateTeam";
    public static final String PLAYER_ADD_DIALOG = "addPlayer";
    public static final String PLAYER_UPDATE_DIALOG = "updatePlayer";

    private OnClickListener listener;
    private int roll;
    private String name;


    @SuppressLint("ValidFragment")
    public MyDialog(int roll, String name) {

        this.roll = roll;
        this.name = name;
    }

    public MyDialog() {

    }

    //DELETE
    public void show(FragmentManager supportFragmentManager, String playerAddDialog) {

    }

    public interface OnClickListener {
        void onClick(String text1, String text2);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if (getTag().equals(TEAM_ADD_DIALOG)) dialog = getAddTeamDialog();
        if (getTag().equals(TEAM_UPDATE_DIALOG)) dialog = getUpdateTeamDialog();
        if (getTag().equals(PLAYER_ADD_DIALOG)) dialog = getAddPlayerDialog();
        if (getTag().equals(PLAYER_UPDATE_DIALOG)) dialog = getUpdatePlayerDialog();
        assert dialog != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private Dialog getUpdatePlayerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Player");

        EditText rollEt = view.findViewById(R.id.edt01);
        EditText nameEt = view.findViewById(R.id.edt02);

        rollEt.setHint("Roll");
        nameEt.setHint("Name");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("update");

        rollEt.setText(roll + "");
        rollEt.setEnabled(false);
        nameEt.setText(name);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String roll = rollEt.getText().toString();
            String name = nameEt.getText().toString();
            listener.onClick(roll, name);
            dismiss();

        });

        return builder.create();

    }


    private Dialog getUpdateTeamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Team");

        EditText teamNameEt = view.findViewById(R.id.edt01);
        EditText seasonEt = view.findViewById(R.id.edt02);

        teamNameEt.setHint("Team Name");
        seasonEt.setHint("Season");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        add.setText("Update");
        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String teamName = teamNameEt.getText().toString();
            String season = seasonEt.getText().toString();
            listener.onClick(teamName, season);
            dismiss();
        });

        return builder.create();


    }

    private Dialog getAddPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Player");

        EditText rollEt = view.findViewById(R.id.edt01);
        EditText nameEt = view.findViewById(R.id.edt02);

        rollEt.setHint("Roll");
        nameEt.setHint("Name");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String roll = rollEt.getText().toString();
            String name = nameEt.getText().toString();
            rollEt.setText(String.valueOf(Integer.parseInt(roll) + 1));
            nameEt.setText("");
            listener.onClick(roll, name);

        });

        return builder.create();
    }

    private Dialog getAddTeamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Team");

        EditText teamNameEt = view.findViewById(R.id.edt01);
        EditText seasonEt = view.findViewById(R.id.edt02);

        teamNameEt.setHint("Team Name");
        seasonEt.setHint("Season");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String teamName = teamNameEt.getText().toString();
            String season = seasonEt.getText().toString();
            listener.onClick(teamName, season);
            dismiss();
        });

        return builder.create();

    }
}
