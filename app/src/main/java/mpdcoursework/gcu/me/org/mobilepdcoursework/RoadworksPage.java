package mpdcoursework.gcu.me.org.mobilepdcoursework;

/**
 * Created by camer on 18/03/2018.
 */
//S1628376 Cameron Crawford
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.List;

public class RoadworksPage extends AppCompatActivity {

    private List<DetailsClass> roadworksList;
    private List<DetailsClass> itemsOfResults;

    private ArrayAdapter<DetailsClass> roadworksAdapter;
    private ArrayAdapter<DetailsClass> selectedAdapter;
    private Button getRoadworkBtn;
    private EditText enterNameTxt;
    private ListView roadworksListview;
    private ListView resultView;
    private TextView txtView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadworkspage_layout);
        //This function is used to provide an underline on the textview that displays the heading
        //txtView = (TextView) findViewById(R.id.textView);
        //txtView.setPaintFlags(txtView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        getSupportActionBar().setTitle("Planned Roadworks");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Assigning the buttons to the appopriate variables, this will be used later
        //to provide functionality to these buttons through
        getRoadworkBtn = (Button) findViewById(R.id.getRoadworkBtn);
        getRoadworkBtn.setInputType(0);
        enterNameTxt = (EditText) findViewById(R.id.enterNameTxt);

        roadworksListview = (ListView) findViewById(R.id.roadworksListView);
        resultView = (ListView) findViewById(R.id.selectedListView);

        //The result view is set as invisible to ensure that the roadworks list can be viewed
        //Later in the class the visibility is set when the user searches for a road
        resultView.setVisibility(View.INVISIBLE);

        roadworksList = (ArrayList<DetailsClass>)getIntent().getSerializableExtra("roadworksList");
        itemsOfResults = new ArrayList<DetailsClass>();

        roadworksAdapter = new displayArrayAdapter(RoadworksPage.this, 0, roadworksList);
        roadworksListview.setAdapter(roadworksAdapter);

        roadworksListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) {

                DetailsClass roadworksItem = roadworksList.get(position);

                Intent intent = new Intent(RoadworksPage.this, DetailsActivity.class);
                intent.putExtra("title", roadworksItem.getTitle());
                intent.putExtra("description", roadworksItem.getDescription());
                intent.putExtra("link", roadworksItem.getLink());
                intent.putExtra("georss", roadworksItem.getGeorss());
                intent.putExtra("author", roadworksItem.getAuthor());
                intent.putExtra("comments", roadworksItem.getComments());
                intent.putExtra("pubDate", roadworksItem.getPubDate());
                startActivity(intent);
                getSupportActionBar().setTitle("Planned Roadworks");
            }
        });
        //This is the item list of the results of what the user has searched
        resultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) {
                //Each value for each seperate item is loaded, this is way the results
                //are displayed, these results are clickable
                //Once Clicked, the detail for that selected item is loaded
                DetailsClass resultItem = itemsOfResults.get(position);
                //All roadworks are loaded through the Roadworks Activity
                //Then the details class is used to get the data that is to be
                //displayed when the user selects the item
                Intent intent = new Intent(RoadworksPage.this, DetailsActivity.class);
                intent.putExtra("title", resultItem.getTitle());
                intent.putExtra("description", resultItem.getDescription());
                intent.putExtra("link", resultItem.getLink());
                intent.putExtra("georss", resultItem.getGeorss());
                intent.putExtra("author", resultItem.getAuthor());
                intent.putExtra("comments", resultItem.getComments());
                intent.putExtra("pubDate", resultItem.getPubDate());
                startActivity(intent);
                getSupportActionBar().setTitle("Planned Roadworks");
            }
        });

        getRoadworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

                itemsOfResults = new ArrayList<DetailsClass>();
                String getTxtInput = enterNameTxt.getText().toString();
                getRoadName(getTxtInput);
                //The IF statement checks if there are results inside of the results variable
                if (itemsOfResults.size() > 0) {
                    selectedAdapter = new displayArrayAdapter(RoadworksPage.this, 0, itemsOfResults);
                    resultView.setAdapter(selectedAdapter);
                    //The results view is then displayed and the original list is then hidden
                    //Each result is taken and displayed in the result view
                    resultView.setVisibility(View.VISIBLE);
                    roadworksListview.setVisibility(View.INVISIBLE);
                    //The success message is then displayed
                    successMessage();
                }
                else
                {
                    //If there are no results for a search then the roadworks list is kept visible
                    //This will then initialise the method for the error toast
                    //This will tell the user that the road cannot be found
                    displayErrorMessage();
                    resultView.setVisibility(View.INVISIBLE);
                    roadworksListview.setVisibility(View.VISIBLE);
                }
            }
        });
        //Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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

    public void successMessage(){
        //When the user has searched for a road on the search bar then
        //a toast message is displayed to tell them that the results have been displayed
        Toast.makeText(RoadworksPage.this,
                "Here are your Results!",
                Toast.LENGTH_LONG).show();
    }

    public void displayErrorMessage() {
        //If there are no results then a toast is initialised
        //This will tell the user there are no entries for that road
        Toast.makeText(RoadworksPage.this,
                "No entries can be found!",
                Toast.LENGTH_LONG).show();
    }

    public void getRoadName(String searchTerm) {
        for (DetailsClass t : roadworksList) {
            //When a user searches for a road, the text is taken
            //and is converted into a lowercase string, this can then be used
            //to look through all of the roadnames in the titles and display the roads
            String search = t.getTitle();
            if (search.toLowerCase().indexOf(searchTerm.toLowerCase()) != -1) {
                itemsOfResults.add(t);
            }
        }
    }
}
