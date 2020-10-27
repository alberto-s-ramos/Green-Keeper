package gmail.albertosilveiramos.green_keeper;

import java.util.ArrayList;
import java.util.Calendar;

public class Plant {

    private String plant_id;
    private String plant_name;

    private String plant_current_temp;
    private String plant_current_moist;

    private String plant_temp_max;
    private String plant_temp_min;

    private String plant_moist_max;
    private String plant_moist_min;

    private String plant_notifications;
    private String plant_irrigation_type; //AUTO or MANUAL

    private String plant_irrigation_duration;
    private ArrayList<String> irrigationHistory;

    private String color;


    public Plant(String plant_id, String plant_name,
                 String plant_current_temp, String plant_current_moist,
                 String plant_temp_max, String plant_temp_min,
                 String plant_moist_max, String plant_moist_min,
                 String plant_notifications, String plant_irrigation_type,
                 String plant_irrigation_duration, ArrayList<String> irrigationHistory, String color){

        this.plant_id=plant_id;
        this.plant_name=plant_name;
        this.plant_current_temp="T: "+plant_current_temp ;
        this.plant_current_moist="M: "+plant_current_moist;
        this.plant_temp_max="Max Temp: "+plant_temp_max;
        this.plant_temp_min="Min Temp: "+plant_temp_min;
        this.plant_moist_max="Max Moist: "+plant_moist_max;
        this.plant_moist_min="Min Moist: "+plant_moist_min;
        this.plant_notifications=plant_notifications;
        this.plant_irrigation_type=plant_irrigation_type;
        this.plant_irrigation_duration="Irrigation Duration (s): "+plant_irrigation_duration;
        this.irrigationHistory=irrigationHistory;
        this.color=color;

    }



    public String getPlant_id(){
        return plant_id;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public String getPlant_current_temp() {
        return plant_current_temp;
    }

    public String getPlant_current_moist() {
        return plant_current_moist;
    }

    public String getPlant_temp_max() {
        return plant_temp_max;
    }

    public String getPlant_temp_min() {
        return plant_temp_min;
    }

    public String getPlant_moist_max() {
        return plant_moist_max;
    }

    public String getPlant_moist_min() {
        return plant_moist_min;
    }

    public String getPlant_notifications() {
        return plant_notifications;
    }

    public String getPlant_irrigation_type() {
        return plant_irrigation_type;
    }

    public String getPlant_irrigation_duration() {
        return plant_irrigation_duration;
    }

    public ArrayList<String> getIrrigationHistory() {
        return irrigationHistory;
    }

    public String getColor() {
        return color;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public void setPlant_current_temp(String plant_current_temp) {
        this.plant_current_temp = "T: "+plant_current_temp;
    }

    public void setPlant_current_moist(String plant_current_moist) {
        this.plant_current_moist = "M: "+ plant_current_moist;
    }

    public void setPlant_notifications(String plant_notifications) {
        this.plant_notifications = plant_notifications;
    }

    public void setPlant_irrigation_type(String plant_irrigation_type) {
        this.plant_irrigation_type = plant_irrigation_type;
    }

    public void setPlant_irrigation_duration(String plant_irrigation_duration) {
        this.plant_irrigation_duration ="Irrigation Duration (s): "+ plant_irrigation_duration;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPlant_id(String id){
       this.plant_id=id;
    }

    public void setPlant_temp_max(String plant_temp_max) {
        this.plant_temp_max = "Max Temp: "+ plant_temp_max;
    }

    public void setPlant_temp_min(String plant_temp_min) {
        this.plant_temp_min ="Min Temp: "+ plant_temp_min;
    }

    public void setPlant_moist_max(String plant_moist_max) {
        this.plant_moist_max = "Max Moist: "+plant_moist_max;
    }

    public void setPlant_moist_min(String plant_moist_min) {
        this.plant_moist_min = "Min Moist: "+plant_moist_min;
    }

    public void setIrrigationHistory(ArrayList<String> irrigationHistory) {
        this.irrigationHistory = irrigationHistory;
    }
}
