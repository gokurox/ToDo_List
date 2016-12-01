package in.ac.iiitd.gursimran14041.todolist_mc_assign4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ToDoList extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private ListManager mListManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        mListManager = ListManager.getListManagerInstance(getApplicationContext());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("ToDo List");
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(null);
        mToolbar.setSubtitle("2014041");
        mToolbar.setSubtitleTextColor(Color.WHITE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerAdapter = new RecyclerAdapter(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent openDetailsActivityIntent = new Intent(getApplicationContext(), Details.class);
                        openDetailsActivityIntent.putExtra(LocalTags.CONTEXT, LocalTags.NAME_TODOLIST);
                        openDetailsActivityIntent.putExtra(LocalTags.ITEM_POSITION, position);
                        startActivityForResult(openDetailsActivityIntent, 1);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(), "POS: " + position, Toast.LENGTH_SHORT).show();
                    }
                })
        );
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Intent openAddActivityIntent = new Intent(getApplicationContext(), AddTask.class);
                openAddActivityIntent.putExtra(LocalTags.CONTEXT, LocalTags.NAME_TODOLIST);
                startActivityForResult(openAddActivityIntent, 2);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRecyclerAdapter.notifyDataSetChanged();
    }
}
