package com.transportervendor.apis;

import com.transportervendor.beans.Bid;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class BidService {
    public static BidService.BidApi bidApi=null;
    public static BidService.BidApi getBidApiInstance(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.serverAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(bidApi==null){
            bidApi=retrofit.create(BidService.BidApi.class);
        }
        return bidApi;
    }
    public interface BidApi{
        @GET("/bid/transporter/{transporterId}")
        public Call<ArrayList<Bid>> getAllBids(@Path("transporterId") String transporterId);
    }
}
