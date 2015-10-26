package com.weatherizer.hisham.weatherizer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

/**
 * Created by Hisham on 10/26/2015.
 */
public class RadarActivity extends Activity {
    static String city;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        ImageView radarImageView=(ImageView)findViewById(R.id.radarImageView);
        Ion.with(this)
                .load("http://api.wunderground.com/api/4b731cd2157e63e2/animatedradar/q/+"+city+".gif?newmaps=1&timelabel=1&timelabel.y=15&num=5&delay=50")
                .withBitmap()
                .placeholder(R.drawable.wait)
                .intoImageView(radarImageView);

    }
}
