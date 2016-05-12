package com.geniteam.lifetabs.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geniteam.lifetabs.bo.DrawerBO;
import com.geniteam.lifetabs.R;

import java.util.List;

/**
 * Created by Tayyab on 3/8/2016.
 */
public class CustomArrayAdapter extends ArrayAdapter<DrawerBO> {

    private LayoutInflater inf;
    private Context cx;
    private List<DrawerBO> clist;

    TextView name;
    ImageView img;


    /**
     * custom array adapter for layout
     * @param context
     * @param resource
     * @param objects
     */
    public CustomArrayAdapter(Context context, int resource, List<DrawerBO> objects)
    {
        super(context, resource, objects);

        cx=context;
        inf=LayoutInflater.from(cx);
        clist=objects;

    }

    /**
     * give one item from given position
     */
    public DrawerBO getItem(int pos)
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
        convertview=(RelativeLayout) inf.inflate(R.layout.drawer_list_item, null);
        name=(TextView)convertview.findViewById(R.id.txt_name);
        img=(ImageView)convertview.findViewById(R.id.imageView);

        DrawerBO c=getItem(position);
        name.setText(c.getText());
        Drawable d=cx.getResources().getDrawable(c.getImg_resource());
        img.setImageDrawable(d);

        return convertview;

    }
}
