package mpd.coursework;
/*
 Name                 Adam Hosie
 Student ID           S1624519
*/
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RSSParserClass {



    public List<RSSModelClass> parseXML(InputStream inputStream) throws IOException {
        List<RSSModelClass> rssModelClassList = null;
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            rssModelClassList = parse(parser);

        } catch (XmlPullParserException e) {
            Log.i("Parser Exception", Objects.requireNonNull(e.getMessage()));
        }

        return rssModelClassList;
    }


    private List<RSSModelClass> parse(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<RSSModelClass> incidents = new ArrayList<>();
        RSSModelClass rssModelClass = null;
        boolean done = false;


        int eventType = parser.getEventType();
        RSSModelClass item = null;
        while (eventType != XmlPullParser.END_DOCUMENT && !done) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item")) {
                        Log.i("new item", "Create new item");
                        item = new RSSModelClass();
                    } else if (item != null) {
                        if (name.equalsIgnoreCase("title")) {
                            Log.i("Attribute", "setLink");
                            item.setTitle(parser.nextText());
                        } else if (name.equalsIgnoreCase("description")) {
                            Log.i("Attribute", "description");
                            item.setDescription(parser.nextText());

                        }else if (name.equalsIgnoreCase("link")) {
                            Log.i("Attribute", "link");
                            item.setLink(parser.nextText().trim());
                        } else if (name.equals("georss:point")) {
                            Log.i("Attribute", "georss");
                            item.setGeoPoint(parser.nextText().trim());
                        } else if (name.equals("author")) {
                            Log.i("Attribute", "author");
                            item.setAuthor(parser.nextText().trim());
                        } else if (name.equalsIgnoreCase("comments")) {
                            Log.i("Attribute", "comments");
                            item.setComments(parser.nextText());
                        } else if (name.equalsIgnoreCase("pubDate")) {
                            Log.i("Attribute", "date");
                            item.setPubDate(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    Log.i("End tag", name);
                    if (name.equalsIgnoreCase("item") && item != null) {
                        Log.i("Added", item.toString());
                        incidents.add(item);
                    } else if (name.equalsIgnoreCase("channel")) {
                        done = true;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return incidents;
    }
}
