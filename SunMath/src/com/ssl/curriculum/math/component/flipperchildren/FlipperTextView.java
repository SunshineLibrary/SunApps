package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.sunshine.metadata.provider.MetadataContract;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: mendlin
 * Date: 12-8-13
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public class FlipperTextView extends FlipperChildView {

    private TextView notesTextView;
    private TextView mainTextView;
    private byte[] buffer = new byte[5000];

    public FlipperTextView(Context context, DomainActivityData data) {
        super(context);
        initUI();
        initComponents(data);
    }

    private void initComponents(DomainActivityData data) {
        notesTextView = (TextView) findViewById(R.id.flipper_text_notes);
        notesTextView.setText(data.notes);
        mainTextView = (TextView) findViewById(R.id.flipper_text_main);
        FileDescriptor descriptor = getTextFileDescriptor(data.activityId);
        if (descriptor == null)
            Log.d("Descriptor", "Descriptor is null");
        else {
            Log.d("Descriptor", "Descriptor is not null");
            try {
                FileInputStream fis = new FileInputStream(descriptor);
                fis.read(buffer);
                String s = new String(buffer);
                mainTextView.setText(s);
            } catch(IOException e) {
                Log.e("TextView", "FileInputStream error");
            }
        }
    }

    private FileDescriptor getTextFileDescriptor(int activityId) {
        ParcelFileDescriptor pfdInput = null;
        try {
            pfdInput = getContext().getContentResolver().openFileDescriptor(MetadataContract.Activities.getActivityTextUri(activityId), "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pfdInput.getFileDescriptor();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.flipper_text_layout, this, false);
        addView(viewGroup);
    }
}
