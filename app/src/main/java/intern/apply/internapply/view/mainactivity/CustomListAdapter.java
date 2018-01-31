package intern.apply.internapply.view.mainactivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.model.Job;

/**
 * Created by habib on 1/29/2018.
 */

/**
 * CustomListAdapter Class
 * Adapter class to convert from Job to rows.
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2018-01-30
 */

public class CustomListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;

    private final String[] allTitles;
    private final String[] allCompanyNames;

    public CustomListAdapter(Activity context, List<Job> jobs) {
        super(context, R.layout.jobs_listview_row, jobs);
        this.context = context;

        allTitles = new String[jobs.size()];
        for (int i = 0; i < jobs.size(); i++)
            allTitles[i] = jobs.get(i).getTitle();
        allCompanyNames = new String[jobs.size()];
        for (int i = 0; i < jobs.size(); i++)
            allCompanyNames[i] = jobs.get(i).getCompanyName();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.jobs_listview_row, null, true);

        TextView titleField = rowView.findViewById(R.id.TitleTextView);
        TextView companyField = rowView.findViewById(R.id.CompanyTextView);

        titleField.setText(allTitles[position]);
        companyField.setText(allCompanyNames[position]);

        return rowView;
    }

}
