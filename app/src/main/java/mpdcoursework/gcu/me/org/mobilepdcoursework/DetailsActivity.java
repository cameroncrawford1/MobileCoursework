package mpdcoursework.gcu.me.org.mobilepdcoursework;

/**
 * Created by camer on 18/03/2018.
 */
//S1628376 Cameron Crawford
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        //Each value of the details page is loaded into their own text view
        TextView titleView = (TextView) findViewById(R.id.title);
        TextView TSDescriptionLabel = (TextView) findViewById(R.id.description);
        TextView TSLinkLabel = (TextView) findViewById(R.id.link);
        TextView georssLabel = (TextView) findViewById(R.id.georss);

        //Provides a back button for navigation when on pages
        //This is displayed on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Creates an intent, this will allow data to be displayed on the
        //application layouts
        Intent intent = getIntent();

        //Variables are initialised for each data value which is fetched inside of the feed
        //This means the values are stored by the system
        String TStitle = intent.getStringExtra("title");
        String TSdescription = intent.getStringExtra("description");
        String TSlink = intent.getStringExtra("link");
        String georss = intent.getStringExtra("georss");

        //The variables are then set through the use of the setText
        titleView.setText(TStitle);
        TSDescriptionLabel.setText(TSdescription);
        georssLabel.setText("Location: " + georss);
        TSLinkLabel.setText("Travel Scotland Link: " + TSlink);
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



}
