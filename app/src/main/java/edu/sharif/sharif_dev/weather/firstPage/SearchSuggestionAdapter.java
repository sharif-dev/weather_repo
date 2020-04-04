package edu.sharif.sharif_dev.weather.firstPage;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.sharif.sharif_dev.weather.R;


public class SearchSuggestionAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resourceId;
    private List<String> items, tempItems, suggestions;

    SearchSuggestionAdapter(@NonNull Context context, int resourceId, ArrayList<String> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            String suggestion = getItem(position);
            TextView name = view.findViewById(R.id.suggestion_text);
            ImageView iconView = view.findViewById(R.id.suggestion_icon);
            iconView.setImageResource(R.drawable.pointer_blue);
            name.setText(suggestion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return suggestionFilter;
    }

    private Filter suggestionFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
// find the suggestion
                for (String suggestion : tempItems) {
                    if (suggestion.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(suggestion);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<String> tempValues = (ArrayList<String>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (String suggestionObj : tempValues) {
                    add(suggestionObj);
                }
                // notify the adapter
                notifyDataSetChanged();
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}