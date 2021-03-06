package edu.sharif.sharif_dev.weather.secondPage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.sharif.sharif_dev.weather.R;

public class ScreenSlidePageFragment extends Fragment {
    private DailyData dailyData;
    private List<HourlyData> hourlyData = null;
    private Handler handler = new Handler();

    public static ScreenSlidePageFragment getInstance(DailyData dailyData) {
        ScreenSlidePageFragment screenSlidePageFragment = new ScreenSlidePageFragment();
        screenSlidePageFragment.dailyData = dailyData;
        return screenSlidePageFragment;
    }

    public void setHourlyData(List<HourlyData> hourlyData) {
        this.hourlyData = hourlyData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        TextView day = rootView.findViewById(R.id.day);
        setTime(day);
        TextView icon = rootView.findViewById(R.id.title_summary);
        icon.setText(dailyData.icon);
        TextView sum = rootView.findViewById(R.id.summary);
        sum.setText(dailyData.summary);
        final TextView temperature = rootView.findViewById(R.id.temperature);

        handler.post(new Runnable() {
            @Override
            public void run() {
                setTemperature(temperature);
                TextView date = rootView.findViewById(R.id.date);
                setDate(date);
                if (hourlyData != null)
                    setDailyDataView(rootView);
                ImageView imageView = rootView.findViewById(R.id.back_image);
                imageView.setImageResource(getIdForImageView());
            }
        });
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

    private void setTemperature(TextView textView) {
        double temp = (dailyData.temperatureHigh + dailyData.temperatureLow) / 2;
        // convert to celsius
        textView.setText(String.valueOf(Math.round((temp - 32) * (0.5556))));
    }

    private int getIdForImageView() {
        if (dailyData.icon.equals(getString(R.string.clear_day))
                || dailyData.icon.equals(getString(R.string.clear_night))
                || dailyData.icon.equals(getString(R.string.sleet)))
            return R.drawable.clear;
        else if (dailyData.icon.equals(getString(R.string.rain))
                || dailyData.icon.equals(getString(R.string.fog)))
            return R.drawable.rain;
        else if (dailyData.icon.equals(getString(R.string.snow)))
            return R.drawable.snow;
        else if (dailyData.icon.equals(getString(R.string.wind))
                || dailyData.icon.equals(getString(R.string.cloudy))
                || dailyData.icon.equals(getString(R.string.partly_cloudy_day))
                || dailyData.icon.equals(getString(R.string.partly_cloudy_night)))
            return R.drawable.cloudy;
        else
            return R.drawable.clear;
    }

    private void setDailyDataView(View view) {
        view.findViewById(R.id.horizontal_scroll).setVisibility(View.VISIBLE);
        view.findViewById(R.id.textView6).setVisibility(View.VISIBLE);
        LinearLayout layout = view.findViewById(R.id.scroll_layout);
        TextView textView;
        ViewGroup.LayoutParams layoutParams;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, HH:mm");
        for (HourlyData hourlyDatum : hourlyData) {
            textView = new TextView(getContext());
            layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setPaddingRelative(10, 0, 10, 0);
            String text = hourlyDatum.summary + "\n"
                    + sdf.format(new Date(Long.valueOf(hourlyDatum.time) * 1000))
                    + "\n" + Math.round((hourlyDatum.temperature - 32) * (0.5556))
                    + " 'C";
            textView.setText(text);
            layout.addView(textView);
        }
    }

}
