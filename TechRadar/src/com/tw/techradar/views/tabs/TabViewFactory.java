package com.tw.techradar.views.tabs;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tw.techradar.R;
import com.tw.techradar.constants.menus.MenuItems;

public class TabViewFactory {

    private LayoutInflater layoutInflater;

    public TabViewFactory(LayoutInflater layoutInflater){

        this.layoutInflater = layoutInflater;
    }

    public View getTabView(MenuItems menuItem){
        View view = layoutInflater.inflate(R.layout.tab_layout,null);
        TextView tabText = (TextView) view.findViewById(R.id.tabText);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tabIcon);

        tabText.setText(menuItem.getTitle());
        tabIcon.setImageResource(menuItem.getToolbarImageId());

        return view;


    }
}
