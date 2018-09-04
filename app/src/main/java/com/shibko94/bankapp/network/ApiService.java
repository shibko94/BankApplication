package com.shibko94.bankapp.network;

import com.shibko94.bankapp.model.DailyExRates;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/XmlExRates")
    Call<DailyExRates> getRates();
}
