package com.zhan.qiwen.model.topic.node.presenter;

import android.util.Log;

import com.zhan.qiwen.model.base.BaseData;
import com.zhan.qiwen.model.base.BasePresenter;
import com.zhan.qiwen.model.topic.node.data.NodeDataNetwork;
import com.zhan.qiwen.model.topic.node.event.NodesEvent;
import com.zhan.qiwen.model.topic.node.view.NodesView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NodesBasePresenter extends BasePresenter {
    private static final String TAG = "NodesPresenter";
    private NodesView nodesView;
    private BaseData data;

    public NodesBasePresenter(NodesView nodesView) {
        this.nodesView = nodesView;
        data = NodeDataNetwork.getInstance();
    }

    public void readNodes() {
        ((NodeDataNetwork) data).readNodes();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void showNodes(NodesEvent nodesEvent) {
        Log.d(TAG, "showNodes");
        nodesView.showNodes(nodesEvent.getNodeList());
        EventBus.getDefault().removeStickyEvent(nodesEvent);
    }

    @Override public void start() {
        EventBus.getDefault().register(this);
    }

    @Override public void stop() {
        EventBus.getDefault().unregister(this);
    }
}
