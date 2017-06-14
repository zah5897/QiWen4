package com.zhan.qiwen.page.adapter.element;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.zhan.qiwen.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

public class FooterViewProvider extends ItemViewProvider<Footer, FooterViewProvider.ViewHolder> {

    @NonNull @Override protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
        @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_topic_reply_load_more, parent, false);
        return new ViewHolder(root);
    }

    @Override protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Footer footer) {
        switch (footer.getStatus()) {
            case Footer.STATUS_NORMAL:
                footer.setStatus(Footer.STATUS_LOADING);
                needLoadMore();
            case Footer.STATUS_LOADING:
                holder.tips.setText("加载中");
                holder.progressBar.setVisibility(View.VISIBLE);
                break;
            case Footer.STATUS_NO_MORE:
                holder.tips.setText("没有更多了");
                holder.progressBar.setVisibility(View.GONE);
                break;
        }

    }

    public void needLoadMore(){};
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tips) TextView tips;
        @BindView(R.id.progress_bar) ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}