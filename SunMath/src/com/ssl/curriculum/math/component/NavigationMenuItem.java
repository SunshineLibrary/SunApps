package com.ssl.curriculum.math.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.NextLevelMenuChangedListener;

public class NavigationMenuItem extends FrameLayout {

    private TextView menuItemLabel;
    private NextLevelMenuChangedListener nextLevelChangedListener;

    public NavigationMenuItem(Context context) {
        super(context);
        initUI();
        initListener();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.navigation_item, this, false);
        this.addView(viewGroup);
        menuItemLabel = (TextView) findViewById(R.id.navigation_menu_item_name);
    }

    private void initListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextLevelChangedListener != null) {
                    nextLevelChangedListener.onNextLevelMenu(menuItemLabel.getText().toString());
                }
            }
        });
    }

    public void setMenuName(String name) {
        menuItemLabel.setText(name);
    }

    public void setNextLevelChangedListener(NextLevelMenuChangedListener nextLevelChangedListener) {
        this.nextLevelChangedListener = nextLevelChangedListener;
    }
}
