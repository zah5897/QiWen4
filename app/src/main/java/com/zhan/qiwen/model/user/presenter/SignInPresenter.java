package com.zhan.qiwen.model.user.presenter;

import android.util.Log;

import com.zhan.qiwen.model.base.BaseData;
import com.zhan.qiwen.model.base.BasePresenter;
import com.zhan.qiwen.model.user.event.TokenEvent;
import com.zhan.qiwen.model.user.model.UserDataNetwork;
import com.zhan.qiwen.model.user.view.SignInView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SignInPresenter extends BasePresenter {
    private static final String TAG = "SignInPresenter";
    private SignInView signInView;
    private BaseData data;

    public SignInPresenter(SignInView signInView) {
        this.signInView = signInView;
        this.data = UserDataNetwork.getInstance();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void getToken(TokenEvent tokenEvent) {
        signInView.getToken(tokenEvent.getToken());
    }

    public void getToken(String username, String password) {
        ((UserDataNetwork) data).getToken(username, password);
    }

    @Override public void start() {
        Log.d(TAG, "register");
        EventBus.getDefault().register(this);
    }

    @Override public void stop() {
        Log.d(TAG, "unregister");
        EventBus.getDefault().unregister(this);
    }
}
