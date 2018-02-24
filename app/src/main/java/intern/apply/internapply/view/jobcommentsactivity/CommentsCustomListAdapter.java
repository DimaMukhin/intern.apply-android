package intern.apply.internapply.view.jobcommentsactivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.model.Comment;

public class CommentsCustomListAdapter extends ArrayAdapter {

    private final Activity context;
    private final List<Comment> comments;

    public CommentsCustomListAdapter(Activity context, List<Comment> comments){
        super(context, R.layout.comments_listview_row , comments);

        this.context = context;
        this.comments = comments;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.comments_listview_row, null,true);

        TextView nameTextField = (TextView) rowView.findViewById(R.id.tvName);
        TextView messageTextField = (TextView) rowView.findViewById(R.id.tvMessage);

        nameTextField.setText(comments.get(position).getAuthor());
        messageTextField.setText(comments.get(position).getMessage());

        return rowView;
    }
}
