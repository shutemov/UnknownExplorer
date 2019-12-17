package com.example.unknownexplorer.interfaces;

import com.example.unknownexplorer.POJO.PojoPoint;
import com.example.unknownexplorer.POJO.PojoRoute;
import com.example.unknownexplorer.POJO.PojoUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JSONPlaceHolderApi {
    @GET("routes/{id}/points")
    Call<PojoRoute> getRouteWithID(@Path("id") int id);

    @GET("routes")
    Call<List<PojoRoute>> getAllRoutes();

    @POST("routes/{id}")
    Call<List<PojoRoute>> getRouteById(@Path("id") int id, @Body PojoRoute pojoRoute);

    @GET("users/{id}/routes")
    Call<List<PojoRoute>> getMyRoutes(@Path("id") int id);

    @GET("routes/{id}/points")
    Call<List<PojoPoint>> getRoutePoints(@Path("id") int id);

    @GET("users/{id}")
    Call<PojoUser> getUserById(@Path("id") int id);

    @POST("users/check")
    Call<PojoUser> getUserExist(@Body PojoUser data);

    @POST("users")
    Call<PojoUser> createNewUser(@Body PojoUser data);

    @POST("users/{id}/routes")
    Call<PojoRoute> createNewRoute(@Path("id") int id, @Body PojoRoute data);

    @POST("routes/{id}/points")
    Call<PojoPoint> createNewPoint(@Path("id") int id, @Body PojoPoint data);

    @POST("points/{id}/remove")
    Call<PojoPoint> deletePoint(@Path("id") int id);

    @POST("routes/{id}/remove")
    Call<PojoRoute> deleteRoute(@Path("id") int id, @Body PojoRoute data);

    @POST("routes/{id}/update")
    Call<PojoRoute> updateRoute(@Path("id") int id, @Body PojoRoute data);
}
