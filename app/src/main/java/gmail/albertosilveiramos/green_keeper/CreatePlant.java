package gmail.albertosilveiramos.green_keeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreatePlant extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private Button set_plant_fields;
    private EditText plant_id_ET;
    private EditText plant_name_ET;
    private EditText plant_max_temp_ET;
    private EditText plant_min_temp_ET;
    private EditText plant_max_moist_ET;
    private EditText plant_min_moist_ET;
    private EditText plant_irrigation_duration_ET;
    private Switch notiSwitch;
    private Switch autoSwitch;
    private Button plant_button_addToList;
    private List<Plant> plantListCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plant);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.plants, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        set_plant_fields = findViewById(R.id.set_plant_fields_button);
        set_plant_fields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyValuesOfSelected(spinner.getSelectedItem().toString());
            }
        });

        plant_id_ET = findViewById(R.id.plant_id_ET);
        plant_name_ET = findViewById(R.id.plant_name_ET);
        plant_max_temp_ET = findViewById(R.id.plant_max_temp_ET);
        plant_min_temp_ET = findViewById(R.id.plant_min_temp_ET);
        plant_max_moist_ET = findViewById(R.id.plant_max_moist_ET);
        plant_min_moist_ET = findViewById(R.id.plant_min_moist_ET);
        plant_irrigation_duration_ET = findViewById(R.id.plant_irrigation_duration_ET);
        plant_button_addToList = findViewById(R.id.add_plant_to_list);
        notiSwitch = findViewById(R.id.plantCreateNotiSwitch);
        autoSwitch = findViewById(R.id.plantCreateAutoSwitch);

        plant_button_addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePlantFields()==true){
                    Intent intent = new Intent(CreatePlant.this, MainActivity.class );
                    startActivity(intent);
                    Log.d("bertinho", "Going to main act and the list size is: "+MainActivity.getPlantList().size());
                    MainActivity.addPlantInList(getPlantValues());
                }
            }
        });

        plantListCopy = new ArrayList<>();
        generateDataBase();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public Plant getPlantValues(){

        Plant plant = new Plant(plant_id_ET.getText().toString(), plant_name_ET.getText().toString(),
                MainActivity.getPlant_current_temp(), MainActivity.getPlant_current_moist(),
                plant_max_temp_ET.getText().toString(), plant_min_temp_ET.getText().toString(),
                plant_max_moist_ET.getText().toString(), plant_min_moist_ET.getText().toString(),
                checkNotiSwitch(), checkAutoSwitch(),
                plant_irrigation_duration_ET.getText().toString(), new ArrayList<String>(), "green_card");
        return plant;
    }

    public boolean validatePlantFields(){

        if(plant_id_ET.getText().toString()=="" || plant_name_ET.getText().toString()=="" ||
                plant_max_temp_ET.getText().toString()=="" || plant_min_temp_ET.getText().toString()=="" ||
                plant_max_moist_ET.getText().toString()=="" || plant_min_moist_ET.getText().toString()=="" ||
                plant_irrigation_duration_ET.getText().toString()==""){
            Toast.makeText(this, "All fields must be filled.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!isNumericInt(plant_id_ET.getText().toString()) ||
                !isNumericDouble(plant_max_temp_ET.getText().toString()) || !isNumericDouble(plant_min_temp_ET.getText().toString())||
                !isNumericDouble(plant_max_moist_ET.getText().toString()) ||  !isNumericDouble(plant_min_moist_ET.getText().toString()) ||
                !isNumericInt(plant_irrigation_duration_ET.getText().toString())){
            Toast.makeText(this, "Type Integer / Double missing in some fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }




    public static boolean isNumericInt(String strNum) {
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isNumericDouble(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }


    public void applyValuesOfSelected(String selected_plantName){
        for(int i=0; i<plantListCopy.size();i++){
            if(plantListCopy.get(i).getPlant_name().equals(selected_plantName)){
                plant_name_ET.setText(plantListCopy.get(i).getPlant_name());
                plant_max_temp_ET.setText(getLastValueOfString(plantListCopy.get(i).getPlant_temp_max()));
                plant_min_temp_ET.setText(getLastValueOfString(plantListCopy.get(i).getPlant_temp_min()));
                plant_max_moist_ET.setText(getLastValueOfString(plantListCopy.get(i).getPlant_moist_max()));
                plant_min_moist_ET.setText(getLastValueOfString(plantListCopy.get(i).getPlant_moist_min()));
                plant_irrigation_duration_ET.setText(getLastValueOfString(plantListCopy.get(i).getPlant_irrigation_duration()));
            }
        }
    }


    public String getLastValueOfString(String s){
        String[] bits = s.split(" ");
        String lastOne = bits[bits.length-1];
        return lastOne;
    }

    public String checkNotiSwitch(){
        String noti="";
        if(notiSwitch.isChecked()==true){
            noti= "ON";
        }
        else if(notiSwitch.isChecked()==false){
            noti= "FALSE";
        }
        return noti;
    }

    public String checkAutoSwitch(){
        String noti="";
        if(autoSwitch.isChecked()==true){
            noti= "AUTO";
        }
        else if(autoSwitch.isChecked()==false){
            noti= "MANUAL";
        }
        return noti;
    }


    public  void generateDataBase() {

        Plant p1 = new Plant("1111", "Arruda",
                "0", "1",
                "26", "4",
                "1024", "0",
                "ON", "MANUAL",
                "15", new ArrayList<String>(), "green_card");

        Plant p2 = new Plant("1122", "Hortelã",
                "0", "1",
                "22", "1",
                "1024", "0",
                "OFF", "MANUAL",
                "15", new ArrayList<String>(), "green_card");
        Plant p3 = new Plant("1133", "Manjericão",
                "0", "0",
                "34", "5",
                "1024", "0",
                "OFF", "MANUAL",
                "15", new ArrayList<String>(), "green_card");
        Plant p4 = new Plant("1144", "Orégãos",
                "0", "1",
                "29", "1",
                "1024", "0",
                "OFF", "MANUAL",
                "9", new ArrayList<String>(), "green_card");
        Plant p5 = new Plant("1155", "Salsa",
                "0", "1",
                "31", "-1",
                "1024", "0",
                "OFF", "MANUAL",
                "7", new ArrayList<String>(), "green_card");


        plantListCopy.add(p1);
        plantListCopy.add(p2);
        plantListCopy.add(p3);
        plantListCopy.add(p4);
        plantListCopy.add(p5);

        }
    }
