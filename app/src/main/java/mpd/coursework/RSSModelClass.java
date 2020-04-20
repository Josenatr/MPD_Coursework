package mpd.coursework;
/*
 Name                 Adam Hosie
 Student ID           S1624519
*/
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RSSModelClass implements Parcelable {
    private String title = "";
    private String description = "";
    private String link = "";
    private String geoPoint = "";
    private String author = "";
    private String comments = "";
    private String pubDate = "";
    private String startDate = "";
    private String endDate = "";
    private long duration;

    public DateClass dateClass;

    public RSSModelClass() {

        this.title = "UNDEFINED";
        dateClass = new DateClass();
    }

    public RSSModelClass(String title, String description, String link, String geoPoint, String pubDate){
        super();
        this.title = title;
        this.description = description;
        this.link = link;
        this.geoPoint = geoPoint;
        this.pubDate = pubDate;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String titleExpanded = title.replace("NB", "Northbound")
                .replace("N/B", "Northbound")
                .replace("SB", "Southbound")
                .replace("S/B", "Southbound")
                .replace("EB", "Eastbound")
                .replace("E/B", "Eastbound")
                .replace("WB", "Westbound")
                .replace("W/B", "Westbound")
                .replace("TTL", "Temp. Traffic Lights")
                .replace("MLC", "Mobile Lane Closure");
        this.title = titleExpanded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = description;
        this.parseStartAndEndDatesFromDescription(description);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setGeoPoint(String geoPoint) {
        this.geoPoint = geoPoint;
    }
    public String getGeoPoint() { return geoPoint; }

    public Double[] getLatLng() {
        String[] latlngsplit = this.geoPoint.split(" ", -1);
        Double[] coordinates = new Double[2];
        coordinates[0] = Double.parseDouble(latlngsplit[0]);
        coordinates[1] = Double.parseDouble(latlngsplit[1]);
        return coordinates;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStartDate() {
        //return "Start Date: " + dateClass.convertDate(startDate);
        return startDate;
    }

    public String getEndDate() {

        //return "End Date: " + dateClass.convertDate(endDate);
        return endDate;

    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getDuration() { return duration; }

    public void setDuration(long dur){ this.duration = dur;}

    public void setPubDate(String nextText) {
        System.out.println("Parsing date");
        this.pubDate = pubDate;

    }

    public String getPubDate() {
        return pubDate;
    }

    public void parseStartAndEndDatesFromDescription(String description)
    {

        if (description.contains("<br /"))
        {
            if(description.contains("Start Date:")) {
                DateFormat format = new SimpleDateFormat("EE, dd MMMMM y hh:mm:ss z", Locale.ENGLISH);
                String[] stringParts = description.split("<br />");
                Date dates[] = getDates(stringParts);
                long duration = getDuration(dates);
                Date startDate = dates[0];
                Date endDate = dates[1];
                if (stringParts.length > 2) {
                    String descriptionWithoutDates = stringParts[2];
                    System.out.println(descriptionWithoutDates);
                    this.description = descriptionWithoutDates;
                }
                this.duration = duration;
                this.startDate = "Start Date: " + dateClass.convertDate(stringParts[0]);
                this.endDate = "End Date: " + dateClass.convertDate(stringParts[1]);
            }
            else
                {
                    String descriptionWithoutDates = "";
                    System.out.println(descriptionWithoutDates);
                    this.description = descriptionWithoutDates;
                }


        }
    }

    @NonNull
    @Override
    public String toString() {
        return "TrafficFeedModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", geoPoint='" + geoPoint + '\'' +
                ", author='" + author + '\'' +
                ", comments='" + comments + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }


    private Date[] getDates(String[] parts){
        Date newStartDate = null;
        Date newEndDate = null;

        try{
            //string dates
            String sDate = parts[0].substring(12);
            String eDate = parts[1].substring(10);

            //first format
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMMMM yyyy - HH:mm", Locale.ENGLISH);
            Date sd = sdf.parse(sDate);
            Date ed = sdf.parse(eDate);

            //reformat
            sdf.applyPattern("dd/MM/yy HH:mm");
            String startDate = sdf.format(sd);
            String endDate = sdf.format(ed);
            newStartDate = sdf.parse(startDate);
            newEndDate = sdf.parse(endDate);

        } catch (ParseException e){
            e.printStackTrace();
        }

        return new Date[]{newStartDate, newEndDate};
    };

    private long getDuration (Date[] dates){
        Date startDate = dates[0];
        Date endDate = dates[1];
        long diffInMil = Math.abs(endDate.getTime() - startDate.getTime());
        long diff = TimeUnit.HOURS.convert(diffInMil, TimeUnit.MILLISECONDS);

        return diff;
    }

    public String calcColour(Long d){
        String colour;
        int duration = d.intValue();
        if (duration >= 336){
            colour = "#000000";
        } else if (duration >= 168) {
            colour = "#FF3F14";
        } else if (duration >= 72) {
            colour = "#FF8114";
        } else if (duration >= 48) {
            colour = "#FFBA14";
        } else if (duration >= 24) {
            colour = "#F1FF14";
        } else {
            colour = "#00FF06";
        }
        return colour;
    }

    protected RSSModelClass(Parcel in) {
        title = in.readString();
        description = in.readString();
        link = in.readString();
        geoPoint = in.readString();
        pubDate = in.readString();
        startDate = in.readString();
        endDate = in.readString();
    }

    public static final Creator<RSSModelClass> CREATOR = new Creator<RSSModelClass>() {
        @Override
        public RSSModelClass createFromParcel(Parcel in) {
            return new RSSModelClass(in);
        }

        @Override
        public RSSModelClass[] newArray(int size) {
            return new RSSModelClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.link);
        dest.writeString(this.geoPoint);
        dest.writeString(this.pubDate);

    }
}