package mpdcoursework.gcu.me.org.mobilepdcoursework;
//S1628376 Cameron Crawford
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.net.ConnectivityManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";



    private ArrayList<DetailsClass> incidentResultsList;
    private ArrayList<DetailsClass> roadworksResultsList;
    //The RSS feeds of each of the options are displayed below
    private String roadworksBtnURL ="http://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String incidentsBtnURL ="http://trafficscotland.org/rss/feeds/currentincidents.aspx";

    private ArrayAdapter<DetailsClass> incidentAdapter;
    private ArrayAdapter<DetailsClass> roadworksAdapter;
    //The buttons are initialised
    private Button btnIncidents;
    private Button btnRoadworks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnIncidents = (Button) findViewById(R.id.btnIncidents);
        btnRoadworks = (Button) findViewById(R.id.btnRoadworks);

        roadworksResultsList = new ArrayList<DetailsClass>();
        incidentResultsList = new ArrayList<DetailsClass>();

        new FetchFeedTask().execute((Void) null);
        //This is the function for the Incidents button
        btnIncidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //An intent is created
                //It links to the incidents class
                //All of the incidents are loaded once the button is pressed
                Intent myIntent = new Intent(MainActivity.this, CurrentIncidents.class);
                myIntent.putExtra("incidentsList", incidentResultsList);
                MainActivity.this.startActivity(myIntent);

                if(!checkIfConnected()){
                    Toast.makeText(getApplicationContext(),
                            "No Internet connection found",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

            }});
        //This is the button which displays the roadworks for the app
        btnRoadworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //An intent is created again, just like the incidents button
                //The results are then loaded and the app is redirected to the
                //roadworks layout page, which displays the results
                Intent myIntent = new Intent(MainActivity.this, RoadworksPage.class);
                myIntent.putExtra("roadworksList", roadworksResultsList);
                MainActivity.this.startActivity(myIntent);

                if(!checkIfConnected()){
                    Toast.makeText(getApplicationContext(),
                            "No Internet connection found",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }});
    }

    public final class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String urlLink = incidentsBtnURL;
            if (TextUtils.isEmpty(urlLink))
                return false;
            try {
                //This statement checks the url of the button that is selected
                //It then checks the url to see if it starts with anything other than http
                //If they are different then the url is changes to include before searching
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(incidentsBtnURL);
                InputStream inputStream = url.openConnection().getInputStream();
                incidentResultsList = MainActivity.pullParse(inputStream);

                URL url2 = new URL(roadworksBtnURL);
                InputStream inputStream1 = url2.openConnection().getInputStream();
                roadworksResultsList = MainActivity.pullParse(inputStream1);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                //A recycler view is used to fill when one selection has been made
                incidentAdapter = new displayArrayAdapter(MainActivity.this, 0, incidentResultsList);
                roadworksAdapter = new displayArrayAdapter(MainActivity.this, 0, roadworksResultsList);
            } else {

            }
        }
    }
    //This method is where the data from the rss feeds are parsed to the application and then
    //the data is stored, the data stored can then be manipulated to be displayed on the screen
    //This will then create a structure in which the application can display the information
    public static ArrayList<DetailsClass> pullParse(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        String georss = null;
        String author = null;
        String comments = null;
        String pubDate = null;
        boolean isItem = false;
        //An array is made to store each value inside of the feed
        ArrayList<DetailsClass> items = new ArrayList<>();

        try {
            //The pull parser is initiated
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);
            //Goes through the xml tags, retreiving the data
            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int getTypeforResults = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;
                //When the "item" end tag comes up then the instance is stopped
                //This then goes back to the start to read another tag
                if (getTypeforResults == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }
                //If the XML tag starts with "item", as in the rss feed
                //then a new instance of an incident or roadwork is created
                if (getTypeforResults == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }
                //Log.d("MainActivity", "Parsing... " + name);
                String textResultFromParser = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    //Gets the text from each tag
                    textResultFromParser = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }
                //This IF statement goes through all of the XML tags that are on the
                //RSS feed. If the tag matches the condition then the value is loaded into
                //the resultFromParser variable. This allows the array to be made of multiple
                //values from all of the tags in the feed
                if (name.equalsIgnoreCase("title")) {
                    title = textResultFromParser;
                } else if (name.equalsIgnoreCase("description")) {
                    description = textResultFromParser;
                } else if (name.equalsIgnoreCase("link")) {
                    link = textResultFromParser;
                } else if (name.equalsIgnoreCase("georss:point")) {
                    georss = textResultFromParser;
                } else if (name.equalsIgnoreCase("author")) {
                    author = textResultFromParser;
                    if (author == "") {
                        author = "N/A";
                    }
                } else if (name.equalsIgnoreCase("comments")) {
                    comments = textResultFromParser;
                    if (comments == "") {
                        comments = "N/A";
                    }
                } else if (name.equalsIgnoreCase("pubDate")) {
                    pubDate = textResultFromParser;
                }
                //String editedDescription = null;
                if (description != null && description.contains("<br />")) {
                    description.replace("<br />", " \n");
                    //editedDescription = description.replace("<br />", " \n");
                }
                //description = description.replace("<br />", " ");
                //This IF statement checks if all of the data taken from the tags can be stored
                //It checks if all of the variables have values, once it is determined that they all contain
                //a value then a new traffic instance is created
                if (title != null && link != null && description != null && georss != null) {
                    if (isItem) {
                        //String description1 = description.replace("<br />", " ");
                        DetailsClass item = new DetailsClass(title, description, link, georss, author, comments, pubDate);
                        items.add(item);
                    } else {
                    }
                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }
            return items;
        } finally {
            inputStream.close();
        }
    }
    public boolean checkIfConnected(){
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        //When the attempt fails, no internet connection was found
        catch(Exception ex){
            return false;
        }
    }
}
