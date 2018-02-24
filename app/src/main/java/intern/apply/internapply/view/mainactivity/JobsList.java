package intern.apply.internapply.view.mainactivity;

import android.widget.ListView;
import android.widget.Toast;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * JobsList Class
 * ListView to show job list.
 */

class JobsList {
    private final InternAPI api;
    private ListView listView;

    public JobsList(InternAPI api) {
        this.api = api;
    }

    public void ShowList(MainActivity activity) {
        api.getAllJobs().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    CustomListAdapter listAdapter = new CustomListAdapter(activity, response);
                    listView = activity.findViewById(R.id.JobsListView);
                    listView.setAdapter(listAdapter);
                }, error -> Toast.makeText(activity, "Internal server error, please try again later", Toast.LENGTH_LONG).show());
    }
}