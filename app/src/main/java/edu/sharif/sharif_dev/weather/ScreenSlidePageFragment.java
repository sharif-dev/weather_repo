package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        ImageView imageView = rootView.findViewById(R.id.imageView3);
        imageView.setImageResource(getIdForImageView());
        return rootView;
    }

    private int getIdForImageView() {
        if (icon.equals("clear-day"))
            return R.drawable.clear_day;
        else if (icon.equals("clear-night"))
            return R.drawable.clear_night;
        else if (icon.equals("rain"))
            return R.drawable.rain;
        else if (icon.equals("snow"))
            return R.drawable.snow;
        else if (icon.equals("sleet"))
            return R.drawable.sleet;
        else if (icon.equals("wind"))
            return R.drawable.wind;
        else if (icon.equals("fog"))
            return R.drawable.fog;
        else if (icon.equals("cloudy"))
            return R.drawable.cloudy;
        else if (icon.equals("partly-cloudy-day"))
            return R.drawable.other;
        else if (icon.equals("partly-cloudy-night"))
            return R.drawable.other;
        else
            return R.drawable.other;
    }

}
