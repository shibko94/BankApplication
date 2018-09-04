package com.shibko94.bankapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shibko94.bankapp.model.Currency;

import java.util.List;
import java.util.Locale;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private List<Currency> currenciesList;

    MyRecyclerViewAdapter(List<Currency> currenciesList) {
        this.currenciesList = currenciesList;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;

        MyViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(tv);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.MyViewHolder holder, int position) {
        Currency currency = currenciesList.get(position);
        String text = String.format(Locale.getDefault(), "%s %f BYN\n%s за %d ед.",
                currency.getCharCode(), currency.getRate(),
                currency.getName(), currency.getScale());
        holder.mTextView.setText(text);
    }

    @Override
    public int getItemCount() {
        return currenciesList.size();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Currency tmp = currenciesList.remove(fromPosition);
        currenciesList.add(toPosition > fromPosition ? toPosition - 1 : toPosition, tmp);
        notifyItemMoved(fromPosition, toPosition);
    }
}
