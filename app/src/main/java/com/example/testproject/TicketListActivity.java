package com.example.testproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.api.responseobjects.TicketRO;
import com.example.api.service.TicketClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.testproject.AviaMainActivity.TAG;

public class TicketListActivity extends AppCompatActivity  implements View.OnClickListener {

    static DBHelper dbHelper;
    static ArrayList<TicketShowed> tickets;
    static SQLiteDatabase database;
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "Cat channel";
    private int idNotification=1;
    Intent notificationIntent;
    PendingIntent contentIntent;
    private boolean checkprice=false;
    private Handler mHandler = new Handler();
    private SharedPreferences sharedPrefs;
    public static final String APP_PREFERENCES = "mysettings";

    TextView out;
    ListView listWithTickets;
    Button btnClear;
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tickets);
        findByIds();
        sharedPrefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        //УВЕДОМЛЕНИЯ
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My channel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My channel description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(false);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }}
        notificationIntent = new Intent(TicketListActivity.this, TicketListActivity.class);
        contentIntent = PendingIntent.getActivity(TicketListActivity.this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        tickets=new ArrayList<>();
        read();
        if (tickets.size()!=0)   {
        showTickets();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==findViewById(R.id.btnClear).getId()){
//            clearSpisok();
//            tickets= new ArrayList<TicketShowed>();
//            showTickets();
                mHandler.removeCallbacks(checkPrices);
                mHandler.post(checkPrices);

        }
    }

    public void findByIds(){
        out=findViewById(R.id.out);
        listWithTickets=findViewById(R.id.ListWithTickets);
        btnClear=findViewById(R.id.btnClear);
    }

    public void read(){
        tickets=new ArrayList<TicketShowed>();
        Cursor cursor = database.query(DBHelper.TABLE_TICKETS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int fromPlaceIndex = cursor.getColumnIndex(DBHelper.KEY_FROMPLACE);
            int toPlaceIndex = cursor.getColumnIndex(DBHelper.KEY_TOPLACE);
            int gateIndex = cursor.getColumnIndex(DBHelper.KEY_GATE);
            int wantPriceIndex = cursor.getColumnIndex(DBHelper.KEY_WANTPRICE);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
            do {
                TicketShowed tempTicket=new TicketShowed(cursor.getString(idIndex), cursor.getString(fromPlaceIndex), cursor.getString(toPlaceIndex),
                        cursor.getString(gateIndex), cursor.getString(dateIndex), cursor.getDouble(priceIndex), cursor.getDouble(wantPriceIndex));
                tickets.add(tempTicket);
            } while (cursor.moveToNext());
        } else {
            Log.d("mLog", "error. 0 rows");
        }
        cursor.close();
    }

    public void showTickets() {
        ArrayAdapter<TicketShowed> adapter = new ArrayAdapter<>(this, R.layout.item_list_tickets, R.id.id_text_list_item, tickets);
        for (TicketShowed ticket: tickets){
            Log.d(TAG,"show 1:"+ticket.getValue().toString());}
        listWithTickets.setAdapter(adapter);
        listWithTickets.setChoiceMode(ListView.CHOICE_MODE_NONE);
        listWithTickets.setClickable(true);
        listWithTickets.setItemsCanFocus(true);
        listWithTickets.setFocusable(true);
        listWithTickets.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String idDeletedTicket=tickets.get(i).getIdTicket().toString();
                database.delete(DBHelper.TABLE_TICKETS, DBHelper.KEY_ID + " = ?", new String[]{idDeletedTicket});
                tickets.remove(i);
                showTickets();
            }
        });
        }

        public void clearSpisok(){
        database.delete(DBHelper.TABLE_TICKETS, null, null);
        }

        public void notificate(String title, String text){
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setSmallIcon(R.drawable.airplane_material)
                            //ВОзможно тут надо добавить обычный текст еще , хз)))
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                    R.drawable.airplane_material))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setContentIntent(contentIntent)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                            .setAutoCancel(true);
            Notification build = builder.build();
            if (notificationManager != null) {
                notificationManager.notify(idNotification, build);
                idNotification++;
            }
        }

    private Runnable checkPrices = new Runnable() {
        public void run() {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://lyssa.aviasales.ru/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            final TicketClient ticketsClient = retrofit.create(TicketClient.class);

            for (final TicketShowed ticket : tickets){
                Call<TicketRO> call= ticketsClient.reposForTickets(ticket.getFromSTROKA().split(" ")[0],
                        ticket.getToSTROKA().split(" ")[0], ticket.getFromDATE(), ticket.getCurrency(),
                        "0" , "false");

                call.enqueue(new Callback<TicketRO>() {
                    @Override
                    public void onResponse(Call<TicketRO> call, Response<TicketRO> response) {
                        Log.d("Response:", response.body().getPrices().toString());
                        if ( response!= null & response.body().getPrices().size()>0){
                            ticket.setValue(response.body().getPrices().get(0).getValue());
                            ContentValues ticketValues = new ContentValues();
                            ticketValues.put(DBHelper.KEY_PRICE, response.body().getPrices().get(0).getValue());
                            database.update("tickets",
                                    ticketValues,
                                    "from_place = ? AND to_place = ? AND date= ?",
                                    new String[] {ticket.getFromSTROKA(), ticket.getToSTROKA(), ticket.getFromDATE()});
                            Log.d("Value check",ticket.getValue()+" VS "+ticket.getWantValue());
                            if (ticket.getValue() < ticket.getWantValue()){
                                String notificationTitle="Билет достиг желаемой цены!";
                                String notificationText=ticket.getFromSTROKA()+" => "
                                +ticket.getToSTROKA()+" ЦЕНА:"+ticket.getValue();
                                        notificate(notificationTitle, notificationText);
                            }
                        }
                        else{
                            Log.d("UPDATE","Ticket was not find");}
                    }

                    @Override
                    public void onFailure(Call<TicketRO> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + call.request());
                        Log.d(TAG, "Exeption!!!! " + t.getLocalizedMessage());
                    }
                });

            }
            read();
            showTickets();
            //                          ЧАСЫ * МИНУТЫ * СЕКУНДЫ * МИЛИСЕКУНДЫ
            mHandler.postDelayed(this, 1*1*30*1000);
        }
    };

}

