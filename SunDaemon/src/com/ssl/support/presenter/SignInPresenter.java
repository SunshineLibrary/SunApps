package com.ssl.support.presenter;

import com.ssl.support.activities.SignInActivity;

/**
 * @author Linfeng Yang
 * @version 1.0
 */
public class SignInPresenter {

    private SignInActivity signInActivity;

    public SignInActivity(SignInActivity signInActivity) {
        this.navigationActivity = navigationActivity;
        mObserver = new MContentObserver(new Handler());

        navigationActivity.setOnActivityClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity((int) id);
            }
        });
    }

}
