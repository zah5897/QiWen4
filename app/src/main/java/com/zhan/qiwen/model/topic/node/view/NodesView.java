package com.zhan.qiwen.model.topic.node.view;


import com.zhan.qiwen.model.base.BaseView;
import com.zhan.qiwen.model.topic.node.entity.Node;

import java.util.List;

/**
 * Created by plusend on 2016/12/5.
 */

public interface NodesView extends BaseView {
    void showNodes(List<Node> nodeList);
}
