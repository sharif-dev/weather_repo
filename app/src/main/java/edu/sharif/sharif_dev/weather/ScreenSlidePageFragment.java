package edu.sharif.sharif_dev.weather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScreenSlidePageFragment extends Fragment {
    private String day;
    private String icon;
    private String summary;

    public static ScreenSlidePageFragment getInstance(String summary, String icon, String day) {
        ScreenSlidePageFragment screenSlidePageFragment = new ScreenSlidePageFragment();
        screenSlidePageFragment.day = day;
        screenSlidePageFragment.icon = icon;
        screenSlidePageFragment.summary = summary;
        return screenSlidePageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        TextView day = rootView.findViewById(R.id.day);
        day.setText(this.day);
        TextView icon = rootView.findViewById(R.id.icon);
        icon.setText(this.icon);
        TextView sum = rootView.findViewById(R.id.summary);
        sum.setText(this.summary);

        return rootView;
    }

}
