package com.geniteam.lifetabs.viewCaches;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geniteam.lifetabs.R;

/**
 * Created by Tayyab on 3/10/2016.
 */
public class ReadingCache {

    private View baseView;
    private TextView date;
    private TextView time;
    private TextView value;
   // private TextView unit;
    private ImageView iconView;

    public ReadingCache ( View baseView ) {
        this.baseView = baseView;
    }
    public View getViewBase ( ) {
        return baseView;
    }
    public TextView getReadingDate() {
        if ( date == null ) {
            date = ( TextView ) baseView.findViewById(R.id.textView1);
        }
        return date;
    }

    public TextView getReadingTime() {
        if ( time == null ) {
            time = ( TextView ) baseView.findViewById(R.id.textView2);
        }
        return time;
    }

    public TextView getReadingValue() {
        if ( value == null ) {
            value = ( TextView ) baseView.findViewById(R.id.textView3);
        }
        return value;
    }

   /* public TextView getReadingUnit() {
        if ( unit == null ) {
            unit = ( TextView ) baseView.findViewById(R.id.textView4);
        }
        return unit;
    }*/

    public ImageView getReadingIcon() {
        if ( iconView == null ) {
            iconView = ( ImageView ) baseView.findViewById(R.id.imageView1);
        }
        return iconView;
    }

}
