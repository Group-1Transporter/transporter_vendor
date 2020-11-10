package com.transportervendor.apis;

import com.transportervendor.beans.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class UserService {
    public static UserService.UserApi userApi=null;
    public static UserService.UserApi getLeadsApiInstance(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.serverAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(userApi==null){
            userApi=retrofit.create(UserService.UserApi.class);
        }
        return userApi;
    }
    public interface UserApi{
        @GET("/user/{id}")
        public Call<User>getUser(@Path("id") String id);
    }
}
