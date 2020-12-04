package com.transportervendor.apis;

import com.transportervendor.beans.Transporter;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
        @POST("/transporter/update")
        public  Call<Transporter> updateTransporter(@Body Transporter transporter);
        @Multipart
        @POST("/transporter/")
        public Call<Transporter> createTransporter(@Part MultipartBody.Part file,
                                                   @Part("type") RequestBody type ,
                                                   @Part("name") RequestBody name,
                                                   @Part("contactNumber") RequestBody contactNumber,
                                                   @Part("address") RequestBody address,
                                                   @Part("gstNumber") RequestBody gstNumber,
                                                   @Part("rating") RequestBody rating,
                                                   @Part("token") RequestBody token,
                                                   @Part("aadharCardNumber")RequestBody adhar,
                                                   @Part("id") RequestBody transporterId
                                                   );
        @Multipart
        @POST("/transporter/updateImage")
        public Call<Transporter> updateImage(@Part("transporterId") RequestBody transporterId,
                                            @Part MultipartBody.Part file
                                            );
    }
}
