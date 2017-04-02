package nl.rug.www.summerschool;

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
import java.util.ArrayList;
import java.util.List;

public class NetworkingService {

    private static final String TAG = "NetworkingService";
    private static final String TEMP_JSON_ANNOUNCEMENT = "[{\"_id\":\"58e0d5da735cbb602adbfda1\",\"title\":\"Welcome to Summer School\",\"description\":\"Welcome to Summer School!!\\r\\n\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-04-02T10:43:38.323Z\"},{\"_id\":\"58bd2a2cc64d4232b2d2e018\",\"title\":\"Lecture Cancellation\",\"description\":\"I am terribly sorry but I have to cancel the meeting for today. I got a really bad case of the flue and I will not be able to lecture. Apologiesaa aaaaaaaaaaaaa aaaaaaaaaa aaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaa aaaaaaaaa aa aaaaaa aaaaaaaaa aaaaaa aaaaaaaaa aaaaaaaa aaaa aaaaaaaaaa aaaaaaaass ssssssssss ssdaaaaaaa aaaaaaaaaaa aaaaaaaaaaaa aaaaaaaa\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-03-06T09:21:48.325Z\"},{\"_id\":\"58c1ffae465a99102918db77\",\"title\":\"This is a serious test.\",\"description\":\"I would like to talk about testing websites and how this hould be done in a responsible manner bla bla bla bla bla bla bla bla bla bla asdbnsad uiospd ahalsjkdhba;sdaalsdjkbasdk'l;sdjaa a;skdljha;skhld asdhjas\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-03-10T01:21:50.523Z\"},{\"_id\":\"58c179e28683ba42f86b192a\",\"title\":\"There has been some heavy rainfall\",\"description\":\"Don't mind me\"},{\"_id\":\"58c178298683ba42f86b1928\",\"title\":\"Hello\",\"description\":\"My andjls\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-03-09T15:43:37.915Z\"},{\"_id\":\"58bd28cdc64d4232b2d2e016\",\"title\":\"Meeting today\",\"description\":\"There is going to be a change in the lecture room for the meeting today we will be taking the lecture in the academia building lecture room 00.250\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-03-06T09:15:57.450Z\"},{\"_id\":\"58bd296bc64d4232b2d2e017\",\"title\":\"Hello everyone\",\"description\":\"I welcome everyone to this new summer winter school. Lets all have some fun :)\",\"poster\":\"Nikolas Hadjipanayi\",\"date\":\"2017-03-06T09:18:35.896Z\"}]";
    private static final String TEMP_JSON_GENERAL_INFO = "[{\"_id\":\"58c69492b47e3e3f0ed08751\",\"title\":\"Housing\",\"description\":\"                                                                                    <h4>International Student Housing in Groningen</h4>The University of Groningen does not have a university campus. Instead, our buildings and faculties can be found throughout the city of Groningen. To accommodate international students, we work closely together with the SSH, a non-profit housing organisation that offers furnished accommodation in Groningen for (international) students in one of our International Student Houses.<br><br>These houses are located throughout the city of Groningen, which makes it a great experience to live in the city! The buildings are within walking distance of the city centre and the summer school venue.<br><br>All rooms in the International Student Houses are fully furnished and have Wi-Fi. The kitchens and bathrooms are shared, usually with no more than 7 other students.<h4>Housing for summer school participants</h4>Many of our July and August summer schools offer housing to those participants who need accommodation. The service is optional, please indicate on your online application form if you want to have accommodation arranged for you.<br><br>If you agree to have housing arranged for you, you will be offered a standard package with the following conditions:<ul><li>Standard rental from Saturday 2PM to Saturday 10AM.</li><li>Standard rent price for one week: â‚¬ 150 (7 nights). This includes cleaning, bed package, basic kitchen utensils and a practical welcoming package. The price for two weeks (10-14 nights) is â‚¬ 350.</li><li>Check-in is available on Saturday between 2PM and 8PM. If you arrive later, please contact your summer school coordinator as soon as possible, so arrangements can be made to welcome you.</li></ul>We will try to lodge all participants close together, in the same building.                                                                        \"},{\"_id\":\"58c693a1b47e3e3f0ed08750\",\"title\":\"Health Insurance!\",\"description\":\"                        The Netherlands has a high standard of medical care. It is, however, very important to be properly insured. Medical costs can be very high, especially if they include a stay in a hospital. Students at institutes of higher education are not automatically insured against medical expenses abroad. You must arrange this yourself.All international students in the Netherlands must have health insurance. What kind of health insurance applies to you depends on your country of origin, age and the duration of your stay.We expect all summer school participants to have arranged proper health insurance before arriving in Groningen. You can apply for a short term private health insurance through Aon or IPS.NB Participants or guest lecturers who need a visa are required to show proof of a medical insurance.assaddsadasdsa                    \"},{\"_id\":\"58c68b08b47e3e3f0ed0874f\",\"title\":\"Header title\",\"description\":\"                            Description                        \"},{\"_id\":\"58c68a83b47e3e3f0ed0874d\",\"title\":\"Housing\",\"description\":\"Housing is supreme in the Netherlands. Houses can be found around every street corner. The cheapest houses can be found around the suburbans of Groningen. The most popular area is Paddepoel.\"},{\"_id\":\"58c179ef8683ba42f86b192b\",\"title\":\"NBLasfjl\",\"description\":\"jsadgn;aofg'\"},{\"_id\":\"58c178328683ba42f86b1929\",\"title\":\"Blabla\",\"description\":\"jkansf\\r\\n\"},{\"_id\":\"58c16059eecc31141420e50c\",\"title\":\"this\",\"description\":\"is a test\"},{\"_id\":\"58c15b2f8d1b573402798699\",\"title\":\"this should be correct\",\"description\":\"yep it is\"},{\"_id\":\"58bd31e1f36d2837b810b5d9\",\"title\":\"Testing 2\",\"description\":\"This is a second testing general info\"},{\"_id\":\"58bd31b1f36d2837b810b5c3\",\"title\":\"General info test\",\"description\":\"This is a general info description example on how it will look like.\"},{\"_id\":\"58bd314cf36d2837b810b5a5\",\"title\":\"Housing\",\"description\":\"Housing in Groningen is fun. There are many houses available, pick one from our list of items. Enjoy the awesome villa in the back end of the park. Furthermore, there are plenty\"}]";
    private static final String TEMP_JSON_TIMETABLE = "[{\"kind\":\"calendar#event\",\"etag\":\"\\\"2980193349682000\\\"\",\"id\":\"gsqs99ejl39gqmjntn8lc2p0lk_20170323T193000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=Z3Nxczk5ZWpsMzlncW1qbnRuOGxjMnAwbGtfMjAxNzAzMjNUMTkzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-21T11:44:34.000Z\",\"updated\":\"2017-03-21T11:44:34.918Z\",\"summary\":\"Google I/O 2015\",\"description\":\"Just coffee.\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-23T20:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-23T21:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"gsqs99ejl39gqmjntn8lc2p0lk\",\"originalStartTime\":{\"dateTime\":\"2017-03-23T20:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"gsqs99ejl39gqmjntn8lc2p0lk@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2980547753941000\\\"\",\"id\":\"l0p9t9uc0sqciceptlpdm9khqs_20170323T193000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=bDBwOXQ5dWMwc3FjaWNlcHRscGRtOWtocXNfMjAxNzAzMjNUMTkzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-23T12:57:56.000Z\",\"updated\":\"2017-03-23T12:57:57.016Z\",\"summary\":\"Sand\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-23T20:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-23T20:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"l0p9t9uc0sqciceptlpdm9khqs\",\"originalStartTime\":{\"dateTime\":\"2017-03-23T20:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"l0p9t9uc0sqciceptlpdm9khqs@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2980227001742000\\\"\",\"id\":\"v3mknf8c9grn0fbmm6cp0422to_20170323T193000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=djNta25mOGM5Z3JuMGZibW02Y3AwNDIydG9fMjAxNzAzMjNUMTkzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-21T16:25:00.000Z\",\"updated\":\"2017-03-21T16:25:00.937Z\",\"summary\":\"Coffee\",\"description\":\"Just coffee.\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-23T20:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-23T21:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"v3mknf8c9grn0fbmm6cp0422to\",\"originalStartTime\":{\"dateTime\":\"2017-03-23T20:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"v3mknf8c9grn0fbmm6cp0422to@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2980558575485000\\\"\",\"id\":\"i8ekaeos5uvhl0uh4nqqliacbg_20170324T053000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=aThla2Flb3M1dXZobDB1aDRucXFsaWFjYmdfMjAxNzAzMjRUMDUzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-23T14:28:07.000Z\",\"updated\":\"2017-03-23T14:28:07.835Z\",\"summary\":\"Delete dis\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-24T06:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-24T09:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"i8ekaeos5uvhl0uh4nqqliacbg\",\"originalStartTime\":{\"dateTime\":\"2017-03-24T06:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"i8ekaeos5uvhl0uh4nqqliacbg@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2980548881494000\\\"\",\"id\":\"s6oqlmrm185jsigfmkjsl1ulpg_20170324T113000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=czZvcWxtcm0xODVqc2lnZm1ranNsMXVscGdfMjAxNzAzMjRUMTEzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-23T13:07:20.000Z\",\"updated\":\"2017-03-23T13:07:20.801Z\",\"summary\":\"Lunch\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-24T12:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-24T13:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"s6oqlmrm185jsigfmkjsl1ulpg\",\"originalStartTime\":{\"dateTime\":\"2017-03-24T12:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"s6oqlmrm185jsigfmkjsl1ulpg@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2980547017815000\\\"\",\"id\":\"mh4mlsdgk9auvmt1ohfmqk0j4o_20170325T193000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=bWg0bWxzZGdrOWF1dm10MW9oZm1xazBqNG9fMjAxNzAzMjVUMTkzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-23T12:51:48.000Z\",\"updated\":\"2017-03-23T12:51:48.956Z\",\"summary\":\"Moon\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-25T20:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-26T00:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"mh4mlsdgk9auvmt1ohfmqk0j4o\",\"originalStartTime\":{\"dateTime\":\"2017-03-25T20:30:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"mh4mlsdgk9auvmt1ohfmqk0j4o@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2981756463546000\\\"\",\"id\":\"0fel8o94fjdsmlc90qb2u42fpk_20170325T235900Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=MGZlbDhvOTRmamRzbWxjOTBxYjJ1NDJmcGtfMjAxNzAzMjVUMjM1OTAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-30T12:50:31.000Z\",\"updated\":\"2017-03-30T12:50:31.878Z\",\"summary\":\"food\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-26T00:59:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-27T01:59:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"0fel8o94fjdsmlc90qb2u42fpk\",\"originalStartTime\":{\"dateTime\":\"2017-03-26T00:59:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"0fel8o94fjdsmlc90qb2u42fpk@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2981755560781000\\\"\",\"id\":\"s86e4qa16pj7crbhasfm88mlbg_20170325T235900Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=czg2ZTRxYTE2cGo3Y3JiaGFzZm04OG1sYmdfMjAxNzAzMjVUMjM1OTAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-30T12:43:00.000Z\",\"updated\":\"2017-03-30T12:43:00.451Z\",\"summary\":\"food\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-26T00:59:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-27T01:59:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"s86e4qa16pj7crbhasfm88mlbg\",\"originalStartTime\":{\"dateTime\":\"2017-03-26T00:59:00+01:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"s86e4qa16pj7crbhasfm88mlbg@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2980547273042000\\\"\",\"id\":\"frb5d2jq4na0j3ct4ompipposo_20170326T083000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=ZnJiNWQyanE0bmEwajNjdDRvbXBpcHBvc29fMjAxNzAzMjZUMDgzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-23T12:53:56.000Z\",\"updated\":\"2017-03-23T12:53:56.565Z\",\"summary\":\"Yarn\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-26T10:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-26T10:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"frb5d2jq4na0j3ct4ompipposo\",\"originalStartTime\":{\"dateTime\":\"2017-03-26T10:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"frb5d2jq4na0j3ct4ompipposo@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2981775654057000\\\"\",\"id\":\"1gk4qquba6qgggm5b16rrrn5u0_20170329T093000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=MWdrNHFxdWJhNnFnZ2dtNWIxNnJycm41dTBfMjAxNzAzMjlUMDkzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-30T15:30:26.000Z\",\"updated\":\"2017-03-30T15:30:27.117Z\",\"summary\":\"Launch\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-29T11:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-30T11:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"1gk4qquba6qgggm5b16rrrn5u0\",\"originalStartTime\":{\"dateTime\":\"2017-03-29T11:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"1gk4qquba6qgggm5b16rrrn5u0@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2981776007740000\\\"\",\"id\":\"naj8um6dhlf4o7tfobuh76pu10_20170329T093000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=bmFqOHVtNmRobGY0bzd0Zm9idWg3NnB1MTBfMjAxNzAzMjlUMDkzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-03-30T15:33:23.000Z\",\"updated\":\"2017-03-30T15:33:23.916Z\",\"summary\":\"Dream\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-03-29T11:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-03-29T13:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"naj8um6dhlf4o7tfobuh76pu10\",\"originalStartTime\":{\"dateTime\":\"2017-03-29T11:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"naj8um6dhlf4o7tfobuh76pu10@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2982181530824000\\\"\",\"id\":\"va34vgjdbkov4eu6rvr2um35mo_20170402T052500Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=dmEzNHZnamRia292NGV1NnJ2cjJ1bTM1bW9fMjAxNzA0MDJUMDUyNTAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-01T23:52:45.000Z\",\"updated\":\"2017-04-01T23:52:45.534Z\",\"summary\":\"Hei\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-02T07:25:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-02T09:35:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"va34vgjdbkov4eu6rvr2um35mo\",\"originalStartTime\":{\"dateTime\":\"2017-04-02T07:25:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"va34vgjdbkov4eu6rvr2um35mo@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2982268251371000\\\"\",\"id\":\"3rub883j5kt2jn0liebisl7hq0_20170402T053000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=M3J1Yjg4M2o1a3Qyam4wbGllYmlzbDdocTBfMjAxNzA0MDJUMDUzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-02T11:55:25.000Z\",\"updated\":\"2017-04-02T11:55:25.743Z\",\"summary\":\"Event\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-02T07:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-02T21:50:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"3rub883j5kt2jn0liebisl7hq0\",\"originalStartTime\":{\"dateTime\":\"2017-04-02T07:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"3rub883j5kt2jn0liebisl7hq0@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2982265222280000\\\"\",\"id\":\"3kintuq4u38m4ap1c4fejlv8d0_20170402T113000Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=M2tpbnR1cTR1MzhtNGFwMWM0ZmVqbHY4ZDBfMjAxNzA0MDJUMTEzMDAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-02T11:30:11.000Z\",\"updated\":\"2017-04-02T11:30:11.219Z\",\"summary\":\"New event\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-02T13:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-02T21:45:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"3kintuq4u38m4ap1c4fejlv8d0\",\"originalStartTime\":{\"dateTime\":\"2017-04-02T13:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"3kintuq4u38m4ap1c4fejlv8d0@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}},{\"kind\":\"calendar#event\",\"etag\":\"\\\"2982266048897000\\\"\",\"id\":\"70k90g8ops6mtfsbm2bav3q6e0_20170402T143500Z\",\"status\":\"confirmed\",\"htmlLink\":\"https://www.google.com/calendar/event?eid=NzBrOTBnOG9wczZtdGZzYm0yYmF2M3E2ZTBfMjAxNzA0MDJUMTQzNTAwWiBydWcubmxfN21rMWs5ZmN1MGExbzkwbzQyOGVsdmpiYmdAZw\",\"created\":\"2017-04-02T11:37:04.000Z\",\"updated\":\"2017-04-02T11:37:04.502Z\",\"summary\":\"Some event\",\"description\":\"No description provided\",\"location\":\"Zernike Campus, University of Groningen\",\"creator\":{\"email\":\"schedulingagent@rugwintersummerschool.iam.gserviceaccount.com\"},\"organizer\":{\"email\":\"rug.nl_7mk1k9fcu0a1o90o428elvjbbg@group.calendar.google.com\",\"displayName\":\"RUGWinterSummerSchool\",\"self\":true},\"start\":{\"dateTime\":\"2017-04-02T16:35:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"end\":{\"dateTime\":\"2017-04-02T19:30:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"recurringEventId\":\"70k90g8ops6mtfsbm2bav3q6e0\",\"originalStartTime\":{\"dateTime\":\"2017-04-02T16:35:00+02:00\",\"timeZone\":\"Europe/Amsterdam\"},\"iCalUID\":\"70k90g8ops6mtfsbm2bav3q6e0@google.com\",\"sequence\":0,\"attendees\":[{\"email\":\"lpage@example.com\",\"responseStatus\":\"needsAction\"}],\"reminders\":{\"useDefault\":false,\"overrides\":[{\"method\":\"popup\",\"minutes\":10}]}}]";

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
                    .encodedAuthority("localhost:8080")
                    .appendPath("announcement")
                    .appendPath("item");
            Log.d(TAG, "URL string :" + builder.toString());
//            String jsonString = getUrlString(builder.toString());
            String jsonString = TEMP_JSON_ANNOUNCEMENT;
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
                    encodedAuthority("localhost:8080")
                    .appendPath("generalinfo")
                    .appendPath("item");
            Log.d(TAG, "URL string :" + builder.toString());
//            String jsonString = getUrlString(builder.toString());
            String jsonString = TEMP_JSON_GENERAL_INFO;
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
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").
                    encodedAuthority("localhost:8080")
                    .appendPath("calendar")
                    .appendPath("event")
                    .appendQueryParameter("startDate", "2017-03-01T22:00:00.000Z")
                    .appendQueryParameter("endDate", "2017-04-02T22:00:00.000Z");
            Log.d(TAG, "URL string :" + builder.toString());
//            String jsonString = getUrlString(builder.toString());
            String jsonString = TEMP_JSON_TIMETABLE;
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
