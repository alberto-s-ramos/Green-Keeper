package gmail.albertosilveiramos.green_keeper;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MotorAdafruitIO extends AsyncTask<String,Void,String> {

    private final static String motorBooleanApiUrl = "https://io.adafruit.com/api/v2/rodrigoagostinhotecnico/feeds/motor-boolean/data/?X-AIO-Key=fb9462765fdb4368b841393c3c9adf52";
    private int sleepTime=1;
    private Thread thread;


    @Override
    protected String doInBackground(String... strings) {
       executeMotor("ON");
       Log.d("Horita", "Irrigating.");
        thread = new Thread() {

            @Override
            public void run() {
                try {
                   sleep(sleepTime*1000);
                   executeMotor("OFF");
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();


       return "";
    }

    public void setSleepTime(int time){
        this.sleepTime=time;
    }

    public static void executeMotor(String booleanValue){
        try {
            URL url = new URL(motorBooleanApiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            StringBuilder tokenUri = new StringBuilder("value=");
            tokenUri.append(URLEncoder.encode(booleanValue,"UTF-8"));
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");

            conn.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
            outputStreamWriter.write(tokenUri.toString());
            outputStreamWriter.flush();

            BufferedReader inputStreamWriter = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = inputStreamWriter.readLine()) != null) {
                response.append(inputLine);
            }
            inputStreamWriter.close();

            System.out.println(response.toString());

            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
