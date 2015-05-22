package com.mcube.app;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.mcube.app.tabs.TabsPagerAdapter;
import com.mcube.app.util.McubeUtils;
import com.mcube.app.widget.SingleTabWidget;


public class HomeActivity extends ActionBarActivity {


    ViewPager viewPager;
    int lastPagePosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.header_blue));
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        initializeTabs();
    }

    private void initializeTabs() {
        final SingleTabWidget tabWidget = (SingleTabWidget) findViewById(R.id.tabs);

        tabWidget.setLayout(R.layout.tab_indicator);
        tabWidget.addTab(R.drawable.tab_home,
                "Follow Up");
        tabWidget.addTab(R.drawable.tab_search,
                "Track");
        tabWidget.addTab(R.drawable.tab_home,
                "X");
        tabWidget.addTab(R.drawable.tab_search,
                "IVRS");
        tabWidget.addTab(R.drawable.tab_home,
                "Lead");
        tabWidget.addTab(R.drawable.tab_search,
                "Settings");


        tabWidget.setOnTabChangedListener(new SingleTabWidget.OnTabChangedListener() {
            @Override
            public void onTabChanged(int index) {
                //addFragment(index);
                McubeUtils.debugLog("Changing page:" + index);
                viewPager.setCurrentItem(index);

            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position != lastPagePosition) {
                    McubeUtils.debugLog("Setting tab:" + position);
                    tabWidget.setCurrentTab(position);
                    tabWidget.setmSelectedTab(position);
                    lastPagePosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
