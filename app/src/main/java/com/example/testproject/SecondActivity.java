package com.example.testproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import static com.example.testproject.MainActivity.TAG;

public class SecondActivity extends AppCompatActivity  implements View.OnClickListener {

    static DBHelper dbHelper;
    static ArrayList<TicketShowed> tickets;
    static SQLiteDatabase database;

    TextView out;
    ListView listWithTickets;
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findByIds();
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
            clearSpisok();
            tickets= new ArrayList<TicketShowed>();
            showTickets();
        }
    }

    public void findByIds(){
        out=findViewById(R.id.out);
        listWithTickets=findViewById(R.id.ListWithTickets);
        btnClear=findViewById(R.id.btnClear);
    }

    public void read(){

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
        ArrayAdapter<TicketShowed> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tickets);
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

}

