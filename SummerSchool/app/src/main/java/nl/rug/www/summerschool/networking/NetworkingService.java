package nl.rug.www.summerschool.networking;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.model.Announcement;
import nl.rug.www.summerschool.model.GeneralInfo;
import nl.rug.www.summerschool.model.Lecturer;
import nl.rug.www.summerschool.model.Event;
import nl.rug.www.summerschool.model.EventsPerDay;

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

    private static final int ANNOUNCEMENT = 0;
    private static final int GENERAL_INFO = 1;
    private static final int LECTURER = 2;
    private static final int TIMETABLE = 3;

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
            int bytesRead;
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
            case LECTURER :
                builder.appendPath("lecturer")
                        .appendPath("item");
                break;
        }
        String jsonString;
        try {
            jsonString = getUrlString(builder.toString());
            return new JSONArray(jsonString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject buildJSONObject(int week) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").encodedAuthority(URL_DATABASE).appendPath("calendar").appendPath("event")
                .appendQueryParameter("week", week + "");
        try {
            String jsonString = getUrlString(builder.toString());
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Announcement> fetchAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        try {
            parseAnnouncements(announcements, buildJSONArray(ANNOUNCEMENT));
        } catch (IOException | JSONException e) {
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

    public List<EventsPerDay> fetchTimeTables(int week) {

        List<EventsPerDay> timeTables = new ArrayList<>();

        try {
            parseTimeTables(timeTables, buildJSONObject(week));
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch TimeTables", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse Event JSON", je);
        }

        return timeTables;
    }

    public List<Lecturer> fetchLecturers() {

        List<Lecturer> lecturers = new ArrayList<>();

        try {
            parseLecturers(lecturers, buildJSONArray(LECTURER));
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch Lecturers", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse Lecturers JSON", je);
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

    private void parseTimeTables(List<EventsPerDay> items, JSONObject jsonBody)
            throws IOException, JSONException {
        if (jsonBody == null) return;

        String data = jsonBody.getString("data");
        JSONArray array = new JSONArray(data);
        Log.d(TAG, array.toString());
        for (int i = 0; i < array.length(); ++i) {
            JSONArray dataArray = array.getJSONArray(i);
            String[] parts = dataArray.getString(0).split("T");
            JSONArray eventsArray = dataArray.getJSONArray(1);
            EventsPerDay timeTablePerDay = new EventsPerDay(parts[0]);
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat format2 = new SimpleDateFormat(" (MMM-dd)", Locale.getDefault());
                SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault());
                Date date = format.parse(parts[0]);
                String title = dayOfWeek.format(date) + format2.format(date);
                timeTablePerDay = new EventsPerDay(title);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d(TAG, parts[0]);
            List<Object> childTimeTables = new ArrayList<>();

            for (int j = 0; j < eventsArray.length(); ++j) {
                Event event = new Event();
                JSONObject object = new JSONObject(eventsArray.getString(j));
                event.setId(object.getString("id"));
                event.setTitle(object.getString("summary"));
                JSONObject startDate = object.getJSONObject("start");
                JSONObject endDate = object.getJSONObject("end");
                event.setStartDate(startDate.getString("dateTime"));
                event.setEndDate(endDate.getString("dateTime"));
                childTimeTables.add(event);
            }
            timeTablePerDay.setChildObjectList(childTimeTables);
            items.add(timeTablePerDay);
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
            InputStream content;
            try {
                content = (InputStream) url.getContent();
                Drawable picture = Drawable.createFromStream(content, "src");
                lecturer.setProfilePicture(picture);
            } catch (FileNotFoundException e) {

            }

            items.add(lecturer);
        }
    }
}