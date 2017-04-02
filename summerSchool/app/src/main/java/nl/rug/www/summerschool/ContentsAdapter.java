package nl.rug.www.summerschool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jk on 09/03/17.
 */

public class AnnouncementAdapter extends ArrayAdapter<Contents> {
    public AnnouncementAdapter(Context context, List<Contents> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.announcement_list_item, parent, false);
        }

        Contents currentAnnoucement = getItem(position);
        TextView titleView = (TextView) listItemView.findViewById(R.id.announcementTitle);
        String title = currentAnnoucement.getTitle();
        titleView.setText(title);

        TextView dateView = (TextView) listItemView.findViewById(R.id.announcementDate);
        String date = currentAnnoucement.getDate();
        dateView.setText(date);

        TextView contentsView = (TextView) listItemView.findViewById(R.id.announcementContents);
        String contents = currentAnnoucement.getContents();
        contentsView.setText(contents);

        TextView authorView = (TextView) listItemView.findViewById(R.id.announcemetAuthor);
        String author = currentAnnoucement.getAuthor();
        authorView.setText(author);

        return listItemView;
    }
}
