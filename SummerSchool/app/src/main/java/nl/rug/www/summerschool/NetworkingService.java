package nl.rug.www.summerschool;

import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

/**
 * This class is to deal with all process for fetching data from online.
 * Fetching data from database works only local server of port 8080.
 * In order to fetch data from server, you should uncomment jsonString
 * on each fetch method, and comment temporary jsonString.
 * Only mobile running on emulator that runs server can access via "https://10.0.2.2:8080".
 * Otherwise, use temporary jsonStrings.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class NetworkingService {

    private static final String TAG = "NetworkingService";

    /** temporary JSON data fetched from the database */
    private static final String ANNOUNCEMENT_JSON = "[{\"_id\":\"58e0d5da735cbb602adbfda1\",\"title\":\"Welcome to Summer School\",\"description\":\"                            <h4>Welcome</h4><br>Welcome to the University of Groningen Summer School on 2017.<br><br>The University of Groningen (UoG) has a rich academic tradition dating back to 1614. From this tradition arose the first female student and the first female lecturer in the Netherlands, the first Dutch astronaut, the first president of the European Central Bank and, in 2016, a Nobel prize winner in Chemistry. <br><br>The UoG is a leading research institution and is among the worldâ€™s top 100 universities. Although highly research-driven and innovative, UoG acknowledges its heritage and has strong links to the northern Netherlands region. UoG aims to conduct research of high societal relevance and strongly connects with societal stakeholders. It differentiates itself in the international market by having a close link between education and research and by focusing on three key themes: Energy, Healthy Ageing, and Sustainable Society.<br><br>The summer school is an initiative of the Faculty of XXX , and it has been developed in conjunction with sponsors, partners. <br><br>The summer school is presented under the auspices of XXX.                                               \"},{\"_id\":\"58eab2560750057609a0ca6e\",\"title\":\"This is not a drill\",\"description\":\"Please turn off your cell phones and stow them in your carry on. \",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-04-09T22:14:46.104Z\"},{\"_id\":\"58eaae927901e2903a2ffe90\",\"title\":\"This is a test announcement\",\"description\":\"Hello world this is a cow.\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-04-09T21:58:42.376Z\"},{\"_id\":\"58ea6a3666c5ce143538dc3a\",\"title\":\"ive been wondering\",\"description\":\"about some stuff\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-04-09T17:07:02.918Z\"},{\"_id\":\"58ea6a2966c5ce143538dc39\",\"title\":\"hello\",\"description\":\"its me\\r\\n\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-04-09T17:06:49.129Z\"},{\"_id\":\"58ea69ea66c5ce143538dc37\",\"title\":\"Does this work?\",\"description\":\"Yes it does!!!<h4>WOOOP</h4>\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-04-09T17:05:46.566Z\"},{\"_id\":\"58e4e336e92900dc52157656\",\"title\":\"Hello\",\"description\":\"Bye\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-04-05T12:29:42.100Z\"},{\"_id\":\"58c179e28683ba42f86b192a\",\"title\":\"There has been some heavy rainfall\",\"description\":\"Don't mind me\"},{\"_id\":\"58c178298683ba42f86b1928\",\"title\":\"Hello\",\"description\":\"My andjls\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-03-09T15:43:37.915Z\"},{\"_id\":\"58bd28cdc64d4232b2d2e016\",\"title\":\"Meeting today\",\"description\":\"There is going to be a change in the lecture room for the meeting today we will be taking the lecture in the academia building lecture room 00.250\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-03-06T09:15:57.450Z\"},{\"_id\":\"58bd296bc64d4232b2d2e017\",\"title\":\"Hello everyone\",\"description\":\"I welcome everyone to this new summer winter school. Lets all have some fun :)\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-03-06T09:18:35.896Z\"}]";
    private static final String TIMETABLE_JSON = "[{\"kind\":\"calendar#event\",\"etag\":\"\\\"2982181530824000\\\"\",\"id\":\"va34vgjdbkov4eu6rvr2um35mo_20170402T052500Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=dmEzNHZnamRia292NGV1NnJ2cjJ1bTM1bW9fMjAxNzA0MDJUMDUyNTAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-01T23:52:45.000Z\",\"updated\":\"2017-04-01T23:52:45.534Z\",\"summary\":\"Hei\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-02T07:25:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-02T09:35:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"va34vgjdbkov4eu6rvr2um35mo\",\"originalStartTime\":{\"dateTime\":\"2017-04-02T07:25:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"va34vgjdbkov4eu6rvr2um35mo@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2983297763684000\\\"\",\"id\":\"igusucv8i6lhk705p68thoig58_20170403T082500Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=aWd1c3VjdjhpNmxoazcwNXA2OHRob2lnNThfMjAxNzA0MDNUMDgyNTAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-08T10:54:41.000Z\",\"updated\":\"2017-04-08T10:54:41.911Z\",\"summary\":\"Football\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-03T10:25:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-03T11:00:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"igusucv8i6lhk705p68thoig58\",\"originalStartTime\":{\"dateTime\":\"2017-04-03T10:25:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"igusucv8i6lhk705p68thoig58@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2982453914292000\\\"\",\"id\":\"ul1amah31ssp9ulnpot2rd6or0_20170404T103000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=dWwxYW1haDMxc3NwOXVsbnBvdDJyZDZvcjBfMjAxNzA0MDRUMTAzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-03T13:42:36.000Z\",\"updated\":\"2017-04-03T13:42:37.254Z\",\"summary\":\"Balloon\",\"description\":\"Null\",\"location\":\"Null\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-04T12:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-04T15:35:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"ul1amah31ssp9ulnpot2rd6or0\",\"originalStartTime\":{\"dateTime\":\"2017-04-04T12:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"ul1amah31ssp9ulnpot2rd6or0@google.com\",\"sequence\":0,\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2982454274571000\\\"\",\"id\":\"32iso46eior2q33k8pg397qf7s_20170406T032000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=MzJpc280NmVpb3IycTMzazhwZzM5N3FmN3NfMjAxNzA0MDZUMDMyMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-03T13:45:37.000Z\",\"updated\":\"2017-04-03T13:45:37.371Z\",\"summary\":\"Amber\",\"description\":\"Null\",\"location\":\"Null\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-06T05:20:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-06T09:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"32iso46eior2q33k8pg397qf7s\",\"originalStartTime\":{\"dateTime\":\"2017-04-06T05:20:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"32iso46eior2q33k8pg397qf7s@google.com\",\"sequence\":0,\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2983544081627000\\\"\",\"id\":\"j20qfnoealu9masfa16phuphdg_20170406T040000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=ajIwcWZub2VhbHU5bWFzZmExNnBodXBoZGdfMjAxNzA0MDZUMDQwMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-09T21:07:20.000Z\",\"updated\":\"2017-04-09T21:07:20.880Z\",\"summary\":\"fgads\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-06T06:00:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-06T08:00:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"j20qfnoealu9masfa16phuphdg\",\"originalStartTime\":{\"dateTime\":\"2017-04-06T06:00:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"j20qfnoealu9masfa16phuphdg@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2982447531657000\\\"\",\"id\":\"5u86mstfcog97ufndb8lk2l0d0_20170406T094500Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=NXU4Nm1zdGZjb2c5N3VmbmRiOGxrMmwwZDBfMjAxNzA0MDZUMDk0NTAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-03T12:49:25.000Z\",\"updated\":\"2017-04-03T12:49:25.891Z\",\"summary\":\"Frisbee\",\"description\":\"Null\",\"location\":\"Nettelbosje 2, 9747 AC Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-06T11:45:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-06T12:00:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"5u86mstfcog97ufndb8lk2l0d0\",\"originalStartTime\":{\"dateTime\":\"2017-04-06T11:45:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"transparency\":\"transparent\",\"visibility\":\"public\",\"iCalUID\":\"5u86mstfcog97ufndb8lk2l0d0@google.com\",\"sequence\":0,\"extendedProperties\":{\"shared\":{\"ssid\":\"ssid\",\"summerSchoolId\":\"Null\"}},\"anyoneCanAddSelf\":true,\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":60}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2982455292806000\\\"\",\"id\":\"rugh5oaa0o25gh1o4oie03d1ec_20170407T073000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=cnVnaDVvYWEwbzI1Z2gxbzRvaWUwM2QxZWNfMjAxNzA0MDdUMDczMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-03T13:54:06.000Z\",\"updated\":\"2017-04-03T13:54:06.482Z\",\"summary\":\"Kanel\",\"description\":\"Null\",\"location\":\"Nettelbosje 2, 9747 AC Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-07T09:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-07T10:25:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"rugh5oaa0o25gh1o4oie03d1ec\",\"originalStartTime\":{\"dateTime\":\"2017-04-07T09:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"transparency\":\"transparent\",\"visibility\":\"public\",\"iCalUID\":\"rugh5oaa0o25gh1o4oie03d1ec@google.com\",\"sequence\":0,\"extendedProperties\":{\"shared\":{\"ssid\":\"ssid\"}},\"anyoneCanAddSelf\":true,\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":60}]}}]";
    private static final String GENERALINFO_JSON = "[{\"_id\":\"58eab6d807d4868c09cb7e75\",\"title\":\"This can't be edited, it's just a local database.@\",\"description\":\"                            There's no such thing as an remote database.                         \"},{\"_id\":\"58eab2780750057609a0ca6f\",\"title\":\"About leaving.\",\"description\":\"<h4>DONT TELL ME WHAT TO DO I AM LEAVING</h4>                         \"},{\"_id\":\"58ea6a0666c5ce143538dc38\",\"title\":\"This works also\",\"description\":\"Yep it does <h4>Woop</h4>\"},{\"_id\":\"58ea603139827958338bd533\",\"title\":\"hi\",\"description\":\"this doenst owrk\"},{\"_id\":\"58c69492b47e3e3f0ed08751\",\"title\":\"Housing\",\"description\":\"                                                                                    <h4>International Student Housing in Groningen</h4>The University of Groningen does not have a university campus. Instead, our buildings and faculties can be found throughout the city of Groningen. To accommodate international students, we work closely together with the SSH, a non-profit housing organisation that offers furnished accommodation in Groningen for (international) students in one of our International Student Houses.<br><br>These houses are located throughout the city of Groningen, which makes it a great experience to live in the city! The buildings are within walking distance of the city centre and the summer school venue.<br><br>All rooms in the International Student Houses are fully furnished and have Wi-Fi. The kitchens and bathrooms are shared, usually with no more than 7 other students.<h4>Housing for summer school participants</h4>Many of our July and August summer schools offer housing to those participants who need accommodation. The service is optional, please indicate on your online application form if you want to have accommodation arranged for you.<br><br>If you agree to have housing arranged for you, you will be offered a standard package with the following conditions:<ul><li>Standard rental from Saturday 2PM to Saturday 10AM.</li><li>Standard rent price for one week: â‚¬ 150 (7 nights). This includes cleaning, bed package, basic kitchen utensils and a practical welcoming package. The price for two weeks (10-14 nights) is â‚¬ 350.</li><li>Check-in is available on Saturday between 2PM and 8PM. If you arrive later, please contact your summer school coordinator as soon as possible, so arrangements can be made to welcome you.</li></ul>We will try to lodge all participants close together, in the same building.                                                                        \"},{\"_id\":\"58c693a1b47e3e3f0ed08750\",\"title\":\"Health Insurance!\",\"description\":\"                        The Netherlands has a high standard of medical care. It is, however, very important to be properly insured. Medical costs can be very high, especially if they include a stay in a hospital. Students at institutes of higher education are not automatically insured against medical expenses abroad. You must arrange this yourself.All international students in the Netherlands must have health insurance. What kind of health insurance applies to you depends on your country of origin, age and the duration of your stay.We expect all summer school participants to have arranged proper health insurance before arriving in Groningen. You can apply for a short term private health insurance through Aon or IPS.NB Participants or guest lecturers who need a visa are required to show proof of a medical insurance.assaddsadasdsa                    \"},{\"_id\":\"58c68b08b47e3e3f0ed0874f\",\"title\":\"Header title\",\"description\":\"                            Description                        \"},{\"_id\":\"58c68a83b47e3e3f0ed0874d\",\"title\":\"Housing\",\"description\":\"Housing is supreme in the Netherlands. Houses can be found around every street corner. The cheapest houses can be found around the suburbans of Groningen. The most popular area is Paddepoel.\"},{\"_id\":\"58c179ef8683ba42f86b192b\",\"title\":\"NBLasfjl\",\"description\":\"jsadgn;aofg'\"},{\"_id\":\"58c178328683ba42f86b1929\",\"title\":\"Blabla\",\"description\":\"jkansf\\r\\n\"},{\"_id\":\"58c16059eecc31141420e50c\",\"title\":\"this\",\"description\":\"is a test\"},{\"_id\":\"58c15b2f8d1b573402798699\",\"title\":\"this should be correct\",\"description\":\"yep it is\"},{\"_id\":\"58bd31e1f36d2837b810b5d9\",\"title\":\"Testing 2\",\"description\":\"This is a second testing general info\"},{\"_id\":\"58bd31b1f36d2837b810b5c3\",\"title\":\"General info test\",\"description\":\"This is a general info description example on how it will look like.\"},{\"_id\":\"58bd314cf36d2837b810b5a5\",\"title\":\"Housing\",\"description\":\"Housing in Groningen is fun. There are many houses available, pick one from our list of items. Enjoy the awesome villa in the back end of the park. Furthermore, there are plenty\"}]";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Announcement> fetchAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();

        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("10.0.2.2:8080")
                    .appendPath("announcement")
                    .appendPath("item");
            Log.d(TAG, "URL string :" + builder.toString());
