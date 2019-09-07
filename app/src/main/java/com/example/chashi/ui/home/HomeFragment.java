package com.example.chashi.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chashi.LandingPage;
import com.example.chashi.News;
import com.example.chashi.News_adapter;
import com.example.chashi.R;
import com.example.chashi.ui.gallery.GalleryFragment;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private FusedLocationProviderClient fusedLocationClient;
    double lattitude;
    double longitude;
    ListView listView;
    List<News> newsList;
    DatabaseReference databaseReference;
    private TextView txt_temp_min,txt_temp_max,txt_humidity;
    private LinearLayout linearLayout_product;
    int position_item = -1;
    String news_link,news_heading,news_description;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.news_view);
        newsList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        txt_temp_min = (TextView)view.findViewById(R.id.temp_min);
        txt_temp_max = (TextView)view.findViewById(R.id.temp_max);
        txt_humidity = (TextView)view.findViewById(R.id.humidity);
        linearLayout_product = view.findViewById(R.id.product_btn);
        linearLayout_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new fragment and transaction
                Fragment newFragment = new GalleryFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        createLocationRequest();

        findWeather();

        databaseReference.child("news").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listView.setAdapter(null);
                newsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    News news = ds.getValue(News.class);
                    newsList.add(news);
                }
                News_adapter news_adapter = new News_adapter(getActivity(),newsList);
                listView.setAdapter(news_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (position_item == -1){
                    position_item = i;
                }
                else {
                    return;
                }
                news_link = newsList.get(i).getLinks();
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse(news_link));

                startActivity(httpIntent);
            }
        });
    }

    private void findWeather() {


        String url = "https://api.openweathermap.org/data/2.5/weather?lat="+lattitude+"&lon="+longitude+"&appid=8eded5f15907ac059aebe4f4faeeef9a&units=imperial";
        JsonObjectRequest jobj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainObject = response.getJSONObject("main");
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject object = jsonArray.getJSONObject(0);
                    String temp_min = String.valueOf(mainObject.getDouble("temp_min"));
                    String temp_max = String.valueOf(mainObject.getDouble("temp_max"));
                    String humidity = String.valueOf(mainObject.getDouble("humidity"));
                    String description = object.getString("description");
                    String city = response.getString("name");

                    double temp_min_int = Double.parseDouble(temp_min);
                    double centi = (temp_min_int - 32)/1.8;
                    centi = Math.round(centi);
                    txt_temp_min.setText(String.valueOf(centi));

                    double temp_max_int = Double.parseDouble(temp_max);
                    centi = (temp_min_int - 32)/1.8;
                    centi = Math.round(centi);
                    txt_temp_max.setText(String.valueOf(centi));
                    txt_humidity.setText(humidity);
                    Toast.makeText(getContext(),"hocche",Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jobj);
    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getLocation();
                return;
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // LocationFragment settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),
                                REQUEST_CHECK_SETTINGS);
                        getLocation();
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }else{
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    longitude = location.getLongitude();
                    lattitude = location.getLatitude();

                }
            }).addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to get location", Toast.LENGTH_LONG).show();
                    longitude = 0;
                    lattitude = 0;
                }
            });
        }

    }
}