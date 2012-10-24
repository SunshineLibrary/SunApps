package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class HorizListView extends AdapterView<ListAdapter> {

    public HorizListView(Context context) {
        super(context);
    }

    public HorizListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ListAdapter getAdapter() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getSelectedView() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSelection(int position) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
