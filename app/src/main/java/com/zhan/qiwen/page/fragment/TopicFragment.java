package com.zhan.qiwen.page.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhan.qiwen.R;
import com.zhan.qiwen.model.base.BasePresenter;
import com.zhan.qiwen.model.channel.entity.Channel;
import com.zhan.qiwen.model.topic.entity.Topic;
import com.zhan.qiwen.model.topic.presenter.TopicsPresenter;
import com.zhan.qiwen.model.topic.view.TopicsView;
import com.zhan.qiwen.page.adapter.topic.Footer;
import com.zhan.qiwen.page.adapter.topic.FooterViewProvider;
import com.zhan.qiwen.page.adapter.topic.TopicViewProvider;
import com.zhan.qiwen.page.widget.DividerListItemDecoration;
import com.zhan.qiwen.page.widget.EmptyRecyclerView;
import com.zhan.qiwen.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class TopicFragment extends Fragment implements TopicsView {
    public static final String TYPE = "type";
    public static final String CHANNEL = "channel";
    public static final int TYPE_ALL = 1;
    public static final int TYPE_CREATE = 2;
    public static final int TYPE_FAVORITE = 3;
    private static final String TAG = "TopicFragment";
    @BindView(R.id.rv)
    EmptyRecyclerView rv;
    @BindView(R.id.empty_view)
    TextView emptyView;
    private MultiTypeAdapter adapter;
    private Items items;
    private LinearLayoutManager linearLayoutManager;
    private BasePresenter topicsBasePresenter;
    private int offset = 0;
    private int type = 0;
    private String loginName;
    // 标记 Fragment 是否是第一次初始化
    private boolean isFirstLoad = true;
    private Channel channel;

    public static TopicFragment newInstance(Channel channel) {
        TopicFragment topicFragment = new TopicFragment();
        Bundle b = new Bundle();
        b.putInt(TYPE, 1);
        b.putParcelable(CHANNEL, channel);
        topicFragment.setArguments(b);
        return topicFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    private void initAdapter() {
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        adapter.register(Topic.class, new TopicViewProvider());
        adapter.register(Footer.class, new FooterViewProvider());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_topic, container, false);
        ButterKnife.bind(this, rootView);
        initRV();
        return rootView;
    }

    // 初始化默认 RV
    private void initRV() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerListItemDecoration(getContext()));
        rv.setEmptyView(emptyView);
        loadMore();
    }

    private void loadMore() {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    ((Footer) items.get(items.size() - 1)).setStatus(Footer.STATUS_LOADING);
                    adapter.notifyItemChanged(adapter.getItemCount());
                    if (type == TYPE_ALL) {
                        ((TopicsPresenter) topicsBasePresenter).getTopics(offset);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public void showTopics(List<Topic> topicList) {
        if (topicList == null) {
            Log.v(TAG, "showTopics: null");
            return;
        }
        Log.v(TAG, "showTopics: " + topicList.size());
        if (items.size() == 0) {
            items.add(new Footer(Footer.STATUS_NORMAL));
        }
        for (Topic topic : topicList) {
            // 插入 FooterView 前面
            items.add(items.size() - 1, topic);
            adapter.notifyItemInserted(adapter.getItemCount() - 1);
        }
        offset = items.size() - 1;
        if (topicList.size() < 20) {
            ((Footer) items.get(items.size() - 1)).setStatus(Footer.STATUS_NO_MORE);
        } else {
            ((Footer) items.get(items.size() - 1)).setStatus(Footer.STATUS_NORMAL);
        }
        adapter.notifyItemChanged(adapter.getItemCount());
    }

    @Override
    public void showTopTopics(List<Topic> topicList) {
        if (topicList == null) {
            Log.v(TAG, "showTopTopics: null");
            return;
        }
        Log.v(TAG, "showTopTopics: " + topicList.size());
        int size = topicList.size();
        for (int i = 0; i < size; i++) {
            items.add(i, topicList.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();

        Bundle bundle = getArguments();
        if (bundle != null) {
            loginName = bundle.getString(Constant.User.LOGIN);
            type = bundle.getInt(TYPE);
            Log.d(TAG, "loginName: " + loginName + " type: " + type);
        }

        if (type == TYPE_ALL) {
            topicsBasePresenter = new TopicsPresenter(this);
        }
        topicsBasePresenter.start();
        Log.v(TAG, "isFirstLoad: " + isFirstLoad);
        if (isFirstLoad) {
            if (!TextUtils.isEmpty(loginName)) {

            } else {
                // TODO 置顶帖子的获取
                ((TopicsPresenter) topicsBasePresenter).getTopTopics();
                ((TopicsPresenter) topicsBasePresenter).getTopics(offset);
            }
            // 标记 Fragment 已经进行过第一次加载
            isFirstLoad = false;
        }
    }

    @Override
    public void onStop() {
        if (topicsBasePresenter != null) {
            topicsBasePresenter.stop();
        }
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(TAG, "onAttach");
    }
}
