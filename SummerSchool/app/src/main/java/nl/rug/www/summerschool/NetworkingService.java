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

public class NetworkingService {

    private static final String TAG = "NetworkingService";

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
            String jsonString = getUrlString(builder.toString());
//            String jsonString = TEMP_JSON_ANNOUNCEMENT;
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
            String jsonString = getUrlString(builder.toString());
//            String jsonString = TEMP_JSON_GENERAL_INFO;
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
            String jsonString = getUrlString(builder.toString());
//            String jsonString = TEMP_JSON_TIMETABLE;
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
