package intern.apply.internapply.view.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.view.addjobactivity.AddJobActivity;
import intern.apply.internapply.view.contactusactivity.ContactUsActivity;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private JobsList jobsList;
    private InternAPI internAPI;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test) {
            internAPI = InternAPI.getAPI();
            onInit();
        }
    }

    private void onInit() {
        ShowJobs();
    }

    private void ShowJobs() {
        listView = findViewById(R.id.JobsListView);
        jobsList = new JobsList(internAPI);
        jobsList.ShowList(this);
        ShowFilteredJobs();
        onJobClick();
    }

    public void SetAPI(InternAPI api) {
        internAPI = api;
        onInit();
    }

    public void ShowFilteredJobs() {
        EditText searchBox = findViewById(R.id.searchBox);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                if (query.equals("")) {
                    jobsList.ShowList(MainActivity.this);
                } else {
                    jobsList.ShowFilteredList(MainActivity.this, query);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
    }

    /**
     * navigates to the page of a specific job
     */
    private void onJobClick() {
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Job job = (Job) adapterView.getItemAtPosition(i);

            Intent intent = new Intent(this, ViewJobActivity.class);
            intent.putExtra("jobId", job.getId());
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        } else if (id == R.id.action_contact_us) {
            Intent contactUsIntent = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(contactUsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_add_job) {
            Intent addJobIntent = new Intent(MainActivity.this, AddJobActivity.class);
            startActivity(addJobIntent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
