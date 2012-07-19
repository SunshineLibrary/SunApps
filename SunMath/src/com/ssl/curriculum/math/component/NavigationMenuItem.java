package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.ssl.curriculum.math.R;

public class NavigationMenuItem extends FrameLayout {

    private TextView menuItemLabel;

    public NavigationMenuItem(Context context) {
        super(context);
        initUI();
        menuItemLabel = (TextView) findViewById(R.id.navigation_menu_item_name);
    }

    public NavigationMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.navigation_item, this, false);
        this.addView(viewGroup);
        menuItemLabel = (TextView) findViewById(R.id.navigation_menu_item_name);
    }

    public void setMenuName(String name) {
        menuItemLabel.setText(name);
    }

}
