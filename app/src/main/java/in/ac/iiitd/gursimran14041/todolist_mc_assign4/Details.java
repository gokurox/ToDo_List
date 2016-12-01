package in.ac.iiitd.gursimran14041.todolist_mc_assign4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Details extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ListManager mListManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Details");
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mListManger = ListManager.getListManagerInstance(getApplicationContext());
        mViewPager.setAdapter(new CustomViewPagerAdapter(getSupportFragmentManager(), getApplicationContext()));

        Intent callingIntent = getIntent();
        Bundle callingExtras = callingIntent.getExtras();
        if (callingExtras != null) {
            if (callingExtras.getString(LocalTags.CONTEXT, LocalTags.DEFAULT).compareTo(LocalTags.DEFAULT) != 0) {
                String context = callingExtras.getString(LocalTags.CONTEXT);

                if (context.compareTo(LocalTags.NAME_TODOLIST) == 0) {
                    int position = callingExtras.getInt(LocalTags.ITEM_POSITION);
                    mViewPager.setCurrentItem(position);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                int position = mViewPager.getCurrentItem();
                mListManger.deleteTask(position);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private class CustomViewPagerAdapter extends FragmentStatePagerAdapter {
        public CustomViewPagerAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            DetailFragment fragment = new DetailFragment();

            Bundle args = new Bundle();
            args.putInt(LocalTags.ITEM_POSITION, position);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mListManger.getSize();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return new String("Item " + (position + 1));
        }
    }
}
