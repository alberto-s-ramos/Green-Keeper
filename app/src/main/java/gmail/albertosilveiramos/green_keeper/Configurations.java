package gmail.albertosilveiramos.green_keeper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Configurations extends AppCompatActivity {

    private Switch notiSwitch;
    private Switch autoSwitch;
    private TimePicker timePicker;
    private Button setFieldsButton;
    private Button deletePlantButton;
    private Button editPlantButton;
    private ListView irrigationHistory;
    private static ArrayList<String> historyArray = new ArrayList<>();
    private static int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configurations);

        notiSwitch = findViewById(R.id.notiSwitch);
        autoSwitch = findViewById(R.id.autoSwitch);
        timePicker = findViewById(R.id.timePicker);
        setFieldsButton = findViewById(R.id.set_configurations);
        deletePlantButton = findViewById(R.id.deletePlant);
        editPlantButton=findViewById(R.id.editPlant);
        irrigationHistory = findViewById(R.id.plant_irrigation_history);


        irrigationHistory.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setListViewHeightBasedOnChildren(irrigationHistory);

        Collections.sort(historyArray, Collections.reverseOrder());


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, historyArray);
        irrigationHistory.setAdapter(arrayAdapter);



        if(MainActivity.getPlantList().get(pos).getPlant_notifications().equals("ON")){
            notiSwitch.setChecked(true);
        }
        else if(MainActivity.getPlantList().get(pos).getPlant_notifications().equals("OFF")){
            notiSwitch.setChecked(false);
        }

        if(MainActivity.getPlantList().get(pos).getPlant_irrigation_type().equals("MANUAL")){
            autoSwitch.setChecked(false);
            timePicker.setVisibility(View.INVISIBLE);
        }
        else if(MainActivity.getPlantList().get(pos).getPlant_irrigation_type().equals("AUTO")){
            autoSwitch.setChecked(true);
            timePicker.setVisibility(View.VISIBLE);
        }


        notiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        autoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(autoSwitch.isChecked()==true){
                    timePicker.setVisibility(View.VISIBLE);
                }
                else if(autoSwitch.isChecked()==false){
                    timePicker.setVisibility(View.INVISIBLE);
                }
            }
        });


        setFieldsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notiSwitch.isChecked()==true){
                    MainActivity.getPlantList().get(pos).setPlant_notifications("ON");
                }
                else if(notiSwitch.isChecked()==false){
                    MainActivity.getPlantList().get(pos).setPlant_notifications("OFF");
                }

                if(autoSwitch.isChecked()==true){
                    Calendar calendar = Calendar.getInstance();
                    if(Build.VERSION.SDK_INT>=23){
                        calendar.set(calendar.get(Calendar.YEAR),
                                     calendar.get(Calendar.MONTH),
                                     calendar.get(Calendar.DAY_OF_MONTH),
                                     timePicker.getHour(), timePicker.getMinute(),0);
                                Log.d("Horita", "Set. Daily Irrigation set to: "+calendar.get(Calendar.DAY_OF_MONTH)+"."+calendar.get(Calendar.MONTH)+"."+calendar.get(Calendar.YEAR)
                                + "  at  " + timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute()+":00");
                    }
                    else {
                        calendar.set(calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH),
                                timePicker.getCurrentHour(), timePicker.getCurrentMinute(),0);
                                 Log.d("Horita", "Set. Daily Irrigation set to: "+calendar.get(Calendar.DAY_OF_MONTH)+"."+calendar.get(Calendar.MONTH)+"."+calendar.get(Calendar.YEAR)
                                + "  at  " + timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute()+":00");
                    }
                    setAutoIrrigation(calendar.getTimeInMillis());
                    MainActivity.getPlantList().get(pos).setPlant_irrigation_type("AUTO");
                }
                else if(autoSwitch.isChecked()==false){
                    MainActivity.getPlantList().get(pos).setPlant_irrigation_type("MANUAL");
                }

                Intent intent = new Intent(Configurations.this, MainActivity.class );
                startActivity(intent);
            }
        });

        deletePlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.removePlantInList(pos);
                Intent intent = new Intent(Configurations.this, MainActivity.class );
                startActivity(intent);
            }
        });

        editPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit.setPos(pos);
                Intent intent = new Intent(Configurations.this, Edit.class );
                startActivity(intent);
            }
        });



    }

    public static void setPos(int i){
       pos=i;
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setIrrigationHistoryList( ArrayList<String> arrayToSet){
        historyArray.clear();
        for(int i=0; i<arrayToSet.size(); i++){
            historyArray.add(arrayToSet.get(i));
        }
    }

    public void setAutoIrrigation(long timeInMillis){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AutomaticMotor.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,0);
        alarmManager.setRepeating(AlarmManager.RTC, timeInMillis, alarmManager.INTERVAL_DAY,pendingIntent);
        AutomaticMotor.setPos(pos);
        Toast.makeText(this, "Daily Irrigation set", Toast.LENGTH_SHORT).show();
    }



}
