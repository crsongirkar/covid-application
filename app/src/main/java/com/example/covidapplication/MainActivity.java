package com.example.covidapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.sql.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    CountryCodePicker countryCodePicker;

    TextView  mtodaytotal,mtotal,mactive,mtodayactive,mrecoverd,mtodayrecoverd,mdeaths,mtodaydeaths;

    String country;
    TextView mfilter ;
    Spinner spinner;
    String[] types={"active","recovered","deaths","cases"};
    private List<ModelClass> modelClassList;
    private List<ModelClass> modelClassList2;

    PieChart mpiechart;
    private RecyclerView recyclerView;
    com.example.covidapplication.Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        countryCodePicker=findViewById(R.id.ccp);
        mtodayactive=findViewById(R.id.todayactivecase);
                                mactive=findViewById(R.id.activecase);
                                mtodaydeaths=findViewById(R.id.todaydeaths);
                                mdeaths=findViewById(R.id.totaldeaths);
                                mtodayrecoverd=findViewById(R.id.todayRecovred);
                                mrecoverd=findViewById(R.id.recovredcase);
                                mtodaytotal=findViewById(R.id.todaytotal);
                                mtotal=findViewById(R.id.totalcase);

                                mpiechart=findViewById(R.id.piechart);
                                spinner=findViewById(R.id.spinner);
                                mfilter=findViewById(R.id.filter);
                                recyclerView=findViewById(R.id.recylerview);
                                modelClassList=new ArrayList<>();
                                modelClassList2=new ArrayList<>();

                                spinner.setOnItemSelectedListener(this);
                                ArrayAdapter arrayAdapter=new ArrayAdapter( this, android.R.layout.simple_spinner_dropdown_item);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                spinner.setAdapter(arrayAdapter);
                                spinner.setSelection(0,true);


                                ApiUtilities.getApiInterface().getcountrydata().enqueue(new Callback<List<ModelClass>>() {
                                    @Override
                                    public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {

                                        modelClassList2.addAll(response.body());

                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(Call<List<ModelClass>> call, Throwable t) {

                                    }
                                });
                                 
                                adapter =new Adapter(getApplicationContext(),modelClassList2);
                                recyclerView.setLayoutManager(new LinearLayoutManager( this  ));
                                recyclerView .setHasFixedSize(true);
                                recyclerView .setAdapter(adapter);

                                countryCodePicker.setAutoDetectedCountry(true);
                                country=countryCodePicker.getSelectedCountryName();
                                countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
                                    @Override
                                    public void onCountrySelected(){
                                        country=countryCodePicker.getSelectedCountryName();
                                        fetchdata();

                                    }
                                });

                                fetchdata();







                            }

    private void fetchdata() {

        ApiUtilities.getApiInterface().getcountrydata().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {
                    modelClassList.addAll(response.body());
                    for (int i=0; i<modelClassList.size();i++)
                    {
                        if (modelClassList.get(i).getCountry().equals(country))
                        {
                            mactive.setText((modelClassList .get(i).getActive()));
                            mtodaydeaths.setText((modelClassList .get(i).getTodayDeaths()));
                            mtodayrecoverd.setText((modelClassList .get(i).getTodayRecovered()));
                            mtodaytotal.setText((modelClassList .get(i).getTodayCases()));
                            mtotal.setText((modelClassList .get(i).getCases()));
                            mdeaths.setText((modelClassList .get(i).getDeaths()));
                            mrecoverd.setText((modelClassList .get(i).getRecovered()));


                            int active,recovered,deaths,total;

                            active=Integer .parseInt(modelClassList.get(i).getActive());
                            recovered=Integer .parseInt(modelClassList.get(i).getRecovered());
                            deaths=Integer .parseInt(modelClassList.get(i).getDeaths());
                            total=Integer .parseInt(modelClassList.get(i).getCases());

                            updateGraph(active,recovered,deaths,total);









                        }
                    }



            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {

            }
        });




    }

    private void updateGraph(int active, int recovered, int deaths, int total) {

        mpiechart.clearChart();



        mpiechart.addPieSlice(new PieModel("Confirm",total,Color.parseColor("#FFB701")));
        mpiechart.addPieSlice(new PieModel("Active",active,Color.parseColor("#FF4caf50")));
        mpiechart.addPieSlice(new PieModel("Recovered",recovered,Color.parseColor("#38ACCD")));
        mpiechart.addPieSlice(new PieModel("Deaths",deaths,Color.parseColor("#F55c47")));

        mpiechart.startAnimation();


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        String item =types[position];
        mfilter.setText(item);
        adapter.filter(item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
