package gmail.albertosilveiramos.green_keeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AutomaticMotor extends BroadcastReceiver {

    private final static String motorBooleanApiUrl = "https://io.adafruit.com/api/v2/rodrigoagostinhotecnico/feeds/motor-boolean/data/?X-AIO-Key=fb9462765fdb4368b841393c3c9adf52";
    private static int pos;

    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.water_sound);
        mediaPlayer.start();
        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d 'at' h:m:s a z");
        MainActivity.getPlantList().get(pos).getIrrigationHistory().add(dateFormatter.format(now)+" - AUTO");
        MotorAdafruitIO motor = new MotorAdafruitIO();
        motor.setSleepTime(5);
        motor.execute(motorBooleanApiUrl);
    }

    public static void setPos(int newpos){
        pos=newpos;
    }
}
