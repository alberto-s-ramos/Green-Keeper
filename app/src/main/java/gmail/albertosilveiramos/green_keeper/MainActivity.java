package gmail.albertosilveiramos.green_keeper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static gmail.albertosilveiramos.green_keeper.Notifications.CHANNEL_1_ID;

public class  MainActivity extends AppCompatActivity {

    private static Context context;
    private static RecyclerView recyclerView;
    private static MyAdapter adapter;

    private static List<Plant> plantList= new ArrayList<Plant>();
    private static String plant_current_temp ="0";
    private static String plant_current_moist ="0";

    private static String plant_current_temp_AUX ="0";
    private static String plant_current_moist_AUX ="0";


    private int threadCounter=0;

    private Button add_plant_button;

    private Thread thread;
    private AdafruitIO adafruitIO;
    private static final String apiUrl = "https://io.adafruit.com/api/v2/rodrigoagostinhotecnico/feeds/temperature-sensor/?X-AIO-Key=fb9462765fdb4368b841393c3c9adf52";
    private final static String moistureSensorApiUrl = "https://io.adafruit.com/api/v2/rodrigoagostinhotecnico/feeds/moisture-sensor/?X-AIO-Key=fb9462765fdb4368b841393c3c9adf52";


    private NotificationManagerCompat notificationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context= getApplicationContext();

        notificationManager = NotificationManagerCompat.from(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        add_plant_button = findViewById(R.id.add_plant_button);
        add_plant_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreatePlant.class );
                startActivity(intent);

            }
        });


        //if(plantList.size()==0){
        //    generatePlants();
        //}


        adapter = new MyAdapter(plantList, getAppContext());
        recyclerView.setAdapter(adapter);



        thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(5000);
                        adafruitIO = new AdafruitIO(plant_current_temp);
                        adafruitIO.execute(apiUrl);
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                plant_current_temp=adafruitIO.getTempValue();
                                plant_current_moist=adafruitIO.getMoistValue();
                                updateValues();
                                if(threadCounter==0){
                                    plant_current_moist_AUX=plant_current_moist;
                                    plant_current_temp_AUX=plant_current_temp;
                                }
                                threadCounter++;
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();
        //------------------------------------------------------------------------------

    }


    public static void generatePlants(){

            Plant p1 = new Plant("1111", "Arruda",
                    plant_current_temp,  plant_current_moist,
                    "26",  "4",
                    "1024",  "0",
                    "ON",  "MANUAL",
                    "15", new ArrayList<String>(), "green_card");

            plantList.add(p1);

            adapter = new MyAdapter(plantList, getAppContext());
            recyclerView.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    public static List<Plant> getPlantList(){
        return plantList;
    }

    public static void addPlantInList(Plant plant){
        plantList.add(plant);
        //adapter = new MyAdapter(plantList, getAppContext());
       // recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    public static void removePlantInList(int pos){
        plantList.remove(pos);
        adapter.notifyDataSetChanged();
    }


    public static Context getAppContext() {
        return MainActivity.context;
    }


    public static String getPlant_current_temp(){
        return plant_current_temp;
    }
    public static String getPlant_current_moist(){
        return plant_current_moist;
    }

    public void sendNotification(int pos, String current, String plantName, String valueType) {
            Notification noti = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.noti_icon)
                    .setContentTitle("Your " + plantName + " requires your attention!")
                    .setContentText("The plants " + valueType + " limits have been surpassed - " + current)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            notificationManager.notify(1, noti);

    }


    public void updateValues(){
        if(validateValues(plant_current_moist)){
            boolean  notisent=false;
            for(int i=0; i<plantList.size();i++){
                plantList.get(i).setPlant_current_moist(plant_current_moist);
                if((Double.parseDouble(getLastValueOfString(plantList.get(i).getPlant_moist_max())) < Double.parseDouble(plant_current_moist)
                    || Double.parseDouble(getLastValueOfString(plantList.get(i).getPlant_moist_min())) > Double.parseDouble(plant_current_moist))
                    && plantList.get(i).getPlant_notifications()=="ON"
                    && Double.parseDouble(plant_current_moist_AUX)!=Double.parseDouble(plant_current_moist)){
                        sendNotification(i,plant_current_moist, plantList.get(i).getPlant_name(), "moisture");
                        System.out.println( "sending noti");
                        plantList.get(i).setColor("lightred");
                        adapter.notifyDataSetChanged();
                        notisent=true;
                }
                else if((Double.parseDouble(getLastValueOfString(plantList.get(i).getPlant_moist_max())) < Double.parseDouble(plant_current_moist)
                        || Double.parseDouble(getLastValueOfString(plantList.get(i).getPlant_moist_min())) > Double.parseDouble(plant_current_moist))){
                        plantList.get(i).setColor("lightred");
                        adapter.notifyDataSetChanged();
                }
            }
            if(notisent==true){
                plant_current_moist_AUX=plant_current_moist;
            }
        }

        if(validateValues(plant_current_temp)){
            boolean  notisent=false;
            for(int i=0; i<plantList.size();i++){
                plantList.get(i).setPlant_current_temp(plant_current_temp);
                if((Double.parseDouble(getLastValueOfString(plantList.get(i).getPlant_temp_max())) < Double.parseDouble(plant_current_temp)
                    || Double.parseDouble(getLastValueOfString(plantList.get(i).getPlant_temp_min())) > Double.parseDouble(plant_current_temp))
                    && plantList.get(i).getPlant_notifications()=="ON"
                    && Double.parseDouble(plant_current_temp_AUX)!=Double.parseDouble(plant_current_temp)){
                        sendNotification(i,plant_current_temp, plantList.get(i).getPlant_name(), "temperature");
                         plantList.get(i).setColor("lightred");
                         adapter.notifyDataSetChanged();
                        notisent=true;
                }
                else if((Double.parseDouble(getLastValueOfString(plantList.get(i).getPlant_temp_max())) < Double.parseDouble(plant_current_temp)
                        || Double.parseDouble(getLastValueOfString(plantList.get(i).getPlant_temp_min())) > Double.parseDouble(plant_current_temp))){
                         plantList.get(i).setColor("lightred");
                         adapter.notifyDataSetChanged();
                }
                else{
                    plantList.get(i).setColor("green_card");
                    adapter.notifyDataSetChanged();
                }
            }
            if(notisent==true){
                plant_current_temp_AUX=plant_current_temp;
            }
        }
        adapter.notifyDataSetChanged();
    }

    public Boolean validateValues(String toValidate){
        boolean isDouble = false;
        try
        {
            if(toValidate!=null){
                Double.parseDouble(toValidate);
                isDouble=true;
            }

        }
        catch(NumberFormatException e)
        {
            isDouble = false;
        }
        return isDouble;

    }

    public String getLastValueOfString(String s){
        String[] bits = s.split(" ");
        String lastOne = bits[bits.length-1];
        return lastOne;
    }




}
