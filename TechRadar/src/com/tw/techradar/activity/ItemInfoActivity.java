package com.tw.techradar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.tw.techradar.R;
import com.tw.techradar.model.RadarItem;

public class ItemInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.activity_item_info);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        RadarItem item = (RadarItem) getIntent().getSerializableExtra(RadarItem.ITEM_KEY);

        displayInfo(item);
    }

    private void displayInfo(RadarItem item) {
        ((TextView)findViewById(R.id.itemTitle)).setText(item.getName());
        ((TextView)findViewById(R.id.itemDesc)).setText(item.getDescription());
        ImageView iconView = (ImageView) findViewById(R.id.itemIcon);
        int icon = (item.getMovement().equals("t"))? R.drawable.triangle_blip2x : R.drawable.circle_blip2x ;
        iconView.setImageResource(icon);
    }
}
