package org.apache.taverna.mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.taverna.mobile.R;

import java.util.List;

/**
 * Created by root on 6/7/15.
 */
public class SliderMenuAdapter extends BaseAdapter{

    private List<String> dataItems;
    private Context context;

    public SliderMenuAdapter(Context c,List<String> items){
        dataItems = items;
        context = c;
    }

    @Override
    public int getCount() {
        return dataItems.size();
    }

    @Override
    public String getItem(int i) {
        return dataItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View menuitemview = LayoutInflater.from(context).inflate(R.layout.menu_item_layout, viewGroup, false);
       /* if(view != null ) {
            ViewHolder vh = (ViewHolder) view.getTag();
            if (vh == null) {
                ViewHolder v = new ViewHolder(menuitemview);
                view.setTag(v);
                return getView(i,view,viewGroup);
            } else {
                vh.menuitem.setText(dataItems.get(i));
                vh.menuicon.setImageResource(R.drawable.gear_icon);
            }
        }*/
        ImageView menuicon = (ImageView) menuitemview.findViewById(R.id.menuIcon);
        TextView menuitem = (TextView) menuitemview.findViewById(R.id.menuItemText);
        switch(i +1){
            case 1:
                menuicon.setImageResource(R.drawable.gear_icon);
                menuitem.setText(dataItems.get(i));
                break;
            case 2:
                menuicon.setImageResource(android.R.drawable.ic_menu_agenda);
                menuitem.setText(dataItems.get(i));
                break;
            case 3:
                menuicon.setImageResource(R.mipmap.ic_usage);
                menuitem.setText(dataItems.get(i));
                break;
            case 4:
                menuicon.setImageResource(android.R.drawable.ic_dialog_info);
                menuitem.setText(dataItems.get(i));
                break;
            case 5:
                menuicon.setImageResource(android.R.drawable.ic_lock_power_off);
                menuitem.setText(dataItems.get(i));
                break;
        }
        return menuitemview;
    }

    public static class ViewHolder{
        public final ImageView menuicon;
        public final TextView menuitem;

        public ViewHolder(View view){
            menuicon = (ImageView) view.findViewById(R.id.menuIcon);
            menuitem = (TextView) view.findViewById(R.id.menuItemText);
        }

    }
}
