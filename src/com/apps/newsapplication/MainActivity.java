package com.apps.newsapplication;
 
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.apps.newsapplication.R.string;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.StartAppAd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

	
	private String[] tabs = { "LATEST", "ALL NEWS", "POPULAR", "MY FAVORITES" };
    private TabsPagerAdapter mAdapter;
    private ViewPager viewPager;
    ActionBar.Tab tab;
    private AdView mAdView;
    private StartAppAd startAppAd = new StartAppAd(this); 
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
       
        super.onCreate(savedInstanceState);
        StartAppAd.init(this, getString(R.string.startapp_dev_id), getString(R.string.startapp_app_id));
        setContentView(R.layout.activity_main);
        StartAppAd.showSlider(this);
        mAdView = (AdView) findViewById(R.id.adView);
	    mAdView.loadAd(new AdRequest.Builder().build());
        
        viewPager = (ViewPager) findViewById(R.id.pager);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        
        //tab added in action bar
         for(String tab_name:tabs)
         {
        	tab = getSupportActionBar().newTab();
            tab.setText(tab_name);
            tab.setTabListener(this);
            getSupportActionBar().addTab(tab);
         }
         
         /**
 		 * on swiping the viewpager make respective tab selected
 		 * */
 		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

 			@Override
 			public void onPageSelected(int position) {
 				// on changing the page
 				// make respected tab selected
 				getSupportActionBar().setSelectedNavigationItem(position);
 			}

 			@Override
 			public void onPageScrolled(int arg0, float arg1, int arg2) {
 			}

 			@Override
 			public void onPageScrollStateChanged(int arg0) {
 			}
 		});
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction transaction) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction transaction) {
    	viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	  MenuInflater inflater = getSupportMenuInflater();
          inflater.inflate(R.menu.main, menu);
          

          return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) 
        {
    	case R.id.menu_about:
    		
    		Intent about=new Intent(getApplicationContext(),AboutActivity.class);
    		startActivity(about);
    		
    		return true;
    		
    	case R.id.menu_overflow:
    		return true;
    		
    	case R.id.menu_moreapp:
    		
    		startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse(getString(R.string.play_more_apps))));
			
    		return true;
    	
    	case R.id.menu_rateapp:
    		
    		final String appName = getPackageName();//your application package name i.e play store application url
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id="
								+ appName)));
			} catch (android.content.ActivityNotFoundException anfe) {
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ appName)));
			}
    		return true;
    	
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Toast.makeText(appContext, "BAck", Toast.LENGTH_LONG).show();
			AlertDialog.Builder alert = new AlertDialog.Builder(
					MainActivity.this);
			alert.setTitle(string.app_name);
			alert.setIcon(R.drawable.app_icon);
			alert.setMessage("Are You Sure You Want To Quit?");
		
			alert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							
							 //you may open Interstitial Ads here
							 startAppAd.onBackPressed();
							finish();
						}
							 
		
						 
					});
		
			alert.setNegativeButton("Rate App",
					new DialogInterface.OnClickListener() {
		
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							 
							final String appName = getPackageName();//your application package name i.e play store application url
							try {
								startActivity(new Intent(Intent.ACTION_VIEW,
										Uri.parse("market://details?id="
												+ appName)));
							} catch (android.content.ActivityNotFoundException anfe) {
								startActivity(new Intent(
										Intent.ACTION_VIEW,
										Uri.parse("http://play.google.com/store/apps/details?id="
												+ appName)));
							}
				    		 
						}
					});
			alert.show();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
		
		}
    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
        startAppAd.onResume();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }


	
}
