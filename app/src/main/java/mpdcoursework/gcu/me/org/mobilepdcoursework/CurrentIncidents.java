package mpdcoursework.gcu.me.org.mobilepdcoursework;

/**
 * Created by camer on 18/03/2018.
 */
//S1628376 Cameron Crawford
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class CurrentIncidents extends AppCompatActivity {
    //Creating the list as an instance of the details
    private List<DetailsClass> incidentList;
    //The adapter is used to get bridge the gap between the parser and the UI
    private ArrayAdapter<DetailsClass> incidentAdapter;

    private ListView incidentsListView;
    private TextView txtView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currentincidents_layout);
        //This function provides an underline on the title of the page
        txtView = (TextView) findViewById(R.id.textView);
        txtView.setPaintFlags(txtView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        incidentsListView = (ListView) findViewById(R.id.incidentsListView);
        //The data is parsed from the main activity is taken through the incidents page
        incidentList = (ArrayList<DetailsClass>)getIntent().getSerializableExtra("incidentsList");
        //All incients are then stored in an array, which allows them to be displayed
        incidentAdapter = new displayArrayAdapter(CurrentIncidents.this, 0, incidentList);

        incidentsListView.setAdapter(incidentAdapter);
        incidentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) {

                DetailsClass traffic = incidentList.get(position);
                //Intents are used to put each tag onto the user interface of the incidents
                //page. This takes the details and creates each instance, these instances
                //are made clickable. Which is why in the initialisation of the intent, there
                //is a link to the details activity, this will then lead to loading the
                //detail of each incident
                Intent intent = new Intent(CurrentIncidents.this, DetailsActivity.class);
                intent.putExtra("title", traffic.getTitle());
                //This was an attempt to take the <br /> symbols out of the description
                //intent.putExtra("description", traffic.getDescription().replace("<br />", " "));
                intent.putExtra("description", traffic.getDescription());
                //Put Extras are used to data into a bundle
                intent.putExtra("link", traffic.getLink());
                intent.putExtra("georss", traffic.getGeorss());
                intent.putExtra("author", traffic.getAuthor());//Isnt used
                intent.putExtra("comments", traffic.getComments());//Isnt used
                intent.putExtra("pubDate", traffic.getPubDate());//Isnt used
                startActivity(intent);
            }
        });
        //This is the code which allows a back button to be placed on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //This method takes the item that the user selects and displays the result
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}