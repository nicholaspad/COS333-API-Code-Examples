import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


/*
You must first download the Simple JSON parser.
Go to http://www.java2s.com/Code/Jar/j/Downloadjsonsimple11jar.htm

and download the following zip file:

json-simple/json-simple-1.1.jar.zip(14 k)

Then, be sure to add this resulting jar file into your
list of referenced libraries. So, in VSCode for example,
go to the right window pane, go to Java Dependencies,
go down to Referenced Libraries, and click the plus sign.
Then, select the jar file that you just downloaded. Then,
you should be set.

Run the following command to add this jar file into your
classpath.

javac 

*/

public class Configs {

    public String consumer_key;
    public String consumer_secret;
    public String access_token;
    public String base_url;
    public String course_courses;
    public String course_terms;
    public String dining_locations;
    public String dining_events;
    public String dining_menu;
    public String places_open;
    public String events_events;
    public String refresh_token_url;

    private String extractAccessToken(String response) {
        String ACCESS_TOKEN = "access_token\"";
        int start_index = response.indexOf('"', response.indexOf(ACCESS_TOKEN) + ACCESS_TOKEN.length()) + 1;
        int last_index = response.indexOf('"', start_index);

        return response.substring(start_index, last_index);
    }

    public void updateAccessToken() throws Exception {
        URL url = new URL(refresh_token_url);
        HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();

        byte[] data = "grant_type=client_credentials".getBytes(StandardCharsets.UTF_8);
        byte[] secret_key = (consumer_key + ":" + consumer_secret).getBytes(StandardCharsets.UTF_8);
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(secret_key);
        httpsCon.setRequestProperty("Authorization", basicAuth);

        httpsCon.setRequestMethod("POST");
        httpsCon.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(httpsCon.getOutputStream());
        wr.write(data);

        int responseCode = httpsCon.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String access_token = extractAccessToken(response.toString());

            this.access_token = access_token;
        } else {
            throw new RuntimeException("Something went terribly wrong in the request");
        }
    }

    public Configs() {
        this.consumer_key = "M8QxiCMNux9eIoINvQ96la7kbb0a";
        this.consumer_secret = "bG0jFnzEhdM3sfUlhBwRrZBpW6Ua";
        this.base_url = "https://api.princeton.edu:443/mobile-app/1.0.0";
        this.course_courses = "/courses/courses";
        this.course_terms = "/courses/terms";
        this.dining_locations = "/dining/locations";
        this.dining_events = "/dining/events";
        this.dining_menu = "/dining/menu";
        this.places_open = "/places/open";
        this.events_events = "/events/events";
        this.refresh_token_url = "https://api.princeton.edu:443/token";

        // Get the access token
        try {
            updateAccessToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Configs configs = new Configs();
        System.out.println(configs.access_token);
    }
}
