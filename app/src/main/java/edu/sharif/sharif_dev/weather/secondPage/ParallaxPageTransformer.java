package edu.sharif.sharif_dev.weather.secondPage;

import android.support.v4.view.ViewPager;
import android.view.View;

import edu.sharif.sharif_dev.weather.R;

public class ParallaxPageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) {
            view.setAlpha(1);
        } else if (position <= 1) {
            view.findViewById(R.id.back_image).findViewById(R.id.back_image)
                    .setTranslationX(-position * (pageWidth / 2));
        } else {
            view.setAlpha(1);
        }


    }
}
