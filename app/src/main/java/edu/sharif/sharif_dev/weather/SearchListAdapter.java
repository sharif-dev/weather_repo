package edu.sharif.sharif_dev.weather;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SearchListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;
    ListView listView ;

    //to store the list of countries
    private final List<String> cityNameArray;

    SearchListAdapter(Activity context, List<String> cityNameArray){
        super(context,R.layout.search_list_row ,  cityNameArray);
        this.context = context;
        this.cityNameArray = cityNameArray;
    }
    public int getCount(){
        return cityNameArray.size();
    }
    public Object getItem(int position){
        return null;
    }
    public long getItemId(int position){
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.search_list_row, null);

        //this code gets references to objects in the listview_row.xml file
//        TextView nameTextField = (TextView) rowView.findViewById(R.id.cityNameID);
//
//        //this code sets the values of the objects to values from the arrays
//        String[] array = cityNameArray.toArray(new String[cityNameArray.size()]);
//        nameTextField.setText(array[position]);

        return rowView;

    };

}
