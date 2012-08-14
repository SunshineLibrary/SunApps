package com.ssl.curriculum.math.component;

import java.io.FileNotFoundException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.NextLevelMenuChangedListener;

public class NavigationMenuItem extends FrameLayout {

    private TextView menuItemLabel;
    private int uniqueId = 0;
    private NextLevelMenuChangedListener nextLevelChangedListener;
    private ImageView bgImageView;

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
        bgImageView = (ImageView) findViewById(R.id.navdeck_item_bg_image);
    }

    private void initListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextLevelChangedListener != null) {
						nextLevelChangedListener.onNextLevelMenu(getUniqueId());
					
                }
                
                
            }
        });
        
        
        
    }

    public void setMenuName(String name) {
        menuItemLabel.setText(name);
    }
    
    public void setUniqueId(int id){
    	this.uniqueId = id;
    }
    
    public int getUniqueId(){
    	return this.uniqueId;
    }
    
    public void setNextLevelChangedListener(NextLevelMenuChangedListener nextLevelChangedListener) {
        this.nextLevelChangedListener = nextLevelChangedListener;
    }

    public void active() {
        bgImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_side_list_selected));
    }

    public void deActive() {
        bgImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_side_list_not_selected));
    }
}
