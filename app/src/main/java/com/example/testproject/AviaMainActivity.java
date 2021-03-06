package com.example.testproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.api.model.city.City;
import com.example.api.responseobjects.TicketRO;
import com.example.api.service.CitiesClient1;
import com.example.api.service.NearestCityClient;
import com.example.api.service.TicketClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static com.example.testproject.WorkWithDate.setInitialDateTime;
import static com.example.testproject.WorkWithDate.d;

import retrofit2.converter.gson.GsonConverterFactory;

public class AviaMainActivity extends AppCompatActivity implements View.OnClickListener{

    static String TAG = "HTTP", fromIATA, toIATA, fromSTROKA, toSTROKA, dateFROM, wantVal;
    static Boolean CityFrom=false, CityTo=false, DateIsSet=false, WantWalSsSet=false;
    static Calendar dateAndTime;
    public static DBHelper dbHelper;
    static TextView dateinput, nearestAirport;
    EditText cityInput1, cityInput2;
    TextView txtTicketOut, wantvalue;
    Button save,goToSpisok, btn_to_trip;
    ListView listCity1, listCity2;
    ImageButton imgbtn1, imgbtn2;

    SharedPreferences mSettings;
    public static final String PREFERENCES_LOGIN = "LOGIN";
    public static final String PREFERENCES_PASSWORD = "PASSWORD";
    public static final String PREFERENCES_FILE = "mysettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_avia);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setId();
        setInitialValue();
        setInitialDateTime();
        findNearestAirport();
        dbHelper = new DBHelper(this);

