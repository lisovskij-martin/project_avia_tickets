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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.api.model.city.City;
import com.example.api.model.trip.Hotel;
import com.example.api.responseobjects.CityRO;
import com.example.api.responseobjects.TripRO;
import com.example.api.service.CitiesClient1;
import com.example.api.service.CitiesClient2;
import com.example.api.service.TripClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.text.InputType.TYPE_CLASS_NUMBER;


public class TripMainActivity extends AppCompatActivity {
    static String TAG = "HTTP";
    static String cityIATA="";
    static String cityID="";
    static String cityCurrency="";
    ListView listView;
    Button btn_search, btn_to_avia;
    ArrayList<Hotel> hotelArrayList;
    ArrayList<Hotel> hotelFavouriteList;
    GifImageView search_gifview;
    Calendar dateAndTime;
    ListView listView_city;
    Boolean cityIsSet=false;
    RelativeLayout LL_trip_main;
    ImageButton btn_close_city, btn_dataChoose1, btn_dataChoose2, btn_priceChoose1, btn_priceChoose2;
    EditText editTxtCity;
    TextView txtv_date1, txtv_date2, txtv_price1, txtv_price2;
    private SharedPreferences sharedPrefs;
    public static final String APP_PREFERENCES = "mysettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_trip);
        setInitialValue();
        setListeners();
        sharedPrefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    private void setInitialValue(){
        hotelArrayList =new ArrayList<Hotel>();
        hotelFavouriteList=new ArrayList<Hotel>();
        listView = findViewById(R.id.listview);
        btn_search = findViewById(R.id.btn_search_trip);
        btn_close_city= findViewById(R.id.imgbtn_close_city);
        editTxtCity =findViewById(R.id.edittxt_trip_city);
        txtv_date1=findViewById(R.id.txt_date1);
        txtv_date2=findViewById(R.id.txt_date2);
        txtv_price1=findViewById(R.id.txt_price1);
        txtv_price2=findViewById(R.id.txt_price2);
        LL_trip_main = findViewById(R.id.LL_trip_main);
        search_gifview = findViewById(R.id.search_gifview);
        btn_dataChoose1=findViewById(R.id.btn_dataChoose1);
        btn_dataChoose2=findViewById(R.id.btn_dataChoose2);
        btn_priceChoose1=findViewById(R.id.btn_priceChoose1);
        btn_priceChoose2=findViewById(R.id.btn_priceChoose2);
        listView_city=findViewById(R.id.listView_city);
        btn_to_avia=findViewById(R.id.btn_to_avia);
        dateAndTime=Calendar.getInstance();
        setInitialDateTime(dateAndTime);
    }

