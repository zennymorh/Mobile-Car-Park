package com.example.carpark.views.homefragments;

import android.content.Intent;
import android.os.Bundle;

import com.example.carpark.Api.ParkingApi;
import com.example.carpark.Api.Responses.BaseDataResponse;
import com.example.carpark.Api.RetrofitClient;
import com.example.carpark.Model.Vehicle;
import com.example.carpark.R;
import com.example.carpark.adapter.MyVehicleAdapter;
import com.example.carpark.views.CarDetailsActiviy;
import com.example.carpark.views.TransactionActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyVehicleFragment extends Fragment {
    private ArrayList<Vehicle> vehicleList;
    private RecyclerView recyclerView;
    private MyVehicleAdapter myVehicleAdapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root =  inflater.inflate(R.layout.fragment_my_vehicle, container, false);

        FloatingActionButton fab = root.findViewById(R.id.mv_add_vehicle);
        final TextView new_text = root.findViewById(R.id.new_text);
        progressBar = root.findViewById(R.id.progressBar);
        new_text.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent transactionIntent = new Intent(getContext(), CarDetailsActiviy.class);
                startActivity(transactionIntent);
            }
        });
        recyclerView = root.findViewById(R.id.mv_recyclerView);
        myVehicleAdapter = new MyVehicleAdapter(this.getContext(), vehicleList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);
        vehicleList = new ArrayList<Vehicle>();
        myVehicleAdapter = new MyVehicleAdapter(getContext(), vehicleList );
        recyclerView.setAdapter(myVehicleAdapter);

        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9obmctY2FyLXBhcmstYXBpLmhlcm9rdWFwcC5jb21cL2FwaVwvdjFcL2F1dGhcL3JlZ2lzdGVyXC91c2VyIiwiaWF0IjoxNTcyODc4NDc0LCJleHAiOjE1NzI5ODY0NzQsIm5iZiI6MTU3Mjg3ODQ3NCwianRpIjoidEp4SGJ0OGo1MXFoM25MSSIsInN1YiI6MTIsInBydiI6Ijg3ZTBhZjFlZjlmZDE1ODEyZmRlYzk3MTUzYTE0ZTBiMDQ3NTQ2YWEifQ.vLYVZOEHCk1K79BKzwF2GjdhrTsdgIlfgB3zU6jWEBE";
        ParkingApi parkingApi = RetrofitClient.getInstance().create(ParkingApi.class);
        Call<Vehicle> vehicles = parkingApi.getAllVehicles(token);
        vehicles.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if(response.isSuccessful()){
                    Log.e("Response code", String.valueOf(response.code()));
                    if (response.body()==null){
                        new_text.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }else{
                        new_text.setVisibility(View.INVISIBLE);
                        vehicleList.addAll(response.body().getVehicles());
                        myVehicleAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    Log.e("Response code", String.valueOf(response.code()));
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage()+"Failed to retrieve items", Toast.LENGTH_LONG).show();
                Log.e("On Failure", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
        return root;
    }
}
