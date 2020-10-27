package gmail.albertosilveiramos.green_keeper;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {

    private List<Plant> plant_list;
    private List<Plant> plant_list_COPY;
    private Context context;
    private final static String motorBooleanApiUrl = "https://io.adafruit.com/api/v2/rodrigoagostinhotecnico/feeds/motor-boolean/data/?X-AIO-Key=fb9462765fdb4368b841393c3c9adf52";
    private ViewHolder holderGeneral;


    public MyAdapter(List<Plant> plant_list, Context context) {
        this.plant_list = plant_list;
        this.context = context;
        plant_list_COPY = new ArrayList<>(plant_list);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.plant_card, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        Plant plant = plant_list.get(i);

        this.holderGeneral=holder;

        if(plant_list.get(i).getColor().equals("green_card")){
            holder.background_cardView.setBackgroundTintList( ColorStateList.valueOf(context.getResources().getColor(R.color.green_card)));
            holder.plant_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else if(plant_list.get(i).getColor().equals("lightred")){
            holder.background_cardView.setBackgroundTintList( ColorStateList.valueOf(context.getResources().getColor(R.color.lightred)));
            holder.plant_name.setTextColor(context.getResources().getColor(R.color.red));

        }
        holder.plant_name.setText(plant.getPlant_name());

        holder.plant_current_temp.setText(plant.getPlant_current_temp());
        holder.plant_current_moist.setText(plant.getPlant_current_moist());

        holder.plant_max_temp.setText(plant.getPlant_temp_max());
        holder.plant_min_temp.setText(plant.getPlant_temp_min());

        holder.plant_max_moist.setText(plant.getPlant_moist_max());
        holder.plant_min_moist.setText(plant.getPlant_moist_min());

        holder.plant_irrigation_duration.setText(plant.getPlant_irrigation_duration());

        holder.plant_config_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Configurations.class);
                Configurations.setPos(i);
                Configurations.setIrrigationHistoryList(MainActivity.getPlantList().get(i).getIrrigationHistory());
                context.startActivity(intent);
            }
        });

        holder.plant_irrigate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.water_sound);
                mediaPlayer.start();
                Date now = new Date();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d 'at' h:m:s a z");
                MainActivity.getPlantList().get(i).getIrrigationHistory().add(dateFormatter.format(now)+"");
                MotorAdafruitIO motor = new MotorAdafruitIO();
                motor.setSleepTime(Integer.parseInt(getLastValueOfString(MainActivity.getPlantList().get(i).getPlant_irrigation_duration())));
                motor.execute(motorBooleanApiUrl);
            }
        });

    }


    @Override
    public int getItemCount() {
        return plant_list.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Plant> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length()==0){
                filteredList.addAll(plant_list_COPY);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Plant plant : plant_list_COPY){
                    if(plant.getPlant_name().toLowerCase().contains(filterPattern)){
                        filteredList.add(plant);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            plant_list.clear();
            plant_list.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };



    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView background_cardView;

        public TextView plant_name;

        public TextView plant_current_temp;
        public TextView plant_current_moist;

        public TextView plant_max_temp;
        public TextView plant_min_temp;

        public TextView plant_max_moist;
        public TextView plant_min_moist;

        public Button plant_irrigate_button;
        public TextView plant_irrigation_duration;

        public Button plant_config_button;

        public ViewHolder(View itemView) {
            super(itemView);

            background_cardView = itemView.findViewById(R.id.background_cardView);

            plant_name = itemView.findViewById(R.id.plant_name);

            plant_current_temp = itemView.findViewById(R.id.plant_current_temp);
            plant_current_moist = itemView.findViewById(R.id.plant_current_moist);

            plant_max_temp = itemView.findViewById(R.id.plant_max_temp);
            plant_min_temp = itemView.findViewById(R.id.plant_min_temp);

            plant_max_moist = itemView.findViewById(R.id.plant_max_moist);
            plant_min_moist = itemView.findViewById(R.id.plant_min_moist);

            plant_irrigate_button = itemView.findViewById(R.id.plant_irrigate_button);
            plant_irrigation_duration = itemView.findViewById(R.id.plant_irrigation_duration);

            plant_config_button = itemView.findViewById(R.id.plant_config_button);

        }
    }

    public String getLastValueOfString(String s){
        String[] bits = s.split(" ");
        String lastOne = bits[bits.length-1];
        return lastOne;
    }


    public void updateBGColor(int pos){
        holderGeneral.background_cardView.setBackgroundTintList( ColorStateList.valueOf(context.getResources().getColor(R.color.lightred)));
    }


}
