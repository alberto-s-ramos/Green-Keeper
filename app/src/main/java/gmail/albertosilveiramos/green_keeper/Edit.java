package gmail.albertosilveiramos.green_keeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class Edit extends AppCompatActivity {


    private Button set_plant_fields;
    private EditText plant_id_Edit;
    private EditText plant_name_Edit;
    private EditText plant_max_temp_Edit;
    private EditText plant_min_temp_Edit;
    private EditText plant_max_moist_Edit;
    private EditText plant_min_moist_Edit;
    private EditText plant_irrigation_duration_Edit;
    private Switch notiSwitch;
    private Switch autoSwitch;
    private Button edit_plant;
    private List<Plant> plantListCopy;

    private static int pos;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_plant);

        plant_id_Edit = findViewById(R.id.plant_id_Edit);
        plant_name_Edit = findViewById(R.id.plant_name_Edit);
        plant_max_temp_Edit = findViewById(R.id.plant_max_temp_Edit);
        plant_min_temp_Edit = findViewById(R.id.plant_min_temp_Edit);
        plant_max_moist_Edit = findViewById(R.id.plant_max_moist_Edit);
        plant_min_moist_Edit = findViewById(R.id.plant_min_moist_Edit);
        plant_irrigation_duration_Edit = findViewById(R.id.plant_irrigation_duration_Edit);
        notiSwitch = findViewById(R.id.plantEditNotiSwitch);
        autoSwitch = findViewById(R.id.plantEditAutoSwitch);
        edit_plant = findViewById(R.id.edit_plant);

        setDefaultValues();

        edit_plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePlantFields()==true){
                    updatePlantOnMain();
                    Configurations.setPos(pos);
                    Intent intent = new Intent(Edit.this, Configurations.class);
                    startActivity(intent);
                }

            }
        });




    }


    public static void setPos(int posNew){
        pos=posNew;
    }

    public void updatePlantOnMain(){
        MainActivity.getPlantList().get(pos).setPlant_id(plant_id_Edit.getText().toString());
        MainActivity.getPlantList().get(pos).setPlant_name(plant_name_Edit.getText().toString());
        MainActivity.getPlantList().get(pos).setPlant_temp_max(plant_max_temp_Edit.getText().toString());
        MainActivity.getPlantList().get(pos).setPlant_temp_min(plant_min_temp_Edit.getText().toString());
        MainActivity.getPlantList().get(pos).setPlant_moist_max(plant_max_moist_Edit.getText().toString());
        MainActivity.getPlantList().get(pos).setPlant_moist_min(plant_min_moist_Edit.getText().toString());
        MainActivity.getPlantList().get(pos).setPlant_irrigation_duration(plant_irrigation_duration_Edit.getText().toString());
        if(notiSwitch.isChecked()==true){
            MainActivity.getPlantList().get(pos).setPlant_notifications("ON");
        }
        else if(notiSwitch.isChecked()==false){
            MainActivity.getPlantList().get(pos).setPlant_notifications("OFF");
        }
        if(autoSwitch.isChecked()==true){
            MainActivity.getPlantList().get(pos).setPlant_irrigation_type("AUTO");
        }
        else if(autoSwitch.isChecked()==false){
            MainActivity.getPlantList().get(pos).setPlant_irrigation_type("MANUAL");
        }



    }


    public void setDefaultValues(){
        plant_id_Edit.setText(getLastValueOfString(MainActivity.getPlantList().get(pos).getPlant_id()));
        plant_name_Edit.setText(getLastValueOfString(MainActivity.getPlantList().get(pos).getPlant_name()));
        plant_max_temp_Edit.setText(getLastValueOfString(MainActivity.getPlantList().get(pos).getPlant_temp_max()));
        plant_min_temp_Edit.setText(getLastValueOfString(MainActivity.getPlantList().get(pos).getPlant_temp_min()));
        plant_max_moist_Edit.setText(getLastValueOfString(MainActivity.getPlantList().get(pos).getPlant_moist_max()));
        plant_min_moist_Edit.setText(getLastValueOfString(MainActivity.getPlantList().get(pos).getPlant_moist_min()));
        plant_irrigation_duration_Edit.setText(getLastValueOfString(MainActivity.getPlantList().get(pos).getPlant_irrigation_duration()));
        if((MainActivity.getPlantList().get(pos).getPlant_notifications().equals("ON"))){
            notiSwitch.setChecked(true);
        }
        else if((MainActivity.getPlantList().get(pos).getPlant_notifications().equals("OFF"))){
            notiSwitch.setChecked(false);
        }

        if((MainActivity.getPlantList().get(pos).getPlant_irrigation_type().equals("AUTO"))){
            autoSwitch.setChecked(true);
        }
        else if((MainActivity.getPlantList().get(pos).getPlant_irrigation_type().equals("MANUAL"))){
            autoSwitch.setChecked(false);
        }

    }

    public boolean validatePlantFields(){

        if(plant_id_Edit.getText().toString()=="" || plant_name_Edit.getText().toString()=="" ||
                plant_max_temp_Edit.getText().toString()=="" || plant_min_temp_Edit.getText().toString()=="" ||
                plant_max_moist_Edit.getText().toString()=="" || plant_min_moist_Edit.getText().toString()=="" ||
                plant_irrigation_duration_Edit.getText().toString()==""){
            Toast.makeText(this, "All fields must be filled.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!isNumericInt(plant_id_Edit.getText().toString()) ||
                !isNumericDouble(plant_max_temp_Edit.getText().toString()) || !isNumericDouble(plant_min_temp_Edit.getText().toString())||
                !isNumericDouble(plant_max_moist_Edit.getText().toString()) ||  !isNumericDouble(plant_min_moist_Edit.getText().toString()) ||
                !isNumericInt(plant_irrigation_duration_Edit.getText().toString())){
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

    public String getLastValueOfString(String s){
        String[] bits = s.split(" ");
        String lastOne = bits[bits.length-1];
        return lastOne;
    }


}
