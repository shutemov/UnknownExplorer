package com.example.unknownexplorer.interfaces;

import com.example.unknownexplorer.POJO.PojoPoint;
import com.example.unknownexplorer.POJO.PojoRoute;
import com.example.unknownexplorer.POJO.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JSONPlaceHolderApi {
    @GET("routes/{id}/points")
    public Call<PojoRoute> getRouteWithID(@Path("id") int id);

    @GET("routes")
    public Call<List<PojoRoute>> getAllRoutes();

    @GET("users/{id}/routes")
    public Call<List<PojoRoute>> getMyRoutes(@Path("id") int id);

    @GET("routes/{id}/points")
    public Call<List<PojoPoint>> getRoutePoints(@Path("id") int id);

    @GET("users/{id}")
    public Call<User> getUserWithID(@Path("id") int id);

    @POST("users/check")
    public Call<User> getUserExist(@Body User data);

    @POST("users")
    public Call<User> createNewUser(@Body User data);

    @POST("users/{id}/routes")
    public Call<PojoRoute> createNewRoute(@Path("id") int id, @Body PojoRoute data);

    @POST("routes/{id}/points")
    public Call<PojoPoint> createNewPoint(@Path("id") int id, @Body PojoPoint data);

    @POST("points/{id}/remove")
    public Call<PojoPoint> deletePoint(@Path("id") int id, @Body PojoPoint data);

    @POST("routes/{id}/remove")
    public Call<PojoRoute> deleteRoute(@Path("id") int id, @Body PojoRoute data);

    @POST("routes/{id}/update")
    public Call<PojoRoute> updateRoute(@Path("id") int id, @Body PojoRoute data);

}