//            String jsonString = getUrlString(builder.toString()); // fetch data from online database
            String jsonString = ANNOUNCEMENT_JSON; // use temporary json
            Log.i(TAG, "Received Announcement JSON: " + jsonString);
            JSONArray jsonBody = new JSONArray(jsonString);
            parseAnnouncements(announcements, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch Announcements", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse Announcement JSON", je);
        }

        return announcements;
    }

    public List<GeneralInfo> fetchGeneralInfos() {

        List<GeneralInfo> generalInfos = new ArrayList<>();

        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").
                    encodedAuthority("10.0.2.2:8080")
                    .appendPath("generalinfo")
                    .appendPath("item");
            Log.d(TAG, "URL string :" + builder.toString());
//            String jsonString = getUrlString(builder.toString());
            String jsonString = GENERALINFO_JSON;
            Log.i(TAG, "Received GeneralInfo JSON: " + jsonString);
            JSONArray jsonBody = new JSONArray(jsonString);
            parseGeneralInfos(generalInfos, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch GeneralInfos", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse GeneralInfo JSON", je);
        }

        fetchTimeTables();
        return generalInfos;
    }

    public List<TimeTable> fetchTimeTables() {

        List<TimeTable> timeTables = new ArrayList<>();

        try {

            Calendar c = GregorianCalendar.getInstance();

            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String startDate = "", endDate = "";

            startDate = df.format(c.getTime());
            c.add(Calendar.DATE, 7);
            endDate = df.format(c.getTime());

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").
                    encodedAuthority("10.0.2.2:8080")
                    .appendPath("calendar")
                    .appendPath("event")
                    .appendQueryParameter("startDate", startDate + "T00:00:00.000Z")
                    .appendQueryParameter("endDate", endDate + "T00:00:00.000Z");
            Log.d(TAG, "URL string :" + builder.toString());
//            String jsonString = getUrlString(builder.toString());
            String jsonString = TIMETABLE_JSON;
            Log.i(TAG, "Received TimeTable JSON: " + jsonString);
            JSONArray jsonBody = new JSONArray(jsonString);
            parseTimeTables(timeTables, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch TimeTables", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse TimeTable JSON", je);
        }

        return timeTables;
    }

    private void parseAnnouncements(List<Announcement> items, JSONArray jsonBody)
            throws IOException, JSONException {

        for (int i = 0; i < jsonBody.length(); i++) {
            JSONObject contentJsonObject = jsonBody.getJSONObject(i);

            Announcement announcement = new Announcement();
            announcement.setId(contentJsonObject.getString("_id"));
            announcement.setTitle(contentJsonObject.getString("title"));
            announcement.setDescription(contentJsonObject.getString("description"));
            if (!contentJsonObject.isNull("poster"))
                announcement.setPoster(contentJsonObject.getString("poster"));
            else
                announcement.setPoster("");
            if (!contentJsonObject.isNull("date"))
                announcement.setDate(contentJsonObject.getString("date"));
            else
                announcement.setDate("");

            items.add(announcement);
        }
    }

    private void parseGeneralInfos(List<GeneralInfo> items, JSONArray jsonBody)
            throws IOException, JSONException {

        for (int i = 0; i < jsonBody.length(); i++) {
            JSONObject contentJsonObject = jsonBody.getJSONObject(i);

            GeneralInfo generalInfo = new GeneralInfo();
            generalInfo.setId(contentJsonObject.getString("_id"));
            generalInfo.setTitle(contentJsonObject.getString("title"));
            generalInfo.setDescription(contentJsonObject.getString("description"));

            items.add(generalInfo);
        }
    }

    private void parseTimeTables(List<TimeTable> items, JSONArray jsonBody)
            throws IOException, JSONException {

        for (int i = 0; i < jsonBody.length(); i++) {
            JSONObject contentJsonObject = jsonBody.getJSONObject(i);

            TimeTable timeTable = new TimeTable();
            timeTable.setId(contentJsonObject.getString("id"));
            timeTable.setTitle(contentJsonObject.getString("summary"));
            timeTable.setDescription(contentJsonObject.getString("description"));
            timeTable.setLocation(contentJsonObject.getString("location"));
            timeTable.setDate(contentJsonObject.getString("updated"));
            JSONObject startDate = contentJsonObject.getJSONObject("start");
            timeTable.setStartDate(startDate.getString("dateTime"));
            JSONObject endDate = contentJsonObject.getJSONObject("end");
            timeTable.setEndDate(endDate.getString("dateTime"));

            items.add(timeTable);
        }
    }
}
