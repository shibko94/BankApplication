package com.shibko94.bankapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;

import com.shibko94.bankapp.model.DailyExRates;
import com.shibko94.bankapp.network.ApiService;
import com.shibko94.bankapp.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        getRates();
    }


    private void getRates() {
        if (checkConnection()) {
            ApiService api = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
            Call<DailyExRates> call = api.getRates();
            call.enqueue(new Callback<DailyExRates>() {
                @Override
                public void onResponse(Call<DailyExRates> call, Response<DailyExRates> response) {

                    if (response.body() == null) {
                        showAlert("Неверный формат данных");
                    } else {
                        progressBar.setVisibility(View.GONE);
                        setRecyclerView(response.body());
                    }
                }

                @Override
                public void onFailure(Call<DailyExRates> call, Throwable t) {
                    showAlert("Ошибка ответа от сервера");
                }
            });
        } else {
            showAlert("Нет соединения с интернетом");
        }
    }

    public void setRecyclerView(DailyExRates dailyExRates) {
        final MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(dailyExRates.rates);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.START | ItemTouchHelper.END) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    }

                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                            MyRecyclerViewAdapter.MyViewHolder holder = (MyRecyclerViewAdapter.MyViewHolder) viewHolder;
                            holder.itemView.setBackgroundColor(Color.rgb(217, 221, 226));
                        }
                        super.onSelectedChanged(viewHolder, actionState);
                    }

                    @Override
                    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        MyRecyclerViewAdapter.MyViewHolder holder = (MyRecyclerViewAdapter.MyViewHolder) viewHolder;
                        holder.itemView.setBackgroundColor(0);
                    }
                });
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ошибка");
        builder.setMessage(message);
        builder.setPositiveButton("Повторить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.hide();
                getRates();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.hide();
        }
    }
}
