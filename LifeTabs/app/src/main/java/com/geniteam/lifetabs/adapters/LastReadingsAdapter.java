package com.geniteam.lifetabs.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.geniteam.lifetabs.R;
import com.geniteam.lifetabs.bo.ReadingBO;
import com.geniteam.lifetabs.constants.LifeTabsConstents;
import com.geniteam.lifetabs.managers.LifeTabPrefManager;
import com.geniteam.lifetabs.utils.LifeTabUtils;
import com.geniteam.lifetabs.viewCaches.ReadingCache;

import java.util.List;

/**
 * Created by Tayyab on 3/10/2016.
 */
public class LastReadingsAdapter extends ArrayAdapter<ReadingBO> {

    private LayoutInflater inf;
    private Context cx;
    private List<ReadingBO> clist;
    private ReadingCache cache;
    int layout;
    LifeTabPrefManager mPrefManager;


    /**
     * custom array adapter for layout
     * @param context
     * @param resource
     * @param objects
     */
    public LastReadingsAdapter(Context context, int resource, List<ReadingBO> objects,LifeTabPrefManager pManager)
    {
        super(context, resource, objects);

        cx=context;
        inf=LayoutInflater.from(cx);
        clist=objects;
        layout = resource;
        mPrefManager = pManager;
    }

    /**
     * give one item from given position
     */
    public ReadingBO getItem(int pos)
    {
        return clist.get(pos);

    }

    /**
     * return view after setting layout
     */
    @SuppressWarnings("deprecation")
    @SuppressLint({ "ViewHolder", "InflateParams" })
    public View getView(int position,View convertview,ViewGroup parent)
    {
        if (convertview == null) {
            convertview = inf.inflate(layout, parent, false);
            cache = new ReadingCache(convertview);
            convertview.setTag(cache);
        } else {
            cache = (ReadingCache) convertview.getTag();
        }


        //convertview=(RelativeLayout) inf.inflate(R.layout.history_item, null);
        Typeface bold = Typeface.createFromAsset(cx.getAssets(),  "fonts/NotoSans-Bold.ttf");

        ReadingBO item=getItem(position);

        cache.getReadingDate() .setText(item.getDateString());
        cache.getReadingTime() .setText(item.getTimeString());
        cache.getReadingTime().setTypeface(bold);

        if(LifeTabUtils.getColorNumber(Double.parseDouble(item.getValue()),mPrefManager) == LifeTabsConstents.ORANGE){
            cache.getReadingValue().setTextColor(Color.parseColor(LifeTabsConstents.ORANGE_COLOR));
            cache.getReadingIcon().setImageResource(R.drawable.yellow);
        }
        else if(LifeTabUtils.getColorNumber(Double.parseDouble(item.getValue()),mPrefManager) == LifeTabsConstents.BLUE){
            cache.getReadingValue().setTextColor(Color.parseColor(LifeTabsConstents.BLUE_COLOR));
            cache.getReadingIcon().setImageResource(R.drawable.blue);
        }
        else if(LifeTabUtils.getColorNumber(Double.parseDouble(item.getValue()),mPrefManager) == LifeTabsConstents.RED){
            cache.getReadingValue().setTextColor(Color.parseColor(LifeTabsConstents.RED_COLOR));
            cache.getReadingIcon().setImageResource(R.drawable.red);
        }
        cache.getReadingValue().setText(addPadding(item.getValue()));
        cache.getReadingValue().setTypeface(bold);
       // cache.getReadingUnit() .setText(item.getUnit());
        /*name.setText(c.getText());
        Drawable d=cx.getResources().getDrawable(c.getImg_resource());
        img.setImageDrawable(d);*/

        return convertview;

    }

    public String addPadding(String str) {
        String space = " ";

        if (str.length() == 3) {
            return str;
        } else if (str.length() == 2) {
            return space + space + str;
        }
        return str;
    }
}
