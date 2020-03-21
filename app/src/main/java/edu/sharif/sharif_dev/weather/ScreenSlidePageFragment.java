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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScreenSlidePageFragment extends Fragment {
    private DailyData dailyData;

    public static ScreenSlidePageFragment getInstance(DailyData dailyData) {
        ScreenSlidePageFragment screenSlidePageFragment = new ScreenSlidePageFragment();
        screenSlidePageFragment.dailyData = dailyData;
        return screenSlidePageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        TextView day = rootView.findViewById(R.id.day);
        setTime(day);
        TextView icon = rootView.findViewById(R.id.title_summary);
        icon.setText(dailyData.icon);
        TextView sum = rootView.findViewById(R.id.summary);
        sum.setText(dailyData.summary);
        TextView temperature = rootView.findViewById(R.id.temperature);
        setTemperatur(temperature);
        TextView date = rootView.findViewById(R.id.date);
        setDate(date);

        ImageView imageView = rootView.findViewById(R.id.imageView3);
        imageView.setImageResource(getIdForImageView());
        return rootView;
    }

    private void setDate(TextView textView) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
        String date = sdf.format(new Date(Long.valueOf(dailyData.time) * 1000));
        textView.setText(date);
    }

    private void setTime(TextView textView) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayString = sdf.format(new Date(Long.valueOf(dailyData.time) * 1000));
        textView.setText(dayString);
    }

    private void setTemperatur(TextView textView) {
        double temp = (dailyData.temperatureHigh + dailyData.temperatureLow) / 2;
        // convert to celsius
        textView.setText(String.valueOf(Math.round((temp - 32) * (0.5556))));
    }

    private int getIdForImageView() {
        if (dailyData.icon.equals("clear-day"))
            return R.drawable.clear_day;
        else if (dailyData.icon.equals("clear-night"))
            return R.drawable.clear_night;
        else if (dailyData.icon.equals("rain"))
            return R.drawable.rain;
        else if (dailyData.icon.equals("snow"))
            return R.drawable.snow;
        else if (dailyData.icon.equals("sleet"))
            return R.drawable.sleet;
        else if (dailyData.icon.equals("wind"))
            return R.drawable.wind;
        else if (dailyData.icon.equals("fog"))
            return R.drawable.fog;
        else if (dailyData.icon.equals("cloudy"))
            return R.drawable.cloudy;
        else if (dailyData.icon.equals("partly-cloudy-day"))
            return R.drawable.other;
        else if (dailyData.icon.equals("partly-cloudy-night"))
            return R.drawable.other;
        else
            return R.drawable.other;
    }

}
