   package com.example.navigate;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

   public class MainActivity extends AppCompatActivity {

       private DrawerLayout mDrawer;

       private Toolbar toolbar;

       private NavigationView nvDrawer;


       // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.

       private ActionBarDrawerToggle drawerToggle;


       @Override

       public boolean onOptionsItemSelected(MenuItem item) {

           if (drawerToggle.onOptionsItemSelected(item)) {

               return true;

           }

           return super.onOptionsItemSelected(item);

       }
       @Override

       protected void onPostCreate(Bundle savedInstanceState) {

           super.onPostCreate(savedInstanceState);

           // Sync the toggle state after onRestoreInstanceState has occurred.

           drawerToggle.syncState();

       }
       @Override

       public void onConfigurationChanged(Configuration newConfig) {

           super.onConfigurationChanged(newConfig);

           // Pass any configuration change to the drawer toggles

           drawerToggle.onConfigurationChanged(newConfig);

       }



       @Override

       protected void onCreate(Bundle savedInstanceState) {

           super.onCreate(savedInstanceState);

           setContentView(R.layout.activity_main);


           // Set a Toolbar to replace the ActionBar.

           toolbar = (Toolbar) findViewById(R.id.toolbar);

           setSupportActionBar(toolbar);
           nvDrawer = findViewById(R.id.nvView);
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
           drawerToggle = setupDrawerToggle();
           // Setup toggle to display hamburger icon with nice animation

           drawerToggle.setDrawerIndicatorEnabled(true);
           drawerToggle.syncState();
           // Tie DrawerLayout events to the ActionBarToggle

           mDrawer.addDrawerListener(drawerToggle);



           setupDrawerContent(nvDrawer);

           // This will display an Up icon (<-), we will replace it with hamburger later

           Fragment fragment = null;

           try {
               fragment=Home.class.newInstance();
               loadFragment(fragment);
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           } catch (InstantiationException e) {
               e.printStackTrace();
           }


           // Find our drawer view
           nvDrawer.setItemIconTintList(null);



       }
       public void loadFragment(Fragment fragment) {
           FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
           transaction.replace(R.id.flContent, fragment);
           transaction.commit();
       }


       private ActionBarDrawerToggle setupDrawerToggle() {

           // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it

           // and will not render the hamburger icon without it.

           return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);

       }

       private void setupDrawerContent(NavigationView navigationView) {

           navigationView.setNavigationItemSelectedListener(

                   new NavigationView.OnNavigationItemSelectedListener() {

                       @Override

                       public boolean onNavigationItemSelected(MenuItem menuItem) {

                           selectDrawerItem(menuItem);

                           return true;

                       }

                   });

       }




       public void selectDrawerItem(MenuItem menuItem) {

           // Create a new fragment and specify the fragment to show based on nav item clicked

           Fragment fragment = null;

           Class fragmentClass;

           switch (menuItem.getItemId()) {
               case R.id.home_select_frag:

                   fragmentClass = Home.class;

                   break;

               case R.id.ssh_select_frag:

                   fragmentClass = Ssh_conection_fragment.class;

                   break;

               case R.id.host_select_frag:

                   fragmentClass = Hosts_view_fragment.class;

                   break;

               case R.id.usage_select_frag:

                   fragmentClass = Usage_view_fragment.class;

                   break;
               case  R.id.power_select_frag   :

                   fragmentClass= Power.class;
                   break;
               case  R.id.control_panel_select_frag:

                   fragmentClass= Play_fragment.class;
                   break;
               default:

                   fragmentClass = Ssh_conection_fragment.class;

           }


           try {

               fragment = (Fragment) fragmentClass.newInstance();

           } catch (Exception e) {

               e.printStackTrace();

           }
           // Insert the fragment by replacing any existing fragment

           FragmentManager fragmentManager = getSupportFragmentManager();

           assert fragment != null;
           fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();



           // Highlight the selected item has been done by NavigationView

           menuItem.setChecked(true);

           // Set action bar title

           setTitle(menuItem.getTitle());

           // Close the navigation drawer

           mDrawer.closeDrawers();

       }
   }