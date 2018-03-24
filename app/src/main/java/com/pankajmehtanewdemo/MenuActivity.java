package com.pankajmehtanewdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pankajmehtanewdemo.Appcontroler.Constants;
import com.pankajmehtanewdemo.Fragmen.DirectoryFragment;
import com.pankajmehtanewdemo.Fragmen.HomeFragment;
import com.pankajmehtanewdemo.Fragmen.MediaFragment;
import com.pankajmehtanewdemo.Fragmen.NewsFragment;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout ll_photo, li_video, ll_voice, ll_text;
    LinearLayout main_home, main_news, main_media, main_directory;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    TextView textnews, textdir, textmedia, texthome;
    Window window;
    LinearLayout boatmll;
    TextView tv_apni_taklif;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        ImageView imgtogglemenu=(ImageView)findViewById(R.id.imgtogglemenu);
        imgtogglemenu.setVisibility(View.VISIBLE);

        // getSupportActionBar().setTitle("વાગડસેતુ");

         //getSupportActionBar().hide();
        tv_apni_taklif = (TextView) findViewById(R.id.tv_apni_taklif);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
        tv_apni_taklif.startAnimation(startAnimation);
        ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
        li_video = (LinearLayout) findViewById(R.id.ll_video);
        ll_voice = (LinearLayout) findViewById(R.id.ll_voice);
        ll_text = (LinearLayout) findViewById(R.id.ll_text);
        boatmll = (LinearLayout) findViewById(R.id.boatmll);
        textnews = (TextView) findViewById(R.id.textnews);
        textdir = (TextView) findViewById(R.id.textdir);
        textmedia = (TextView) findViewById(R.id.textmedia);
        texthome = (TextView) findViewById(R.id.texthome);
        Constants.user_mob=getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getString("usermob","");
        main_home = (LinearLayout) findViewById(R.id.main_home);
        main_news = (LinearLayout) findViewById(R.id.main_news);
        main_media = (LinearLayout) findViewById(R.id.main_media);
        main_directory = (LinearLayout) findViewById(R.id.main_directory);
        main_home.setOnClickListener(this);
        main_news.setOnClickListener(this);
        main_media.setOnClickListener(this);
        main_directory.setOnClickListener(this);
        li_video.setOnClickListener(this);
        ll_photo.setOnClickListener(this);
        ll_voice.setOnClickListener(this);
        ll_text.setOnClickListener(this);
/*        HomeFragment homeFragment = new HomeFragment();
        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, homeFragment);
        fragmentTransaction.commit();*/


        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
       /* txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);*/

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        // load nav menu header data
        //loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        imgtogglemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawers();
                }else {
                    drawer.openDrawer(Gravity.LEFT);
                }

            }
        });
    }

    private void loadNavHeader() {
        // name, website
        txtName.setText("Ravi Tamada");
        txtWebsite.setText("www.androidhive.info");

/*        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);*/

        // showing dot next to notifications label
       // navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.fragment, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
           /* case 1:
                // photos
                PhotosFragment photosFragment = new PhotosFragment();
                return photosFragment;
            case 2:
                // movies fragment
                MoviesFragment moviesFragment = new MoviesFragment();
                return moviesFragment;
            case 3:
                // notifications fragment
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;

            case 4:
                // settings fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;*/
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_video:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        startActivity(new Intent(MenuActivity.this, WatchVideoActivity.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_aboutus:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        startActivity(new Intent(MenuActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_contact:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        startActivity(new Intent(MenuActivity.this, ContactUsActivity.class));
                        drawer.closeDrawers();
                        break;

/*                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;*/
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu., menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }*/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_photo:
                startActivity(new Intent(MenuActivity.this, PhotoActivity.class));
                break;
            case R.id.ll_voice:
                startActivity(new Intent(MenuActivity.this, AudioActivity.class));
                break;
            case R.id.ll_video:
                startActivity(new Intent(MenuActivity.this, VideoActivity.class));
                break;
            case R.id.ll_text:
                startActivity(new Intent(MenuActivity.this, TextActivity.class));
                break;
            case R.id.main_home:
                window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(Color.parseColor("#f6813e"));
                    android.support.v7.app.ActionBar actionBar = getSupportActionBar();

                    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f6813e")));
                }
                boatmll.setBackgroundColor(Color.parseColor("#f6813e"));
                texthome.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.back_bottom));
                textnews.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_background));
                textmedia.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_background));
                textdir.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_background));
                HomeFragment homeFragment = new HomeFragment();
                fm = getSupportFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, homeFragment);
                fragmentTransaction.commit();

                //startActivity(new Intent(MenuActivity.this,HomemenuActivity.class));
                break;
            case R.id.main_news:
                window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(Color.parseColor("#009687"));
                    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009687")));
                }
                boatmll.setBackgroundColor(Color.parseColor("#009687"));
                textnews.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.back_greenbottom));
                texthome.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_greenbackground));
                textmedia.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_greenbackground));
                textdir.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_greenbackground));
                NewsFragment newsFragment = new NewsFragment();
                fm = getSupportFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, newsFragment);
                fragmentTransaction.commit();
                //startActivity(new Intent(MenuActivity.this,NewsActivity.class));
                break;
            case R.id.main_media:
                window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(Color.parseColor("#adb42a"));
                    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#adb42a")));
                }
                boatmll.setBackgroundColor(Color.parseColor("#adb42a"));
                textmedia.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.back_pinkbottom));
                textnews.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_pinkbackground));
                texthome.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_pinkbackground));
                textdir.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_pinkbackground));
                MediaFragment mediafragment = new MediaFragment();
                fm = getSupportFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, mediafragment);
                fragmentTransaction.commit();
                //startActivity(new Intent(MenuActivity.this,MediaActivity.class));
                break;
            case R.id.main_directory:
                window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(Color.parseColor("#9b27ae"));
                    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9b27ae")));
                }
                boatmll.setBackgroundColor(Color.parseColor("#9b27ae"));
                textmedia.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_purplebackground));
                textdir.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.back_purplepinkbottom));
                textnews.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_purplebackground));
                texthome.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.no_purplebackground));
                DirectoryFragment directoryFragment = new DirectoryFragment();
                fm = getSupportFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, directoryFragment);
                fragmentTransaction.commit();
                //startActivity(new Intent(MenuActivity.this,DirectroyActivity.class));
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_header_menu, menu);
        // Associate searchable configuration with the SearchView
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.notification:
                startActivity(new Intent(MenuActivity.this, NotificationActivity.class));
                break;
        }
        return true;
    }
}
