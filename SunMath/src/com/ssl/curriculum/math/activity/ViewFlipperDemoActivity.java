package com.ssl.curriculum.math.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.ImageViewerFlipper;
import com.ssl.curriculum.math.listener.OnViewFlipperListener;


public class ViewFlipperDemoActivity extends Activity implements OnViewFlipperListener {

    private ImageViewerFlipper imageViewerFlipper;
    private int currentNumber;
    private ImageButton leftbtn;
    private ImageButton rightbtn;
    private ImageButton zoomoutbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_in);

        this.leftbtn = (ImageButton) this.findViewById(R.id.leftbtn);
        this.rightbtn = (ImageButton) this.findViewById(R.id.rightbtn);
        this.zoomoutbtn = (ImageButton) this.findViewById(R.id.zoom_out_btn);

        this.leftbtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                imageViewerFlipper.flingToPrevious();
            }
        });
        this.rightbtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                imageViewerFlipper.flingToNext();
            }
        });

        this.zoomoutbtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });

        currentNumber = 1;
        imageViewerFlipper = (ImageViewerFlipper) findViewById(R.id.myViewFlipper);
        imageViewerFlipper.setOnViewFlipperListener(this);
        imageViewerFlipper.addView(creatView(currentNumber));
    }


    public View getNextView() {
        currentNumber = currentNumber == 10 ? 1 : currentNumber + 1;
        return creatView(currentNumber);
    }

    public View getPreviousView() {
        currentNumber = currentNumber == 1 ? 10 : currentNumber - 1;
        return creatView(currentNumber);
    }

    private View creatView(int currentNumber) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        ScrollView resultView = (ScrollView) layoutInflater.inflate(R.layout.flipper_view, null);

        ((TextView) resultView.findViewById(R.id.textView)).setText(currentNumber + "");
        return resultView;
    }


}  