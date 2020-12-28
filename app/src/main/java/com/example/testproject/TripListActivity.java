package com.example.testproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.api.model.trip.Hotel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TripListActivity extends AppCompatActivity {

    private static final String TAG = "TriplistActivity" ;
    ArrayList<Hotel> hotelArrayList;
    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trips);

        hotelArrayList=(ArrayList<Hotel>) getIntent().getExtras().get("triplist");
        TripAdapter adapter = new TripAdapter(getApplicationContext(), R.layout.item_list_trips, hotelArrayList);

        ListView listView=findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        listView.setClickable(true);
        listView.setItemsCanFocus(true);
        listView.setFocusable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAdvancedInfo(hotelArrayList.get(i));
            }
        });

        btn_back=findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                Intent intent_to_mainactivity=new Intent(TripListActivity.this, TripMainActivity.class);
                startActivity(intent_to_mainactivity);
            }
        });
    }



    public void showAdvancedInfo(final Hotel hotel) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("\""+hotel.getTitle()+"\"");

        LayoutInflater inflater2 = LayoutInflater.from(this);
        View window_info = inflater2.inflate(R.layout.window_trip_info, null);
        dialog.setView(window_info);

        //txtv_info_advanced_1
        TextView txtv_info_advanced_1=window_info.findViewById(R.id.txt_info_advanced_1);
        txtv_info_advanced_1.setText(String.format("Экскурсовод %s", hotel.getGuide().getFirst_name()));
        txtv_info_advanced_1.setTextSize(16);
        txtv_info_advanced_1.setTextColor(Color.parseColor("#001f42"));

        //txtv_info_advanced_2
        TextView txtv_info_advanced_2=window_info.findViewById(R.id.txt_info_advanced_2);
        txtv_info_advanced_2.setText(String.format("%s рейтинг", String.valueOf(hotel.getGuide().getRating())));
        txtv_info_advanced_2.setTextSize(13);

        //txtv_info_price
        TextView txtv_info_price=window_info.findViewById(R.id.txt_info_price);
        txtv_info_price.setTextColor(Color.parseColor("#002958"));
        txtv_info_price.setText(hotel.getPriceString());

        //txtv_info_desc
        TextView txtv_info_desc=window_info.findViewById(R.id.txt_info_desc);
        txtv_info_desc.setTextColor(Color.parseColor("#002958"));
        txtv_info_desc.setText(String.format("Описание: %s", hotel.getTagline()));

        //txtv_info_limits
        TextView txtv_limits = window_info.findViewById(R.id.txt_info_limits);
        txtv_limits.setTextColor(Color.parseColor("#002958"));
        if(hotel.getMax_persons()>0) txtv_limits.setText(String.valueOf(hotel.getMax_persons()));
        txtv_limits.setTextColor(Color.parseColor("#002958"));

        //txtv_info_timelimit
        String tripDurationString="";
        if (hotel.getDuration() < 1)
            tripDurationString=String.valueOf(Double.valueOf(hotel.getDuration()*60).intValue() +" мин");
        else
            tripDurationString=String.valueOf(Double.valueOf(hotel.getDuration()).intValue()+" ч");
        TextView txtv_info_timelimit= window_info.findViewById(R.id.txt_info_timelimit);
        txtv_info_timelimit.setText(tripDurationString);
        txtv_info_timelimit.setTextColor(Color.parseColor("#002958"));

        TextView txtv_info_rating=window_info.findViewById(R.id.txt_info_rating);
        txtv_info_rating.setText(String.format("%s ★", hotel.getRating()));
        txtv_info_rating.setTextColor(Color.parseColor("#002958"));


        //avatar
        ImageView avatar=window_info.findViewById(R.id.image_advanced_info);
        try {
            Picasso.get().load(hotel.getGuide().getAvatar().getSmall()).resize(200, 200).centerCrop()
                    .into(avatar);
        } catch (Exception e) {
            Log.e(TAG, "Could not load image from: " + hotel.getGuide().getAvatar().getSmall());
        }

        dialog.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();



    }

}