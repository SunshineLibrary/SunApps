package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.*;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.adapter.ActivityListAdapter;
import com.ssl.curriculum.math.component.HorizontalListView;
import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.model.Section;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.presenter.NavigationMenuPresenter;
import com.sunshine.metadata.provider.MetadataContract.Activities;
import com.sunshine.metadata.provider.MetadataContract.SectionComponents;
import com.sunshine.metadata.provider.MetadataContract.Sections;

public class NavigationActivity extends Activity implements View.OnClickListener {

    private static String[] ActivitiesInfo = new String[] {
            BaseColumns._ID,
            SectionComponents._SECTION_ID,
            Activities._NAME,
            Activities._DOWNLOAD_PROGRESS,
            Activities._DOWNLOAD_STATUS,
    };

    private static String[] SectionInfo = new String[] {
            Sections._ID,
            Sections._DESCRIPTION
    };

    private NavigationMenuPresenter menuPresenter;
    private Section currentSection;

    private ImageView iv_back_button;
    private TextView tv_menu_title;
    private NavigationListView navigationListView;

    private LinearLayout ll_section_details;
    private ImageButton btn_download;
    private ImageButton btn_study;
    private ImageButton btn_stat;
    private TextView tv_section_name;
    private ImageView iv_section_thumbnail;
    private TextView tv_section_description;
    private HorizontalListView lv_section_activities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initComponent();
        menuPresenter.loadMenuData();
    }

    @Override
    public void onBackPressed() {
    	if(!menuPresenter.menuBack()){
    		super.onBackPressed();
    	}    	
    }
    
    @Override
    public void onClick(View v) {
        if (v == iv_back_button) {
            menuPresenter.menuBack();
        } else if (v == btn_download){

        } else if (v == btn_study) {

        } else if (v == btn_stat) {

        }
    }

    public void setSection(Section section) {
        currentSection = section;
        displaySectionDetails();
        setSectionName(section.name);
        setSectionDescription(section.description);
    }

    public void displaySectionDetails() {
        ll_section_details.setVisibility(View.VISIBLE);
    }

    public void hideSectionDetails() {
        ll_section_details.setVisibility(View.INVISIBLE);
    }

    public void setSectionName(String title) {
        tv_section_name.setText(title);
    }

    public void setSectionDescription(String description) {
        tv_section_description.setText(description);
    }

    public void setSectionActivities(Cursor cursor){
        ((CursorAdapter) lv_section_activities.getAdapter()).changeCursor(cursor);
    }

    public void activateMenuItem(int id) {
        navigationListView.activateMenuItem(id);
    }

    public void setMenuTitle(String title) {
        tv_menu_title.setText(title);
    }

    public void updateMenu(Menu menu) {
        navigationListView.updateMenu(menu);
    }

    private void initUI(){
        setContentView(R.layout.navigation_layout);
        tv_menu_title = (TextView) findViewById(R.id.tv_menu_title);
        iv_back_button = (ImageView) findViewById(R.id.navigation_menu_back_btn);
        navigationListView = (NavigationListView) findViewById(R.id.navi_list_view);
        ll_section_details = (LinearLayout) this.findViewById(R.id.section_details);
        tv_section_name = (TextView) findViewById(R.id.tv_section_name);
        btn_download = (ImageButton)findViewById(R.id.btn_navi_activity_download);
        btn_study = (ImageButton)findViewById(R.id.btn_navi_activity_study);
        btn_stat = (ImageButton)findViewById(R.id.btn_navi_activity_statistic);
        iv_section_thumbnail =(ImageView)findViewById(R.id.iv_section_thumbnail);
        tv_section_description = (TextView) this.findViewById(R.id.tv_section_description);
        lv_section_activities = (HorizontalListView) findViewById(R.id.lv_section_activities);
    }

    private void initComponent() {
        menuPresenter = new NavigationMenuPresenter(this);
        navigationListView.setNextLevelMenuChangedListener(menuPresenter);
        iv_back_button.setOnClickListener(this);
        btn_download.setOnClickListener(this);
        btn_study.setOnClickListener(this);
        btn_stat.setOnClickListener(this);
        initSectionActivitiesView();
    }

    private void initSectionActivitiesView() {
        CursorAdapter adapter = new ActivityListAdapter(this, null);
        lv_section_activities.setAdapter(adapter);
        lv_section_activities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Cursor cursor =  ((CursorAdapter) lv_section_activities.getAdapter()).getCursor();
                cursor.move(position);
                intent.putExtra("sectionId", currentSection.id);
                intent.putExtra("activityId", cursor.getInt(cursor.getColumnIndex(SectionComponents._ACTIVITY_ID)));
                intent.setClass(NavigationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}