package intern.apply.internapply.view.mainactivity;

import android.widget.ListView;
import android.widget.Toast;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPIProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * JobsList Class
 * ListView to show job list.
 */

class JobsList {
    private final int MAX_QUERY_LENGTH =100;
    private final InternAPIProvider api;
    private ListView listView;

    public JobsList(InternAPIProvider api) {
        this.api = api;
    }

    public void ShowList(MainActivity activity) {
        api.getAllJobs().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    CustomListAdapter listAdapter = new CustomListAdapter(activity, response);
                    listView = activity.findViewById(R.id.JobsListView);

                    listView.setAdapter(listAdapter);
                }, error -> Toast.makeText(activity, R.string.InternalServerError, Toast.LENGTH_LONG).show());
    }

    public void ShowFilteredList(MainActivity activity, String filter) {
        if (filter.length() > MAX_QUERY_LENGTH) {
            Toast.makeText(activity, R.string.searchQueryLengthError, Toast.LENGTH_LONG).show();
        }
        else {
            api.getAllJobs(filter).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        CustomListAdapter listAdapter = new CustomListAdapter(activity, response);
                        ListView listView = activity.findViewById(R.id.JobsListView);
                        listView.setAdapter(listAdapter);
                    }, error -> Toast.makeText(activity, R.string.InternalServerError, Toast.LENGTH_LONG).show());
        }
    }
}