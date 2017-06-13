package com.zhan.qiwen.model.topic.data;


import com.zhan.qiwen.model.topic.entity.FavoriteTopic;
import com.zhan.qiwen.model.topic.entity.FollowTopic;
import com.zhan.qiwen.model.topic.entity.Like;
import com.zhan.qiwen.model.topic.entity.Topic;
import com.zhan.qiwen.model.topic.entity.TopicDetail;
import com.zhan.qiwen.model.topic.entity.TopicReply;
import com.zhan.qiwen.model.topic.entity.UnFavoriteTopic;
import com.zhan.qiwen.model.topic.entity.UnFollowTopic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface TopicService {

    /**
     * 获取话题列表
     *
     * @param type 默认值
     * @param offset 默认 0，从第 21 条开始就传 20
     * @param limit 默认 20 范围 [1..150]
     */
    @GET("list/{type}/") Call<List<Topic>> getTopics(@Path("type") int type,@Query("offset") Integer offset,
                                                    @Query("limit") Integer limit);

    /**
     * 获取话题详情
     *
     * @param id 帖子 id
     */
    @GET("topics/{id}.json") Call<TopicDetail> getTopic(@Path("id") int id);

    /**
     * 获取话题评论
     *
     * @param id 帖子 id
     */
    @GET("topics/{id}/replies.json") Call<List<TopicReply>> getReplies(@Path("id") int id,
                                                                       @Query("offset") Integer offset, @Query("limit") Integer limit);

    /**
     * 创建话题
     *
     * @param title 话题标题
     * @param body 话题内容, Markdown 格式
     * @param nodeId 节点编号
     */
    @POST("topics.json") @FormUrlEncoded Call<TopicDetail> newTopic(@Field("title") String title,
                                                                    @Field("body") String body, @Field("node_id") int nodeId);

    /**
     * 收藏话题
     */
    @POST("topics/{id}/favorite.json") Call<FavoriteTopic> favoriteTopic(@Path("id") int id);

    /**
     * 取消收藏话题
     */
    @POST("topics/{id}/unfavorite.json") Call<UnFavoriteTopic> unFavoriteTopic(@Path("id") int id);

    /**
     * 关注话题
     */
    @POST("topics/{id}/follow.json") Call<FollowTopic> followTopic(@Path("id") int id);

    /**
     * 取消关注话题
     */
    @POST("topics/{id}/unfollow.json") Call<UnFollowTopic> unFollowTopic(@Path("id") int id);

    /**
     * 赞
     *
     * @param obj_type ["topic", "reply", "news"]
     */
    @POST("likes.json") @FormUrlEncoded Call<Like> like(@Field("obj_type") String obj_type,
                                                        @Field("obj_id") Integer obj_id);

    /**
     * 取消赞
     *
     * @param obj_type ["topic", "reply", "news"]
     */
    @HTTP(method = "DELETE", path = "likes.json", hasBody = true) @FormUrlEncoded Call<Like> unLike(
            @Field("obj_type") String obj_type, @Field("obj_id") Integer obj_id);

    /**
     * 创建回帖
     *
     * @param id 帖子 id
     * @param body 回帖内容, Markdown 格式
     */
    @POST("topics/{id}/replies.json") @FormUrlEncoded Call<TopicReply> createReply(
            @Path("id") int id, @Field("body") String body);
}
