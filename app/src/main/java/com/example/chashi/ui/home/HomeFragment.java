package com.example.chashi.ui.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chashi.Disease;
import com.example.chashi.FirebaseUtilClass;
import com.example.chashi.LandingPage;
import com.example.chashi.News;
import com.example.chashi.NewsAdapter;
import com.example.chashi.R;
import com.example.chashi.ScanDiseaseCropsFragment;
import com.example.chashi.ui.forums.ForumFragment;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    Dialog dialog_news,dialog_weather;
    private HomeViewModel homeViewModel;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private FusedLocationProviderClient fusedLocationClient;
    double lattitude;
    double longitude;
    RecyclerView listView;
    List<News> newsList;
    NewsAdapter newsAdapter;
    DatabaseReference databaseReference;
    private TextView txt_temp_min, txt_temp_max, txt_humidity;
    private LinearLayout linearLayout_product, linearLayout_disease;
    int position_item = -1;
    String news_link, news_heading, news_description;
    Button button_news,button_weather;
    TextView textView_date;
    ImageView imageView_weather;
    private LinearLayout linearLayout_scan, linearLayout_forum;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onResume() {
        ((LandingPage) getActivity())
                .setActionBarTitle("প্রধান পাতা");

        super.onResume();

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        dialog_news = new Dialog(getActivity());
        textView_date = view.findViewById(R.id.date);
        imageView_weather = view.findViewById(R.id.weather_icon);
        txt_temp_min = (TextView) view.findViewById(R.id.temp_min);
        txt_temp_max = (TextView) view.findViewById(R.id.temp_max);
        txt_humidity = (TextView) view.findViewById(R.id.humidity);
        listView = view.findViewById(R.id.news_view);
        newsList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        txt_temp_min = (TextView) view.findViewById(R.id.temp_min);
        txt_temp_max = (TextView) view.findViewById(R.id.temp_max);
        txt_humidity = (TextView) view.findViewById(R.id.humidity);
        linearLayout_product = view.findViewById(R.id.product_btn);
        linearLayout_forum = view.findViewById(R.id.forum_btn);
        linearLayout_scan = view.findViewById(R.id.diseaseCheck_btn);
        linearLayout_disease = view.findViewById(R.id.disease_btn);
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

        linearLayout_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new fragment and transaction
                Fragment newFragment = new ScanDiseaseCropsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        linearLayout_disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Disease.class);
                getContext().startActivity(intent);
            }
        });

        linearLayout_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new fragment and transaction
                Fragment newFragment = new ForumFragment();
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

        newsAdapter = new NewsAdapter(newsList,getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(newsAdapter);

        FirebaseUtilClass.getDatabaseReference().child("news").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //listView.setAdapter(null);
                //newsList.clear();
                //listView.setAdapter(null);
                newsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    News news = ds.getValue(News.class);
                    newsList.add(news);
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button_news = view.findViewById(R.id.subcription_new);
        button_weather = view.findViewById(R.id.subcription_weather);

        button_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //news subscription button action
                dialog_news.setContentView(R.layout.pop_ups_news);
                dialog_news.show();
                Button button_done;
                ImageView button_hide_news_popup;
                button_hide_news_popup = dialog_news.findViewById(R.id.hide_news_popup);
                button_done = dialog_news.findViewById(R.id.subscription_done_news);
                button_hide_news_popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_news.dismiss();
                    }
                });
                button_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //subscription done here
                        Toast.makeText(getContext(),"News subscription done!",Toast.LENGTH_SHORT).show();
                        dialog_news.dismiss();
                    }
                });
            }
        });

        dialog_weather = new Dialog(getContext());
        button_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_weather.setContentView(R.layout.pop_ups_weather);
                dialog_weather.show();
                Button button_done;
                ImageView button_hide_weather_popup;
                button_hide_weather_popup = dialog_weather.findViewById(R.id.hide_weather_popup);
                button_done = dialog_weather.findViewById(R.id.subscription_done_weather);
                button_hide_weather_popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_weather.dismiss();
                    }
                });
                button_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //subscribe done here
                        Toast.makeText(getContext(),"Weather update subscription done!",Toast.LENGTH_SHORT).show();
                        dialog_weather.dismiss();
                    }
                });
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (position_item == -1) {
                    position_item = i;
                } else {
                    return;
                }
                news_link = newsList.get(i).getLinks();
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse(news_link));

                startActivity(httpIntent);
            }
        });*/

    }

    String TAG = "home activity";
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void findWeather() {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM");
        final String formattedDate = df.format(c);
        Log.d(TAG, "findWeather: "+formattedDate);
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lattitude + "&lon=" + longitude + "&appid=8eded5f15907ac059aebe4f4faeeef9a&units=imperial";
        JsonObjectRequest jobj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainObject = response.getJSONObject("main");
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONArray jsonArray_date = response.getJSONArray("date");
                    JSONObject object = jsonArray.getJSONObject(0);
                    String temp_min = String.valueOf(mainObject.getDouble("temp_min"));
                    String temp_max = String.valueOf(mainObject.getDouble("temp_max"));
                    String humidity = String.valueOf(mainObject.getDouble("humidity"));
                    String description = object.getString("description");
                    String city = response.getString("name");
                    String icon = response.getString("icon");
                    String icon_url = "http://api.openweathermap.org/img/wn/"+icon+"@2x.png";
                    Log.d(TAG, "onResponse: "+icon_url);
                    Picasso.get().load(icon_url).into(imageView_weather);
                    double temp_min_int = Double.parseDouble(temp_min);
                    double centi = (temp_min_int - 32) / 1.8;
                    centi = Math.round(centi);
                    txt_temp_min.setText(String.valueOf(centi));
                    textView_date.setText("১০ কার্তিক । "+formattedDate);
                    double temp_max_int = Double.parseDouble(temp_max);
                    centi = (temp_max_int - 32) / 1.8;
                    centi = Math.round(centi);
                    txt_temp_max.setText(String.valueOf(centi));
                    txt_humidity.setText(humidity+'%');
                    //Toast.makeText(getContext(), "hocche", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
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
        } else {
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