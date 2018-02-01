package nl.rug.www.rugsummerschools.networking;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import nl.rug.www.rugsummerschools.model.Announcement;
import nl.rug.www.rugsummerschools.model.Content;
import nl.rug.www.rugsummerschools.model.Event;
import nl.rug.www.rugsummerschools.model.EventsPerDay;
import nl.rug.www.rugsummerschools.model.ForumComment;
import nl.rug.www.rugsummerschools.model.ForumThread;
import nl.rug.www.rugsummerschools.model.GeneralInfo;
import nl.rug.www.rugsummerschools.model.Lecturer;
import nl.rug.www.rugsummerschools.model.LoginInfo;

/**
 * This class is to deal with all process for fetching data from server.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

@Deprecated
public class NetworkingService<T extends Content> {

    private static final String TAG = "NetworkingService";

    private static final String HTTP_URL = "turing13.housing.rug.nl:8800";

    public static final int LOGIN_CODE = 0;
    public static final int ANNOUNCEMENT = 1;
    public static final int GENERAL_INFO = 2;
    public static final int LECTURER = 3;
    public static final int TIMETABLE = 4;
    public static final int FORUM = 5;

    public interface VolleyCallback {
        void onResponse(String result);
        void onError(NetworkResponse result);
    }

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

    private String buildURL(List<String> paths, Map<String, String> queryParams) {
        Uri.Builder builder = new Uri.Builder();
        builder
                .scheme("http")
                .encodedAuthority(HTTP_URL)
                .appendPath("API");

        if (paths != null) {
            for (String s : paths) {
                builder.appendPath(s);
            }
        }

        if (queryParams != null) {
            Iterator it = queryParams.keySet().iterator();
            while(it.hasNext()) {
                String key = (String)it.next();
                builder.appendQueryParameter(key, queryParams.get(key));
                it.remove();
            }
        }

        String url = builder.toString();
        Log.d(TAG, "url : " + url);
        return url;
    }

    @SuppressWarnings("unchecked")
    public List<T> fetchData(int type, String code) {
        List<T> data = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        Map<String, String> queries = new HashMap<>();
        try {
            String jsonString;
            switch (type) {
                case LOGIN_CODE :
                    paths.add("logincode");
                    queries.put("code", code);
                    jsonString = getUrlString(buildURL(paths, queries));
                    parseLoginCode((List<LoginInfo>)data, new JSONObject(jsonString));
                    break;
                case ANNOUNCEMENT :
                    paths.add("announcement");
                    jsonString = getUrlString(buildURL(paths, null));
                    parseAnnouncements((List<Announcement>)data, new JSONArray(jsonString));
                    break;
                case GENERAL_INFO :
                    paths.add("generalinfo");
                    jsonString = getUrlString(buildURL(paths, null));
                    parseGeneralInfos((List<GeneralInfo>)data, new JSONArray(jsonString));
                    break;
                case LECTURER :
                    paths.add("lecturer");
                    jsonString = getUrlString(buildURL(paths, null));
                    parseLecturers((List<Lecturer>)data, new JSONArray(jsonString));
                    break;
                case TIMETABLE :
                    paths.add("event");
                    jsonString = getUrlString(buildURL(paths, null));
                    break;
                case FORUM :
                    paths.add("forum");
                    paths.add("thread");
                    jsonString = getUrlString(buildURL(paths, null));
                    parseForumThreads((List<ForumThread>)data, new JSONArray(jsonString));
                    break;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void parseLoginCode(List<LoginInfo> items, JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) return;

        LoginInfo info = new LoginInfo();
        info.setId(jsonObject.getString("_id"));
        info.setSchoolId(jsonObject.getString("school"));
        items.add(info);
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
                announcement.setPoster("Unknown");
            if (!contentJsonObject.isNull("created"))
                announcement.setDate(contentJsonObject.getString("created"));
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
//            generalInfo.setCategory(contentJsonObject.getString("category"));
            items.add(generalInfo);
        }
    }

    private void parseTimeTables(List<EventsPerDay> items, JSONObject jsonBody)
            throws IOException, JSONException {
//        if (jsonBody == null) return;
//
//        String data = jsonBody.getString("data");
//        JSONArray array = new JSONArray(data);
//        Log.d(TAG, array.toString());
//        for (int i = 0; i < array.length(); ++i) {
//            JSONArray dataArray = array.getJSONArray(i);
//            Date date = new DateTime(dataArray.getString(0), DateTimeZone.UTC).toDate();
//            JSONArray eventsArray = dataArray.getJSONArray(1);
//            SimpleDateFormat format2 = new SimpleDateFormat("(MMM-dd)", Locale.getDefault());
//            format2.setTimeZone(TimeZone.getTimeZone("UTC"));
//            SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault());
//            dayOfWeek.setTimeZone(TimeZone.getTimeZone("UTC"));
//            String title = dayOfWeek.format(date) + format2.format(date);
//            EventsPerDay timeTablePerDay = new EventsPerDay(title);
//            Log.d(TAG, date.toString());
//            Log.d(TAG, new Date().toString());
//            List<Object> childTimeTables = new ArrayList<>();
//
//            for (int j = 0; j < eventsArray.length(); ++j) {
//                Event event = new Event();
//                JSONObject object = new JSONObject(eventsArray.getString(j));
//                event.setId(object.getString("id"));
//                event.setTitle(object.getString("summary"));
//                event.setDescription(object.getString("description"));
//                event.setLocation(object.getString("location"));
//                JSONObject startDate = object.getJSONObject("start");
//                JSONObject endDate = object.getJSONObject("end");
//                event.setStartDate(startDate.getString("dateTime"));
//                Log.d(TAG, "startdate -- "+event.getStartDate());
//                event.setEndDate(endDate.getString("dateTime"));
//                Log.d(TAG, "enddate -- "+event.getEndDate());
//                childTimeTables.add(event);
//            }
//            timeTablePerDay.setChildObjectList(childTimeTables);
//            items.add(timeTablePerDay);
//        }
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
            lecturer.setWebsite(contentJsonObject.getString("website"));
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority(HTTP_URL)
                    .appendPath(contentJsonObject.getString("imagepath"));
            Log.d(TAG, "URL string :" + builder.toString());
            lecturer.setImgurl(builder.toString());

            items.add(lecturer);
        }
    }

    private void parseForumThreads(List<ForumThread> items, JSONArray jsonBody)
            throws IOException, JSONException {
        if (jsonBody == null) return;

        for (int i = 0; i < jsonBody.length(); i++) {
            JSONObject contentJsonObject = jsonBody.getJSONObject(i);

            ForumThread forumThread = new ForumThread();
            forumThread.setId(contentJsonObject.getString("_id"));
            forumThread.setTitle(contentJsonObject.getString("title"));
            forumThread.setDescription(contentJsonObject.getString("description"));
            forumThread.setPoster(contentJsonObject.getString("author"));
            forumThread.setDate(contentJsonObject.getString("created"));
            forumThread.setPosterId(contentJsonObject.getString("posterID"));
            forumThread.setImgUrl(contentJsonObject.getString("imgURL"));
            List<ForumComment> comments = new ArrayList<>();
            JSONArray jsonComments = contentJsonObject.getJSONArray("comments");
            for (int j = 0; j < jsonComments.length(); ++j) {
                Log.d(TAG, jsonComments.get(j).toString());
//                ForumComment comment = new ForumComment();
//                JSONObject jsonObject = jsonComments.getJSONObject(j);
//                if (jsonObject == null) break;
//                comment.setCommentId(jsonObject.getString("commentID"));
//                comment.setPosterId(jsonObject.getString("posterID"));
//                comment.setPoster(jsonObject.getString("author"));
//                comment.setText(jsonObject.getString("text"));
//                comment.setDate(jsonObject.getString("date"));
//                comment.setImgUrl(jsonObject.getString("imgurl"));
//                comments.add(comment);
            }
            forumThread.setForumCommentList(comments);
            items.add(forumThread);
        }
    }

    public void getDeleteRequest(Context context, int method, List<String> paths, Map<String, String> queryParams, final Map<String, String> valuePairs, final VolleyCallback callback) {
        String url = buildURL(paths, queryParams);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        callback.onError(error.networkResponse);
                    }
                });
        queue.add(request);
    }

    public void postPutRequest(Context context, int method, List<String> paths, Map<String, String> queryParams, final Map<String, String> valuePairs, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = buildURL(paths, queryParams);
        StringRequest request = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        callback.onError(error.networkResponse);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return valuePairs;
            }
        };
        queue.add(request);
    }

    public void postRequestFCMID(Context context, String Token){
        try{
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").encodedAuthority(HTTP_URL);
            builder.appendPath("token").appendQueryParameter("id", Token);
            String url = builder.toString();

            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "On response result (POST FCMID): " + response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error message (POST FCMID): " + error.toString());
                }
            });

            queue.add(stringRequest);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}