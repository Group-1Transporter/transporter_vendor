package com.transportervendor.apis;

import com.transportervendor.beans.Transporter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class TransporterService {
    public static TransporterApi transporterApi=null;
    public static TransporterApi getTransporterApiInstance(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.serverAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(transporterApi==null){
            transporterApi=retrofit.create(TransporterApi.class);
        }
        return transporterApi;
    }
    public interface TransporterApi{
        @GET("/transporter/{id}")
        public Call<Transporter> getTransporter(@Path("id") String id);

    }
}
