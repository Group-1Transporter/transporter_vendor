package com.transportervendor.apis;

import com.transportervendor.beans.Leads;
import com.transportervendor.beans.Transporter;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class LeadsService {
    public static LeadsService.LeadsApi leadsApi=null;
    public static LeadsService.LeadsApi getLeadsApiInstance(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.serverAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(leadsApi==null){
            leadsApi=retrofit.create(LeadsService.LeadsApi.class);
        }
        return leadsApi;
    }
    public interface LeadsApi{
        @GET("/lead/{leadId}")
        public Call<Leads> getLeads(@Path("leadId") String leadId);
        @POST("/transporter")
        public Call<Transporter> createTransporter(@Body Transporter transporter);
        @DELETE("/transporter/{id}")
        public Call<Transporter> deleteTransporter(@Path("id") String id);
    }
}
