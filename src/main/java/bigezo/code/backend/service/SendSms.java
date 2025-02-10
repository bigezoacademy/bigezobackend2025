package bigezo.code.backend.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class SendSms {
    public static String sendingstatus="Sending...";
    String api_url ="https://www.egosms.co/api/v1/plain/?";
    public  String sendSms (String msg, String phone) {
        String api_url ="https://www.egosms.co/api/v1/plain/?";
        String username="alfredochola";
        String password="JesusisLORD";

        try {
            //Parameters to add to the URL
            LinkedHashMap<String,String> params = new LinkedHashMap<>();
            params.put("number", phone);
            params.put("message", msg);
            params.put("username", username);
            params.put("password", password);
            params.put("sender", username);
            StringJoiner sj = new StringJoiner("&");

            //Encoding the above parameters
            for(Map.Entry<String,String> entry : params.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
            String encoded_params = sj.toString();

            //URL with necessary parameters added
            String urlParams = api_url+encoded_params;

            //Sending Http request to above composed URL
            URL url = new URL(urlParams);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("GET");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");

            //Receiving response
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String line = null;
            while((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println(sendingstatus+" failed ..."+username+"...."+password+"...."+api_url+"...."+msg+"..."+phone+"...THE END");
        }
        return sendingstatus;
    }

}