    private void setListeners(){
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast toast1=Toast.makeText(getApplicationContext(), "НАЖАЛ!", Toast.LENGTH_SHORT);
//                toast1.show();
                if (cityIsSet && editTxtCity.getText().toString().length()>1 && txtv_date1.getText()!=null && txtv_date2.getText()!=null) {
                    LL_trip_main.setVisibility(View.GONE);
                    search_gifview.setVisibility(View.VISIBLE);
                    searchTrips();
                }


            }
        });

        btn_to_avia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_to_avia=new Intent(TripMainActivity.this, AviaMainActivity.class);
                startActivity(intent_to_avia);
            }
        });

        btn_dataChoose1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TripMainActivity.this, datePickListener1,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        btn_dataChoose2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TripMainActivity.this, datePickListener2,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        btn_priceChoose1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (cityIsSet) showPriceDialog(txtv_price1);
            }
        });

        btn_priceChoose2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (cityIsSet) showPriceDialog(txtv_price2);
            }
        });

        btn_close_city.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                editTxtCity.setText("");
                editTxtCity.setEnabled(true);
                cityIsSet=false;
                listView_city.setAdapter(null);
                listView_city.setVisibility(View.VISIBLE);
            }
        });

        if (checkPermForInternet())
            editTxtCity.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d(TAG, "beforeTextChanged1:" + s);

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "onTextChanged1:" + s);
                    if (s.length()!=0 && !cityIsSet) getCity(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.d(TAG, "afterTextChanged1:" + s);
                }

            });
    }

    private void searchTrips(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://experience.tripster.ru/api/search/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        final TripClient ticketsClient = retrofit.create(TripClient.class);
        Call <TripRO> call= ticketsClient.reposForTrips(
                cityID,
                "json",
                "false",
                "price",
                txtv_price1.getText().toString().split(" ")[0],
                txtv_price2.getText().toString().split(" ")[0],
                txtv_date1.getText().toString(),
                txtv_date2.getText().toString());
        Log.d("WTF",cityID+" "+txtv_price1.getText().toString().substring(0,txtv_price1.getText().toString().length()-1) +" "+
                txtv_price2.getText().toString().substring(0,txtv_price2.getText().toString().length()-1) +" "+
                txtv_date1.getText().toString()+" "+
                txtv_date2.getText().toString());

        call.enqueue(new Callback<TripRO>() {
            @Override
            public void onResponse(Call<TripRO> call, Response<TripRO> response) {
                if ( response!= null && response.body()!=null && response.body().getResults().size()>0){
                    Log.d(TAG, "response.body():" + response.body().getResults().get(0).getTagline());
                    hotelArrayList=new ArrayList<Hotel>();
                    hotelArrayList.addAll(response.body().getResults());

                    Intent intent_to_triplist=new Intent(TripMainActivity.this, TripListActivity.class);
                    intent_to_triplist.putExtra("triplist", hotelArrayList);
                    startActivity(intent_to_triplist);

                }
                else{Toast toast = Toast.makeText( getApplicationContext(), "Экскурсий не найдено!", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    search_gifview.setVisibility(View.GONE);
                    LL_trip_main.setVisibility(View.VISIBLE);
                    v.setTextColor(Color.RED);
                    v.setBackgroundColor(0);
                    toast.show();}
            }

            @Override
            public void onFailure(Call<TripRO> call, Throwable t) {
                search_gifview.setVisibility(View.GONE);
                LL_trip_main.setVisibility(View.VISIBLE);
                Log.d(TAG, "onFailure: " + call.request());
                Log.d(TAG, "Exeption!!!! " + t.getLocalizedMessage());
            }
        });
    }

    private boolean checkPermForInternet(){
        if (Build.VERSION.SDK_INT >= 23) {
            //динамическое получение прав на INTERNET
            if (checkSelfPermission(Manifest.permission.INTERNET)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission is granted");
                return true;
            } else {
                Log.d(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(TripMainActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
            }
        } else {
        }
        return false;
    }

    public void getCity(String cityName) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://places.aviasales.ru/v2/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        CitiesClient1 citiesClient = retrofit.create(CitiesClient1.class);
        Call<List<City>> call = citiesClient.reposForCities(cityName, "ru", "city", "7" );

        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {

                List<City> cities = response.body();
                final ArrayList<String> citiesToListString = new ArrayList<>();

                for (City city : cities) {
                    citiesToListString.add(city.toString());
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(TripMainActivity.this, R.layout.item_list_trip_cities, R.id.text1,  citiesToListString);

                Log.d(TAG, "onResponse: ");
                listView_city.setAdapter(adapter);
                ListView list = listView_city;
                list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        cityIsSet=true;
                        //editTxtCity.setText(citiesToListString.get(position));
                        cityIATA = citiesToListString.get(position).split(" ")[0];
                        adapter.clear();
                        //editTxtCity.setEnabled(false);
                        Log.d("getcityfrom", "cityIATA="+cityIATA);
                        getCity2();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Log.d(TAG, "Exeption!!!! " + t.getLocalizedMessage());
            }
        });
    }

    public void getCity2() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://experience.tripster.ru/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        final CitiesClient2 citiesClient2 = retrofit.create(CitiesClient2.class);
        Call<CityRO> call= citiesClient2.reposForCities(cityIATA);
        call.enqueue(new Callback<CityRO>() {
            @Override
            public void onResponse(Call<CityRO> call, Response<CityRO> response) {
                if ( response!= null && response.body()!=null && response.body().getResults().size()>0){
                    Log.d("getcity2", "response.body().getResults().get(0).getName_ru:"
                            + response.body().getResults().get(0).getName_ru());
                    editTxtCity.setText(response.body().getResults().get(0).getName_ru());
                    cityID=response.body().getResults().get(0).getId();
                    cityCurrency=response.body().getResults().get(0).getCountry().getCurrency();
                    txtv_price1.setText(String.format("%s %s", txtv_price1.getText().toString()
                            .split(" ")[0], cityCurrency));
                    txtv_price2.setText(String.format("%s %s", txtv_price2.getText().toString()
                            .split(" ")[0], cityCurrency));
                    editTxtCity.setEnabled(false);
                    cityIsSet=true;
                }
                else{Toast toast = Toast.makeText( getApplicationContext(), "Экскурсий не найдено!", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    v.setBackgroundColor(0);
                    toast.show();}
            }

            @Override
            public void onFailure(Call<CityRO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call.request());
                Log.d(TAG, "Exeption!!!! " + t.getLocalizedMessage());
            }
        });
    }

    public void setPrefs() {
        Log.d("setPrefs","Город="+editTxtCity.getText().toString());

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("Город", editTxtCity.getText().toString());
        editor.putString("Дата от", txtv_date1.getText().toString());
        editor.putString("Дата до", txtv_date2.getText().toString());
        editor.putString("Цена от", txtv_price1.getText().toString());
        editor.putString("Цена до", txtv_price2.getText().toString());
        editor.putString("cityIATA", cityIATA);
        editor.putString("cityCurrency", cityCurrency);
        editor.apply();
    }

    // метод для получения текста из SharedPreferences по ключу
    public void getPrefs() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Log.d("getPrefs","Город="+sharedPrefs.getString("Город", ""));

        if(sharedPrefs.contains("Город")) {
            cityIsSet=true;
            listView_city.setVisibility(View.GONE);
            editTxtCity.setText(sharedPrefs.getString("Город", ""));
            editTxtCity.setEnabled(editTxtCity.getText().toString().equals(""));
        }

        if(sharedPrefs.contains("Дата от")) {
            txtv_date1.setText(sharedPrefs.getString("Дата от", ""));
        }
        if(sharedPrefs.contains("Дата до")) {
            txtv_date2.setText(sharedPrefs.getString("Дата до", ""));
        }
        if(sharedPrefs.contains("Цена от")) {
            txtv_price1.setText(sharedPrefs.getString("Цена от", ""));
        }
        if(sharedPrefs.contains("Цена до")) {
            txtv_price2.setText(sharedPrefs.getString("Цена до", ""));
        }

        if ((sharedPrefs.contains("cityIATA") && !sharedPrefs.getString("cityIATA", "").equals(""))) {
            cityIATA=sharedPrefs.getString("cityIATA", "");
            getCity2();
        }

        if(sharedPrefs.contains("cityCurrency")) {
            cityCurrency=sharedPrefs.getString("cityCurrency", "");
        }
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity","onResume");
        super.onResume();
        this.getPrefs();
    }

    @Override
    protected void onPause() {
        Log.d("MainActivity","onPause");
        this.setPrefs();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("MainActivity", "onDestroy");
        LL_trip_main.setVisibility(View.VISIBLE);
        search_gifview.setVisibility(View.GONE);
        super.onStop();
    }

    @Override
    protected  void onDestroy() {
        getApplicationContext().deleteSharedPreferences(APP_PREFERENCES);
        super.onDestroy();
    }

    public void showPriceDialog(final TextView textView){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if (textView==txtv_price1) alert.setTitle("Укажите нижний диапазон:");
        else alert.setTitle("Укажите верхний диапазон");
        final EditText input = new EditText(this);
        input.setInputType(TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String temp = String.valueOf(input.getText());
                textView.setText(String.format("%s %s", temp, cityCurrency));
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private final DatePickerDialog.OnDateSetListener datePickListener1=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int SelectedYear, int SelectedMonthOfYear, int SelectedDayOfMonth) {
            String date_selected=new String (new StringBuilder()
                    .append(SelectedYear)
                    .append("-")
                    .append(SelectedMonthOfYear +1)
                    .append("-")
                    .append(SelectedDayOfMonth));
            if (date_selected.toCharArray()[6]=='-') date_selected=date_selected.substring(0,5)+"0"
                    +date_selected.substring(5, date_selected.length()-0);
            if (date_selected.length()==9) date_selected=date_selected.substring(0,8)+"0"
                    +date_selected.substring(8, 9);
            txtv_date1.setText(date_selected);
            view.init(SelectedYear, SelectedMonthOfYear, SelectedDayOfMonth, null);
        }
    };

    private final DatePickerDialog.OnDateSetListener datePickListener2=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int SelectedYear, int SelectedMonthOfYear, int SelectedDayOfMonth) {
            String date_selected=new String (new StringBuilder()
                    .append(SelectedYear)
                    .append("-")
                    .append(SelectedMonthOfYear +1)
                    .append("-")
                    .append(SelectedDayOfMonth));
            if (date_selected.toCharArray()[6]=='-') date_selected=date_selected.substring(0,5)+"0"
                    +date_selected.substring(5, date_selected.length()-0);
            if (date_selected.length()==9) date_selected=date_selected.substring(0,8)+"0"
                    +date_selected.substring(8, 9);
            txtv_date2.setText(date_selected);
            view.init(SelectedYear, SelectedMonthOfYear, SelectedDayOfMonth, null);
        }
    };

    private void setInitialDateTime(Calendar dateAndTime) {
        int year = dateAndTime.get(Calendar.YEAR),
                month = dateAndTime.get(Calendar.MONTH),
                day = dateAndTime.get(Calendar.DAY_OF_MONTH);

        String dateToday=new String (new StringBuilder()
                .append(year)
                .append("-")
                .append(month+1)
                .append("-")
                .append(day));
        if (dateToday.toCharArray()[6]=='-') dateToday=dateToday.substring(0,5)+"0"+dateToday.substring(5, dateToday.length()-0);
        if (dateToday.length()==9) dateToday=dateToday.substring(0,8)+"0"+dateToday.substring(8, 9);
        txtv_date1.setText(dateToday);
        txtv_date2.setText(dateToday);
    }



}