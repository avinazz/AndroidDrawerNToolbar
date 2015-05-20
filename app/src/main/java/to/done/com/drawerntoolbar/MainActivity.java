package to.done.com.drawerntoolbar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import de.greenrobot.event.EventBus;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Toolbar app_bar;
    private FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction ft;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ManualActionBarDrawerToggle myActionBarDrawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);

        getSupportActionBar().setSubtitle(null);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myActionBarDrawerToggle = new ManualActionBarDrawerToggle(this, drawerLayout, R.string.desc_open, R.string.desc_open);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int count = fragmentManager.getBackStackEntryCount();
                Log.d(TAG, "Fragment Count: " + count);

                if (count == 0) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                    myActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);

                    myActionBarDrawerToggle.animateToMenu();

                    app_bar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "Navigation clicked");
                            if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                                drawerLayout.closeDrawers();
                            }else {
                                drawerLayout.openDrawer(GravityCompat.START);
                            }
                        }
                    });
                }

                if (count > 0) {

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                    if (!myActionBarDrawerToggle.isUpArrow()) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeButtonEnabled(true);
                        myActionBarDrawerToggle.animateToBackArrow();
                        myActionBarDrawerToggle.syncState();
                    }


                    app_bar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "Navigation clicked");
                            EventBus.getDefault().post(new Boolean(true));
                        }
                    });
                }


            }
        });


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public void onEvent(Boolean isPop) {
        if (isPop) {
            Log.d(TAG, "Popping");
            fragmentManager.popBackStack();
        } else {


            Log.d(TAG, "Pushing");
            Bundle bdl = new Bundle();
            bdl.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, fragmentManager.getBackStackEntryCount() + 1);
            PlaceholderFragment pFrag = new PlaceholderFragment();
            pFrag.setArguments(bdl);
            ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.right_to_left_enter, R.anim.right_to_left_exit, R.anim.left_to_right_enter, R.anim.left_to_right_exit);
            ft.replace(R.id.container, pFrag)
                    .addToBackStack("samplefrag");

            ft.commit();

        }

    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
            return;
        }

        if (fragmentManager.getBackStackEntryCount() > 1) {

            EventBus.getDefault().post(new Boolean(true));

        } else {
            super.onBackPressed();
        }

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView tv = (TextView) rootView.findViewById(R.id.section_label);
            Button btn = (Button) rootView.findViewById(R.id.btnNext);
            tv.setText("Static Fragment " + getArguments().getInt(ARG_SECTION_NUMBER));

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new Boolean(false));
                }
            });


            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

        }
    }

}
