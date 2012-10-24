package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.adapter.ActivityListAdapter;
import com.ssl.curriculum.math.component.HorizontalListView;
import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.download.manage.DownloadManageActivity;
import com.ssl.curriculum.math.model.Section;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.presenter.NavigationMenuPresenter;
import com.ssl.curriculum.math.presenter.SectionPresenter;

public class NavigationActivity extends Activity implements View.OnClickListener {

    private NavigationMenuPresenter menuPresenter;

    private ImageView iv_back_button;
    private TextView tv_menu_title;
    private NavigationListView navigationListView;

    private RelativeLayout rl_section_details;
    private ImageView iv_download_lesson;
    private ImageView iv_downloading;
    private ImageButton btn_study;
    private ImageButton btn_stat;
    private TextView tv_section_name;
    private ImageView iv_section_thumbnail;
    private TextView tv_section_description;
    private HorizontalListView lv_section_activities;
    private SectionPresenter sectionPresenter;
    private ImageView download_management_entry;

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
        } else if (v == iv_download_lesson){
            sectionPresenter.startDownload();
            showDownloading();
        } else if (v == btn_study) {

        } else if (v == btn_stat) {

        } else if (v == download_management_entry ) {
            Intent intent = new Intent();
            intent.setClass(this, DownloadManageActivity.class);
            this.startActivity(intent);
        }
    }

    public void presentSection(int id) {
        sectionPresenter.presentSection(id);
    }

    public void setSection(Section section) {
        displaySectionDetails();
        setSectionName(section.name);
        setSectionDescription(section.description);
        lv_section_activities.removeAllViewsInLayout();
    }

    public void displaySectionDetails() {
        rl_section_details.setVisibility(View.VISIBLE);
    }

    public void hideSectionDetails() {
        rl_section_details.setVisibility(View.INVISIBLE);
    }

    public void setSectionName(String title) {
        tv_section_name.setText(title);
    }

    public void setSectionDescription(String description) {
        tv_section_description.setText(description);
    }

    public void setSectionActivities(Cursor cursor){
        ActivityListAdapter adapter = new ActivityListAdapter(this, cursor);
        lv_section_activities.setAdapter(adapter);
    }

    public void notifySectionContentChange() {
        ActivityListAdapter adapter = (ActivityListAdapter) lv_section_activities.getAdapter();
        adapter.notifyContentChange();
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

    public void setOnActivityClickListener(AdapterView.OnItemClickListener listener) {
        lv_section_activities.setOnItemClickListener(listener);

    }

    public void showDownloadLesson(){
        iv_download_lesson.setVisibility(View.VISIBLE);
        iv_download_lesson.setEnabled(true);
        iv_downloading.setVisibility(View.INVISIBLE);
    }

    public void showDownloading(){
        iv_download_lesson.setVisibility(View.INVISIBLE);
        iv_downloading.setVisibility(View.VISIBLE);
    }

    public void showDownloaded() {
        iv_download_lesson.setVisibility(View.VISIBLE);
        iv_download_lesson.setEnabled(false);
        iv_downloading.setVisibility(View.INVISIBLE);
    }

    private void initUI(){
        setContentView(R.layout.navigation_layout);
        tv_menu_title = (TextView) findViewById(R.id.tv_menu_title);
        iv_back_button = (ImageView) findViewById(R.id.navigation_menu_back_btn);
        navigationListView = (NavigationListView) findViewById(R.id.navi_list_view);
        rl_section_details = (RelativeLayout) this.findViewById(R.id.section_details);
        tv_section_name = (TextView) findViewById(R.id.tv_section_name);
        iv_download_lesson = (ImageView)findViewById(R.id.iv_navi_activity_download_lesson);
        iv_downloading = (ImageView)findViewById(R.id.iv_navi_activity_downloading);
        btn_study = (ImageButton)findViewById(R.id.btn_navi_activity_study);
        btn_stat = (ImageButton)findViewById(R.id.btn_navi_activity_statistic);
        iv_section_thumbnail =(ImageView)findViewById(R.id.iv_section_thumbnail);
        tv_section_description = (TextView) this.findViewById(R.id.tv_section_description);
        lv_section_activities = (HorizontalListView) findViewById(R.id.lv_section_activities);
        download_management_entry = (ImageView)findViewById(R.id.download_management_entry) ;
    }

    private void initComponent() {
        menuPresenter = new NavigationMenuPresenter(this);
        sectionPresenter = new SectionPresenter(this);
        navigationListView.setNextLevelMenuChangedListener(menuPresenter);
        iv_back_button.setOnClickListener(this);
        iv_download_lesson.setOnClickListener(this);
        btn_study.setOnClickListener(this);
        btn_stat.setOnClickListener(this);
        download_management_entry.setOnClickListener(this);
        initSectionActivitiesView();
    }

    private void initSectionActivitiesView() {
        CursorAdapter adapter = new ActivityListAdapter(this, null);
        lv_section_activities.setAdapter(adapter);
    }
}
