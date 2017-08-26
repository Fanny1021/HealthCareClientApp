package com.fanny.healthcareclient.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.utils.DateUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

/**
 * Created by Fanny on 17/7/27.
 */

public class CalenderDialog extends DialogFragment implements View.OnClickListener {

    private Dialog dialog;
    private View view;
    private TextView tv_year;
    private TextView tv_month;
    private TextView tv_day;
    private TextView tv_daynumber;
    private TextView tv_ok;
    private TextView tv_cancle;
    private MaterialCalendarView calender;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_calender, null);
        tv_year = (TextView) view.findViewById(R.id.tv_year);
        tv_month = (TextView) view.findViewById(R.id.tv_month);
        tv_day = (TextView) view.findViewById(R.id.tv_day);
        tv_daynumber = (TextView) view.findViewById(R.id.tv_day_number);

        tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);


        calender = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        calender.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();
        calender.setSelectedDate(instance.getTime());
//        Calendar instance1 = Calendar.getInstance();
//        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);
//        Calendar instance2 = Calendar.getInstance();
//        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        calender.state().edit()
//                .setMinimumDate(instance1.getTime())
//                .setMaximumDate(instance2.getTime())
                .setMinimumDate(CalendarDay.from(1990,1,1))
                .setMaximumDate(CalendarDay.from(2200,12,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();


        dialog = new Dialog(getActivity());
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager manager = getActivity().getWindowManager();
        Display display = manager.getDefaultDisplay();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = (int) (display.getHeight() * 0.85);
        dialogWindow.setAttributes(lp);

        return dialog;
    }

    public MyListener myListener;
    public interface MyListener{
        public void setData(String str);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myListener= (MyListener) activity;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_year:

                break;
            case R.id.tv_ok:
                if(calender!=null && calender.getSelectedDate()!=null)
                {
                    String result=(calender.getSelectedDate().getDate().getYear()+1900)+"-"+
                            (calender.getSelectedDate().getDate().getMonth()+1)+"-"+
                            calender.getSelectedDate().getDate().getDate();
                    myListener.setData(result);
                }
//                dialog.setCanceledOnTouchOutside(true);
                dialog.dismiss();
                break;
            case R.id.tv_cancle:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_year.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);

        CalendarDay myDate = calender.getSelectedDate();
        if(myDate==null){
            Log.e("date",myDate+"");
        }else {
            Date date0 = myDate.getDate();
            int year = date0.getYear();
            int month = date0.getMonth();//0~11
            int week = date0.getDay();//0~6
            int day = date0.getDate();
            Log.e("date",""+month+";"+week+";"+day+";"+year);

            tv_year.setText(""+(year+1900));
            tv_month.setText(""+(month+1));
            tv_day.setText(""+day);
            tv_daynumber.setText(DateUtil.getWeek(week));
        }


        calender.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                CalendarDay selectedDate=widget.getSelectedDate();
                if(selectedDate!=null || selected==true){
                    Date date0 = date.getDate();
                    int year = date0.getYear();
                    int month = date0.getMonth();//0~11
                    int week = date0.getDay();//0~6
                    int day = date0.getDate();
                    Log.e("date",""+month+";"+week+";"+day+";"+year);

                    tv_year.setText(""+(year+1900));
                    tv_month.setText(""+(month+1));
                    tv_day.setText(""+day);
                    tv_daynumber.setText(DateUtil.getWeek(week));
                }
            }
        });
    }
}
