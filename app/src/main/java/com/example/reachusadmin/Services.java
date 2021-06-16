package com.example.reachusadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Services extends AppCompatActivity {

    Bundle extras;
    String job;
    Query query;
    FirebaseAuth mAuth;
    FirestoreRecyclerAdapter Adapter;
    FirebaseFirestore fStore;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        recyclerView=findViewById(R.id.recyclerView);
        fStore= FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        extras = getIntent().getExtras();
        if (extras != null) {
            job=extras.getString("Job");
        }

        if(job.equals("Repairing")){
            query = fStore.collection("Services").whereEqualTo("mainJob", "Repairing Service");
        }
        else if(job.equals("Maid")){
            query = fStore.collection("Services").whereEqualTo("mainJob", "Maid Service");
        }
        else if(job.equals("Car")){
            query = fStore.collection("Services").whereEqualTo("mainJob", "Car Service");
        }
        else if(job.equals("Sanitizing")){
            query = fStore.collection("Services").whereEqualTo("mainJob", "Cleaning Service");
        }


        FirestoreRecyclerOptions<servicesAttributes> options=new FirestoreRecyclerOptions.Builder<servicesAttributes>().setQuery(query,servicesAttributes.class).build();

        Adapter = new FirestoreRecyclerAdapter<servicesAttributes, Services.ServicesViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull ServicesViewHolder servicesViewHolder, int i, @NonNull servicesAttributes model) {
                servicesViewHolder.initializeValues(model.getStoreName(),model.getMainJob(), model.getSecondaryJob(),model.getDescription(),model.getPrice(),model.getUserID(),model.getAddress_1(),model.getAddress_2(),model.getPincode(),model.getCity(),model.getDistrict());
                Log.d("Data", model.getStoreName()+"");
            }
            @NonNull
            @Override
            public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_design,parent,false);
                return new Services.ServicesViewHolder(view);
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter);
    }

    private class ServicesViewHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView ServiceName, ServiceDescription, ServicePrice, ServiceType, secondaryServiceType;
        View cardView;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        void initializeValues(String StoreName, String mainJob, String secondaryjob, String Description, String Price, String provideruserID, String addr_1, String addr_2, String pincode, String city, String district) {
            Log.d("data", provideruserID+"");

            ServiceName = (TextView) view.findViewById(R.id.serviceName);
            ServicePrice = (TextView) view.findViewById(R.id.servicePrice);
            ServiceDescription = (TextView) view.findViewById(R.id.serviceDescription);
            ServiceType = view.findViewById(R.id.serviceType);
            secondaryServiceType = view.findViewById(R.id.secondaryServiceType);
            cardView = view.findViewById(R.id.cardView);

            ServiceType.setText(mainJob);
            secondaryServiceType.setText(secondaryjob);
            ServiceName.setText(StoreName);
            ServiceDescription.setText(Description);
            ServicePrice.setText("RS" + Price);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), verifyProviderId.class);
                    intent.putExtra("provideruserId", provideruserID);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Services Fetched", Toast.LENGTH_LONG);
                }
            });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Adapter.stopListening();
    }
}