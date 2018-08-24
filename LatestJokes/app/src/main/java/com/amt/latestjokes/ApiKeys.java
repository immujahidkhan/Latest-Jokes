package com.amt.latestjokes;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class ApiKeys {
    public static final String keys = "AIzaSyAP9pWoDaFZVPSCQb2B7CjNtTPqnskPURA";
    public static final String Url = "https://www.googleapis.com/blogger/v3/blogs/3497622098744177519/posts/";
    public static final String CatsUrl = "https://actionzz.blogspot.com/feeds/posts/";
    public static final String OrderByCats = "https://www.googleapis.com/blogger/v3/blogs/3497622098744177519/posts?labels=";

    public static PostService postService = null;
    public static PostOrderByCategoryService postOrderByCategoryService = null;

    public static PostService getPostService() {
        if (postService == null) {
            //create
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postService = retrofit.create(PostService.class);
        }
        return postService;
    }

    public static PostOrderByCategoryService getPostOrderByCategoryService() {
        if (postOrderByCategoryService == null) {
            //create
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postOrderByCategoryService = retrofit.create(PostOrderByCategoryService.class);
        }
        return postOrderByCategoryService;
    }


    public interface PostService {
        @GET
        Call<PostList> getPostList(@Url String url);
    }

    public interface PostOrderByCategoryService {
        @GET("?key=" + keys)
        Call<PostList> getPostOrderByCategoryList();
    }

}