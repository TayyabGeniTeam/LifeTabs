package com.geniteam.lifetabs.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geniteam.lifetabs.bo.HistoryBO;
import com.geniteam.lifetabs.constants.LifeTabsConstents;
import com.geniteam.lifetabs.R;
import com.geniteam.lifetabs.managers.LifeTabPrefManager;
import com.geniteam.lifetabs.utils.LifeTabUtils;
import com.geniteam.lifetabs.viewCaches.ReadingCache;

import java.util.List;

/**
 * Created by Tayyab on 3/15/2016.
 */
public class HistoryCatagoriesAdapter extends ArrayAdapter<HistoryBO> {

    private LayoutInflater inf;
    private Context cx;
    private List<HistoryBO> clist;
    private ReadingCache cache;
    Typeface bold;
    LifeTabPrefManager mPrefManager;

    public HistoryCatagoriesAdapter(Context context, int resource, List<HistoryBO> objects,LifeTabPrefManager pManager)
    {
        super(context, resource, objects);

        cx=context;
        inf=LayoutInflater.from(cx);
        clist=objects;
        bold = Typeface.createFromAsset(cx.getAssets(),  "fonts/NotoSans-Bold.ttf");
        mPrefManager = pManager;
    }

    /**
     * give one item from given position
     */
    public HistoryBO getItem(int pos)
    {
        return clist.get(pos);

    }

    public View getView(int position,View convertview,ViewGroup parent)
    {
       // if (convertview == null) {
       convertview = inf.inflate(R.layout.history_group_item, parent, false);


        //cache = new ReadingCache(convertview);
           // convertview.setTag(cache);
       // } else {
           // cache = (ReadingCache) convertview.getTag();
       // }


        //convertview=(RelativeLayout) inf.inflate(R.layout.history_item, null);

        HistoryBO item=getItem(position);

        inflateSingleCard(item,convertview);

       // cache.getReadingDate() .setText(item.getDateString());
      //  cache.getReadingTime() .setText(item.getTimeString());
      //  cache.getReadingValue().setText(item.getValue());
       // cache.getReadingUnit() .setText(item.getUnit());
        /*name.setText(c.getText());
        Drawable d=cx.getResources().getDrawable(c.getImg_resource());
        img.setImageDrawable(d);*/

        return convertview;

    }

    public void inflateSingleCard(HistoryBO item,View mView){
        ((TextView)mView.findViewById(R.id.section_name)).setText(item.getSection_header());
        LinearLayout ll = (LinearLayout) mView.findViewById(R.id.ll_container);


        for(int i=0; i < item.getSectionItemList().size() ; i++){
            View subItem = inf.inflate(R.layout.history_item,null);
            ((TextView)subItem.findViewById(R.id.textView1)).setText(item.getSectionItemList().get(i).getDateString());
            ((TextView)subItem.findViewById(R.id.textView2)).setText(item.getSectionItemList().get(i).getTimeString());
            ((TextView)subItem.findViewById(R.id.textView2)).setTypeface(bold);
            ((TextView)subItem.findViewById(R.id.textView3)).setTypeface(bold);


            ((TextView)subItem.findViewById(R.id.textView3)).setText(item.getSectionItemList().get(i).getValue());
            ((TextView)subItem.findViewById(R.id.textView4)).setText(item.getSectionItemList().get(i).getUnit());

            if(LifeTabUtils.getColorNumber(Double.parseDouble(item.getSectionItemList().get(i).getValue()),mPrefManager) == LifeTabsConstents.ORANGE){
                ((TextView)subItem.findViewById(R.id.textView3)).setTextColor(Color.parseColor(LifeTabsConstents.ORANGE_COLOR));
                ((ImageView)subItem.findViewById(R.id.imageView1)).setImageResource(R.drawable.yellow);
            }
            else if(LifeTabUtils.getColorNumber(Double.parseDouble(item.getSectionItemList().get(i).getValue()),mPrefManager) == LifeTabsConstents.BLUE){
                ((TextView)subItem.findViewById(R.id.textView3)).setTextColor(Color.parseColor(LifeTabsConstents.BLUE_COLOR));
                ((ImageView)subItem.findViewById(R.id.imageView1)).setImageResource(R.drawable.blue);
            }
            else if(LifeTabUtils.getColorNumber(Double.parseDouble(item.getSectionItemList().get(i).getValue()),mPrefManager) == LifeTabsConstents.RED){
                ((TextView)subItem.findViewById(R.id.textView3)).setTextColor(Color.parseColor(LifeTabsConstents.RED_COLOR));
                ((ImageView)subItem.findViewById(R.id.imageView1)).setImageResource(R.drawable.red);
            }

            ll.addView(subItem);
        }
    }
}
