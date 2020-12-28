package com.example.testproject;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import static com.example.testproject.AviaMainActivity.dateinput;
import static com.example.testproject.AviaMainActivity.dateAndTime;
import static com.example.testproject.AviaMainActivity.dateFROM;
import static com.example.testproject.AviaMainActivity.DateIsSet;

public class WorkWithDate {
    public static void setInitialDateTime() {
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
        dateinput.setText(dateToday);
        dateFROM=dateinput.getText().toString();
        DateIsSet=true;
    }

    static DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int SelectedYear, int SelectedMonthOfYear, int SelectedDayOfMonth) {
            DateIsSet=true;
            int year=SelectedYear, month=SelectedMonthOfYear, day=SelectedDayOfMonth;
            String dateTodey=new String (new StringBuilder()
                    .append(year)
                    .append("-")
                    .append(month+1)
                    .append("-")
                    .append(day));
            if (dateTodey.toCharArray()[6]=='-') dateTodey=dateTodey.substring(0,5)+"0"+dateTodey.substring(5, dateTodey.length()-0);
            if (dateTodey.length()==9) dateTodey=dateTodey.substring(0,8)+"0"+dateTodey.substring(8, 9);
            dateinput.setText(dateTodey);
            dateFROM=dateinput.getText().toString();
            view.init(year, month, day, null);
        }
    };
}