        //РАБОТА С ГОРОДАМИ
        if (checkPermForInternet())
            cityInput1.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d(TAG, "beforeTextChanged1:" + s);
                    listCity1.setVisibility(View.VISIBLE);
                    findViewById(R.id.btn_to_trip).setVisibility(View.GONE);
                    findViewById(R.id.btn_logout).setVisibility(View.GONE);
                    listCity1.setElevation(4 * getApplicationContext().getResources().getDisplayMetrics().density);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "onTextChanged1:" + s);
                    if (s.length()!=0 && !CityFrom) this.getCityFrom(s.toString());
                    //findViewById(R.id.LLcity2).setVisibility(View.INVISIBLE);
                    findViewById(R.id.ListCity2).setVisibility(View.INVISIBLE);
                    findViewById(R.id.LLticketOut).setVisibility(View.INVISIBLE);
                    findViewById(R.id.Cross1).setVisibility(View.VISIBLE);
                }

                @Override
                public void afterTextChanged(Editable s) {

                    Log.d(TAG, "afterTextChanged1:" + s);
                }
                public void getCityFrom(String cityName) {
                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl("https://places.aviasales.ru/v2/")
                            .addConverterFactory(GsonConverterFactory.create());
                    Retrofit retrofit = builder.build();
                    CitiesClient1 citiesClient1 = retrofit.create(CitiesClient1.class);
                    Call<List<City>> call = citiesClient1.reposForCities(cityName, "ru", "city", "7" );

                    call.enqueue(new Callback<List<City>>() {
                        @Override
                        public void onResponse(Call<List<City>> call, Response<List<City>> response) {

                            List<City> cities = response.body();
                            final ArrayList<String> citiesToListString = new ArrayList<>();

                            for (City city : cities) {
                                citiesToListString.add(city.toString());
                            }

                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AviaMainActivity.this, R.layout.item_list_avia_cities, R.id.text1,  citiesToListString);

                            Log.d(TAG, "onResponse: ");
                            listCity1.setAdapter(adapter);
                            ListView list = listCity1;
                            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    CityFrom=true;
                                    cityInput1.setText(citiesToListString.get(position));
                                    fromIATA=citiesToListString.get(position).split(" ")[0];
                                    fromSTROKA=citiesToListString.get(position);
                                    adapter.clear();
                                    cityInput1.setEnabled(false);
                                    cityInput1.setTextSize(18);
                                    //findViewById(R.id.ListCity2).setVisibility(View.VISIBLE);
                                    findViewById(R.id.Cross2).setVisibility(View.VISIBLE);
                                    listCity1.setVisibility(View.GONE);
                                    findViewById(R.id.btn_to_trip).setVisibility(View.VISIBLE);
                                    findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
                                    listCity1.setElevation(0 * getApplicationContext().getResources().getDisplayMetrics().density);
                                    //findViewById(R.id.LLticketOut).setVisibility(View.VISIBLE);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<List<City>> call, Throwable t) {
                            Log.d(TAG, "Exeption!!!! " + t.getLocalizedMessage());
                        }
                    });
                }

            });

        if (checkPermForInternet())
            cityInput2.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d(TAG, "beforeTextChanged2:" + s);
                    //findViewById(R.id.LLcity1).setVisibility(View.INVISIBLE);
                    findViewById(R.id.LLticketOut).setVisibility(View.INVISIBLE);
                    findViewById(R.id.ListCity1).setVisibility(View.GONE);
                    findViewById(R.id.btn_to_trip).setVisibility(View.GONE);
                    findViewById(R.id.btn_logout).setVisibility(View.GONE);
                    listCity2.setVisibility(View.VISIBLE);
                    listCity2.setElevation(4 * getApplicationContext().getResources().getDisplayMetrics().density);
                    findViewById(R.id.Cross2).setVisibility(View.VISIBLE);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "onTextChanged2:" + s);
                    if (s.length()!=0 && !CityTo) this.getCityTo(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.d(TAG, "afterTextChanged2:" + s);
                }

                public void getCityTo(String cityName) {
                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl("https://places.aviasales.ru/v2/")
                            .addConverterFactory(GsonConverterFactory.create());
                    Retrofit retrofit = builder.build();
                    CitiesClient1 citiesClient1 = retrofit.create(CitiesClient1.class);
                    Call<List<City>> call = citiesClient1.reposForCities(cityName, "ru", "city", "7");
                    call.enqueue(new Callback<List<City>>() {
                        @Override
                        public void onResponse(Call<List<City>> call, Response<List<City>> response) {

                            List<City> cities = response.body();
                            final ArrayList<String> citiesToListString = new ArrayList<>();
                            final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AviaMainActivity.this,  R.layout.item_list_avia_cities, R.id.text1,  citiesToListString);

                            for (City city : cities) {
                                citiesToListString.add(city.toString());
                            }
                            Log.d(TAG, "onResponse: ");
                            listCity2.setAdapter(adapter2);
                            ListView list = listCity2;
                            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    CityTo=true;
                                    cityInput2.setText(citiesToListString.get(position));
                                    toIATA=citiesToListString.get(position).split(" ")[0];
                                    toSTROKA=citiesToListString.get(position);
                                    adapter2.clear();
                                    cityInput2.setEnabled(false);
                                    cityInput2.setTextSize(18);
                                    findViewById(R.id.btn_to_trip).setVisibility(View.VISIBLE);
                                    findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
                                    //findViewById(R.id.ListCity1).setVisibility(View.VISIBLE);
                                    //findViewById(R.id.LLticketOut).setVisibility(View.VISIBLE);
                                    findViewById(R.id.Cross1).setVisibility(View.VISIBLE);
                                    listCity2.setVisibility(View.GONE);
                                    listCity1.setElevation(0 * getApplicationContext().getResources().getDisplayMetrics().density);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<List<City>> call, Throwable t) {
                            Log.d(TAG, "Exeption!!!! " + t.getLocalizedMessage());
                        }
                    });
                }
            });
    }

    public void findNearestAirport(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://places.aviasales.ru/v1/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        NearestCityClient nearestCityClient = retrofit.create(NearestCityClient.class);
        Call<List<City>> call = nearestCityClient.reposForGorods("ru");
        Log.d(TAG, "getData: 12321312");
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                nearestAirport.setText(String.format("Ближайший аэропорт: %s", response.body().get(0).toString()));
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Log.d(TAG, "Exeption!!!! " + t.getLocalizedMessage());
            }
        });
    }

    boolean checkPermForInternet(){
        if (Build.VERSION.SDK_INT >= 23) {
            //динамическое получение прав на INTERNET
            if (checkSelfPermission(android.Manifest.permission.INTERNET)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission is granted");
                return true;
            } else {
                Log.d(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(AviaMainActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
            }
        } else {
        }
        return false;
    }

    void setId(){
        listCity1=findViewById(R.id.ListCity1);
        listCity2=findViewById(R.id.ListCity2);
        cityInput1=findViewById(R.id.CityInput1);
        cityInput2=findViewById(R.id.CityInput2);
        imgbtn1=findViewById(R.id.Cross1);
        imgbtn2=findViewById(R.id.Cross2);
        dateinput=findViewById(R.id.DateInput);
        nearestAirport=findViewById(R.id.GPS_location);
        txtTicketOut=findViewById(R.id.txtTicketOut);
        save=findViewById(R.id.save);
        goToSpisok=findViewById(R.id.goToSpisok);
        wantvalue=findViewById(R.id.wantval);
        btn_to_trip=findViewById(R.id.btn_to_trip);
    }

    void setInitialValue(){
        showTicketOut(false);
        mSettings = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        dateAndTime=Calendar.getInstance();
        btn_to_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_to_trip=new Intent(AviaMainActivity.this, TripMainActivity.class);
                startActivity(intent_to_trip);
            }
        });
        listCity2.setAdapter(null);
        listCity2.setVisibility(View.GONE);
        listCity2.setElevation(0 * getApplicationContext().getResources().getDisplayMetrics().density);

        listCity1.setAdapter(null);
        listCity1.setVisibility(View.GONE);
        listCity1.setElevation(0 * getApplicationContext().getResources().getDisplayMetrics().density);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==findViewById(R.id.Cross1).getId()){
            CityFrom=false;
            showTicketOut(false);
            Log.d(TAG, "Click Cross1");
            cityInput2.setEnabled(false);
            cityInput1.setEnabled(true);
            cityInput1.setText("");
            listCity1.setAdapter(null);
            listCity1.setVisibility(View.GONE);
            findViewById(R.id.btn_to_trip).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
            listCity1.setElevation(0 * getApplicationContext().getResources().getDisplayMetrics().density);
        }
        if (view.getId()==findViewById(R.id.Cross2).getId()){
            CityTo=false;
            showTicketOut(false);
            Log.d(TAG, "Click Cross2");
            cityInput1.setEnabled(false);
            cityInput2.setEnabled(true);
            cityInput2.setText("");
            listCity2.setAdapter(null);
            listCity2.setVisibility(View.GONE);
            findViewById(R.id.btn_to_trip).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
            listCity2.setElevation(0 * getApplicationContext().getResources().getDisplayMetrics().density);
        }
        if (view.getId()==findViewById(R.id.btnDataChooser).getId()){
            showTicketOut(false);
            Log.d(TAG, "Click DataChooser");
            DateIsSet=false;
            new DatePickerDialog(AviaMainActivity.this, d,
                    dateAndTime.get(Calendar.YEAR),
                    dateAndTime.get(Calendar.MONTH),
                    dateAndTime.get(Calendar.DAY_OF_MONTH))
                    .show();
            DateIsSet=true;
        }
        if (view.getId()==findViewById(R.id.btnFindTicket).getId()){
            if (AllFieldsAreSet()){
            Log.d(TAG, "click find ticket");
            TicketFounded.setRange(0);
            TicketFounded.setToSTROKA(toSTROKA);
            TicketFounded.setFromSTROKA(fromSTROKA);
            TicketFounded.setToIATA(toIATA);
            TicketFounded.setFromIATA(fromIATA);
            TicketFounded.setFromDATE(dateFROM);
            TicketFounded.setWantvalue(Double.parseDouble(wantVal));
            searchTickets();
            }
            if(!AllFieldsAreSet()){
                Log.d(TAG, "Не все поля введены");
                Toast toast = Toast.makeText(getApplicationContext(), "Не все поля введены", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(Color.RED);
                v.setBackgroundColor(0);
                toast.show();}
        }

        if(view.getId()==findViewById(R.id.btnToHere).getId() && nearestAirport.getText().toString().length()>20){
            CityTo=true;
            cityInput2.setText(nearestAirport.getText().toString().substring(20));
            toIATA=nearestAirport.getText().toString().substring(20).split(" ")[0];
            toSTROKA=nearestAirport.getText().toString().substring(20);
            listCity2.setAdapter(null);
            cityInput2.setEnabled(false);
            cityInput2.setTextSize(18);
        }
        if(view.getId()==findViewById(R.id.btnFromHere).getId()&& nearestAirport.getText().toString().length()>20){
            CityFrom=true;
            cityInput1.setText(nearestAirport.getText().toString().substring(20));
            fromIATA=nearestAirport.getText().toString().substring(20).split(" ")[0];
            fromSTROKA=nearestAirport.getText().toString().substring(20);
            listCity1.setAdapter(null);
            cityInput1.setEnabled(false);
            cityInput1.setTextSize(18);
        }
        if (view.getId()==findViewById(R.id.save).getId()){
            write();
        }
        if (view.getId()==findViewById(R.id.goToSpisok).getId()){
            runSecondActivity();
        }
        if(view.getId()==findViewById(R.id.btnWantVal).getId()){
            dialog();
        }
        if(view.getId()==findViewById(R.id.btn_logout).getId()){
            SharedPreferences.Editor editor = mSettings.edit();
            editor.remove(PREFERENCES_LOGIN);
            editor.remove(PREFERENCES_PASSWORD);
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(intent);
        }
    }

    public void searchTickets(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://lyssa.aviasales.ru/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        final TicketClient ticketsClient = retrofit.create(TicketClient.class);
        Call <TicketRO> call= ticketsClient.reposForTickets(TicketFounded.getFromIATA(),
                TicketFounded.getToIATA(), TicketFounded.getFromDATE(), TicketFounded.getCurrency(),
                TicketFounded.getRange().toString() , "false");
        call.enqueue(new Callback<TicketRO>() {
            @Override
            public void onResponse(Call<TicketRO> call, Response<TicketRO> response) {
                if ( response.body()!=null & response.body().getPrices().size()>0){
                TicketFounded.setValue(response.body().getPrices().get(0).getValue());
                TicketFounded.setGate(response.body().getPrices().get(0).getGate());
                TicketFounded.setWantvalue(Double.parseDouble(wantVal));
                txtTicketOut.setText(TicketFounded.tostring());
                showTicketOut(true);
                }
                else{Toast toast = Toast.makeText(getApplicationContext(), "Билетов не найдено!", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    v.setBackgroundColor(0);
                toast.show();}
            }

            @Override
            public void onFailure(Call<TicketRO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call.request());
                Log.d(TAG, "Exeption!!!! " + t.getLocalizedMessage());
            }
        });
    }

    public boolean AllFieldsAreSet(){
        return(CityFrom && CityTo && DateIsSet && WantWalSsSet);
    }

    public void showTicketOut(boolean show){
        if (show){
            findViewById(R.id.LLticketOut).setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.LLticketOut).setVisibility(View.INVISIBLE);
            save.setVisibility(View.GONE);
        }
    }

    public void runSecondActivity(){
        Intent intent = new Intent(getApplicationContext(), TicketListActivity.class);
        startActivity(intent);
    }

    public void write(){
        TicketFounded.toBase();
    }

    public void dialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Укажите желаему цену:");
        alert.setMessage("При достижении данной цены вы будете уведомлены");
        final EditText input = new EditText(this);
        input.setInputType(TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String temporary = String.valueOf(input.getText());
                wantVal=temporary;
                wantvalue.setText(temporary);
                WantWalSsSet=true;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}

