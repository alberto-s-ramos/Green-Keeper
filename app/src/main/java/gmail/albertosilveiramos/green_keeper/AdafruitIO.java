package gmail.albertosilveiramos.green_keeper;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AdafruitIO extends AsyncTask<String,Void,String>{

    private static final String apiUrl = "https://io.adafruit.com/api/v2/rodrigoagostinhotecnico/feeds/temperature-sensor/?X-AIO-Key=fb9462765fdb4368b841393c3c9adf52";
    private final static String moistureSensorApiUrl = "https://io.adafruit.com/api/v2/rodrigoagostinhotecnico/feeds/moisture-sensor/?X-AIO-Key=fb9462765fdb4368b841393c3c9adf52";


    private String tempValue;
    private String moistValue;
    private Thread thread;
    private String jsonTextTemp;
    private String jsonTextMoist;


    public AdafruitIO(String currentTemp){
        this.tempValue = currentTemp;
    }

    @Override
    protected String doInBackground(String... strings) {

        //--------------------------TEMPERATURE--------------------------------
        String output = null;
        jsonTextTemp = null;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            jsonTextTemp = readAll(br);
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            setTempValue(getLastValue(jsonTextTemp));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //--------------------------MOISTURE--------------------------------

        try {
            URL url = new URL(moistureSensorApiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            jsonTextMoist = readAll(br);
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            setMoistValue(getLastValue(jsonTextMoist));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String getTempValue(){
        return tempValue;
    }
    public String getMoistValue(){
        return moistValue;
    }

    public void setTempValue(String tempNew){
        this.tempValue=tempNew;
    }
    public void setMoistValue(String moistNew){
        this.moistValue=moistNew;
    }


    public String getLastValue(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        String lastValue = jsonObject.getString("last_value");
        int nCharacters = 5;
        String lastValueUpToNCharacters = lastValue.substring(0, Math.min(lastValue.length(), nCharacters));
        System.out.println(lastValueUpToNCharacters);
        return lastValueUpToNCharacters;
    }




}