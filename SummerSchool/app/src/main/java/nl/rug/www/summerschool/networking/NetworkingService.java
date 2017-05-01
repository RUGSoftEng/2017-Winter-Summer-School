package nl.rug.www.summerschool.networking;

import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import nl.rug.www.summerschool.model.Announcement;
import nl.rug.www.summerschool.model.GeneralInfo;
import nl.rug.www.summerschool.model.Lecturer;
import nl.rug.www.summerschool.model.TimeTable;

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

    private static final String URL_DATABASE = "summer-schools.herokuapp.com";

    public static final int ANNOUNCEMENT = 0;
    public static final int GENERAL_INFO = 1;
    public static final int LECTURER = 2;
    public static final int TIMETABLE = 3;

    private byte[] getUrlBytes(String urlSpec) throws IOException {
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
    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private JSONArray buildJSONArray(int type) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").encodedAuthority(URL_DATABASE);
        switch (type) {
            case ANNOUNCEMENT :
                builder.appendPath("announcement").appendPath("item");
                break;
            case GENERAL_INFO :
                builder.appendPath("generalinfo").appendPath("item");
                break;
            case TIMETABLE :
                Calendar c = GregorianCalendar.getInstance();

                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String startDate = "", endDate = "";

                startDate = df.format(c.getTime());
                c.add(Calendar.DATE, 7);
                endDate = df.format(c.getTime());

                builder.appendPath("calendar").appendPath("event")
                        .appendQueryParameter("startDate", startDate + "T00:00:00.000Z")
                        .appendQueryParameter("endDate", endDate + "T00:00:00.000Z");
                break;
            case LECTURER :
                builder.appendPath("lecturer")
                        .appendPath("item");
                break;
        }
        String jsonString = "";
        try {
            jsonString = getUrlString(builder.toString());
            return new JSONArray(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Announcement> fetchAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        try {
            parseAnnouncements(announcements, buildJSONArray(ANNOUNCEMENT));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return announcements;
    }

    public List<GeneralInfo> fetchGeneralInfos() {

        List<GeneralInfo> generalInfos = new ArrayList<>();

        try {
            parseGeneralInfos(generalInfos, buildJSONArray(GENERAL_INFO));
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch GeneralInfos", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse GeneralInfo JSON", je);
        }

        return generalInfos;
    }

    public List<TimeTable> fetchTimeTables() {

        List<TimeTable> timeTables = new ArrayList<>();

        try {
            parseTimeTables(timeTables, buildJSONArray(TIMETABLE));
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch TimeTables", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse TimeTable JSON", je);
        }

        return timeTables;
    }

    public List<Lecturer> fetchLecturers() {

        List<Lecturer> lecturers = new ArrayList<>();

        try {
            parseLecturers(lecturers, buildJSONArray(LECTURER));
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch TimeTables", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse TimeTable JSON", je);
        }

        return lecturers;
    }

    private void parseAnnouncements(List<Announcement> items, JSONArray jsonBody)
            throws IOException, JSONException {
        if (jsonBody == null) return;

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
        if (jsonBody == null) return;

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
        if (jsonBody == null) return;

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

    private void parseLecturers(List<Lecturer> items, JSONArray jsonBody)
            throws IOException, JSONException {
        if (jsonBody == null) return;

        for (int i = 0; i < jsonBody.length(); i++) {
            JSONObject contentJsonObject = jsonBody.getJSONObject(i);

            Lecturer lecturer = new Lecturer();
            lecturer.setId(contentJsonObject.getString("_id"));
            lecturer.setTitle(contentJsonObject.getString("name"));
            lecturer.setDescription(contentJsonObject.getString("description"));
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority(URL_DATABASE)
                    .appendPath(contentJsonObject.getString("imagepath"));
            Log.d(TAG, "URL string :" + builder.toString());
            URL url = new URL(builder.toString());
            InputStream content = (InputStream)url.getContent();
            Drawable picture = Drawable.createFromStream(content, "src");
            lecturer.setProfilePicture(picture);

            items.add(lecturer);
        }
    }
}