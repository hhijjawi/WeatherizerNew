package com.weatherizer.hisham.weatherizer;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hisham on 10/26/2015.
 */
//Needed Help from BNRG PAge 178 first edition
public class ForecastListAdapter extends ArrayAdapter<DayData> {
    public ForecastListAdapter(Context context, int resource, List<DayData> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Activity mainActivity = (Activity) getContext();
            convertView = mainActivity.getLayoutInflater().inflate(R.layout.weather_day_item, null);
        }
        DayData currentDay = getItem(position);
        ImageView icon = (ImageView) convertView.findViewById(R.id.logoIcon);
        Picasso.with(getContext()).load(currentDay.getIconURL()).into(icon);
        TextView minText = (TextView) convertView.findViewById(R.id.min);
        minText.setText("Min " + currentDay.getMinTemp());
        TextView maxText = (TextView) convertView.findViewById(R.id.max);
        maxText.setText("Max " + currentDay.getMaxTemp());
        TextView currentDayText = (TextView) convertView.findViewById(R.id.dayOfTheWeek);
        currentDayText.setText("" + currentDay.getDayOfWeek()+": ");
        TextView chanceOfPerc = (TextView) convertView.findViewById(R.id.chanceOfRain);
        chanceOfPerc.setText("Percip: " + currentDay.getChanceOfRain()+"%");
        return convertView;

    }
}
