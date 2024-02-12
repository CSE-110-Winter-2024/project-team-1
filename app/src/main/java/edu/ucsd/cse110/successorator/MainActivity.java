package edu.ucsd.cse110.successorator;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.SuccessoratorTaskListFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding view;

    private boolean isShowingTaskList = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(view.getRoot());
    }

    private void swapFragments() {
        if (isShowingTaskList) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, SuccessoratorTaskListFragment.newInstance())
                    .commit();
        } else {
            // todo: add fragment for adding tasks
        }
    }
}
