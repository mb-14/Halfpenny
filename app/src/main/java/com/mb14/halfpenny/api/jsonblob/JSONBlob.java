package com.mb14.halfpenny.api.jsonblob;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

/**
 * Created by mb-14 on 01/03/16.
 */
public class JSONBlob {
    private static JSONBlobService service;
    private static String baseUrl  =  "https://jsonblob.com/";
    private static final String id = "56d436dbe4b01190df51a5cc";

    public static JSONBlobService getService() {
        if (service == null) {
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = client.create(JSONBlobService.class);
        }
        return service ;
    }

    public interface JSONBlobService
    {
        @GET("api/jsonBlob/"+id)
        Call<Expenses> getExpenses();
        @PUT("api/jsonBlob/"+id)
        Call<Expenses> updateExpenses(@Body Expenses body);
    }
}
