package mpdcoursework.gcu.me.org.mobilepdcoursework;

/**
 * Created by camer on 18/03/2018.
 */
//S1628376 Cameron Crawford
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//custom ArrayAdapter
class displayArrayAdapter extends ArrayAdapter<DetailsClass> implements Serializable {

    private Context context;
    private ArrayList<DetailsClass> rentalProperties;

    public displayArrayAdapter(Context context, int resource, List<DetailsClass> objects) {
        super(context, resource, objects);

        this.context = context;
        this.rentalProperties = (ArrayList)objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //The property which is being displayed is placed inside the traffic instance
        DetailsClass traffic = rentalProperties.get(position);

        //The property layout is a template which is used to display the proview of each instance
        //of the reults inside of the list
        //This function inflates the property XML page for each of the results
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.property_layout, null);


        if (traffic.getDescription().contains("Start Date")) {
            String pattern = " EEEE, dd MMMM yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String[] parts = traffic.getDescription().split("<br />");
            String part1 = parts[0];
            String part2 = parts[1];

            part1 = part1.substring(part1.indexOf(':') + 1, part1.indexOf('-'));
            part2 = part2.substring(part2.indexOf(':') + 1, part2.indexOf('-'));

            try {
                Date date = simpleDateFormat.parse(part1);
                Date date1 = simpleDateFormat.parse(part2);
                long difference = Math.abs(date.getTime() - date1.getTime());
                long differenceInDays = difference / (24 * 60 * 60 * 1000);
                if (differenceInDays != 1) {
                    differenceInDays = differenceInDays + 1;
                }
                //Checks the difference between days and color coordinates the list view
                if (differenceInDays <= 1) {
                    view.setBackgroundColor(Color.parseColor("#A4C639"));
                } else if(differenceInDays >= 1 && differenceInDays <= 6) {
                    view.setBackgroundColor(Color.parseColor("#FDFD96"));
                }
                else
                {
                    view.setBackgroundColor(Color.parseColor("#FF6961"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView georss = (TextView) view.findViewById(R.id.location);
        title.setText(traffic.getTitle());
        description.setText(traffic.getDescription());
        georss.setText(traffic.getGeorss());
        //The following method takes the description and counts 100 characters, once it reaches the
        //100 characters, then it adds "...", this way the user has to click the item to view the
        //contents contained inside
        int descriptionLength = traffic.getDescription().length();
        if(descriptionLength >= 105){
            String descriptionTrim = traffic.getDescription().substring(0, 105) + "...";
            description.setText(descriptionTrim);
        }else{
            description.setText(traffic.getDescription());
        }
        //An attempt at splitting the description up using
        String descriptionCrop = traffic.getDescription().replace("<br />", " ");

        return view;
    }

    /*public String cropDesc(DetailsClass t){
        TextView description = (TextView) view.findViewById(R.id.description);
        int descriptionLength = t.getDescription().length();
        if(descriptionLength >= 100){
            String descriptionTrim = t.getDescription().add( + "...");
            description.setText(descriptionTrim);
        }else{
            description.setText(t.getDescription());
        }
        description1 = String.valueOf(description);
        return description1;
    }*/
}
