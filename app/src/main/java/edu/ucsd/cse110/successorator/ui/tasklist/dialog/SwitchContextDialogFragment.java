package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// androidx imports
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

// internal imports
import java.time.LocalDate;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentSwitchContextBinding;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;

public class SwitchContextDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private FragmentSwitchContextBinding view;

    SwitchContextDialogFragment() {
        // limitations of object oriented programming :(
    }

    public static SwitchContextDialogFragment newInstance() {
        var fragment = new SwitchContextDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentSwitchContextBinding.inflate(getLayoutInflater());

        var dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Select Focus")
                .setMessage("If you want to exit focus mode, click cancel")
                .setView(view.getRoot())
                .create();
        view.homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityModel.changeContext(TaskContext.Home);
                dialog.dismiss();
            }
        });

        view.workButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityModel.changeContext(TaskContext.Work);
                dialog.dismiss();
            }
        });

        view.schoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityModel.changeContext(TaskContext.School);
                dialog.dismiss();
            }
        });

        view.errandsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityModel.changeContext(TaskContext.Errands);
                dialog.dismiss();
            }
        });

        view.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityModel.changeContext(TaskContext.None);
                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }
}
