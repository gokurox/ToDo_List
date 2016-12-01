package in.ac.iiitd.gursimran14041.todolist_mc_assign4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTask extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText mTitle;
    private EditText mChronology;
    private EditText mDescription;
    private Button mSubmitButton;
    private ListManager mListManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Add Task");
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mListManager = ListManager.getListManagerInstance(getApplicationContext());

        mTitle = (EditText) findViewById(R.id.add_title);
        mChronology = (EditText) findViewById(R.id.add_timestamp);
        mDescription = (EditText) findViewById(R.id.add_description);
        mSubmitButton = (Button) findViewById(R.id.add_submitButton);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText().toString().trim();
                String description = mDescription.getText().toString().trim();
                String chronology = mChronology.getText().toString().trim();
                ToDoTask task = new ToDoTask(title, description, chronology);
                mListManager.addTask(task);
                finish();
            }
        });
    }
}
