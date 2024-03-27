package github.heinrichbarth.meccgevents.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.heinrichbarth.meccgevents.BuildConfig;
import github.heinrichbarth.meccgevents.MainActivity;
import github.heinrichbarth.meccgevents.R;

public class DataRepository extends SharedPrefsData
{
    private static final String TAG = "DatRepository";
    private static final String FILE_NEWS = "news.json";
    private static final String FILE_EVENTS = "events.json";

    @NotNull
    private static final List<NewsItem> newsItems = new ArrayList<>();

    @NotNull
    private static final List<EventItem> eventItems = new ArrayList<>();

    private static int countGamesMellon = 0;

    private DataRepository(@Nullable Context applicationContext) {
        super(applicationContext);
    }

    @NotNull
    private static final String URL_EVENTS = BuildConfig.URL_EVENTS_ALL;

    @NotNull
    private static final String URL_GAMES_MELLON = BuildConfig.URL_MELLON_GAMES;

    @NotNull
    private static final String URL_NEWS = BuildConfig.URL_NEWS_ALL;

    @NotNull
    public List<NewsItem> getNews(int nMax)
    {
        return getNews(newsItems, nMax);
    }

    @NotNull
    public List<NewsItem> getNews(List<NewsItem> list, int nMax)
    {
        if (list.isEmpty())
            return Collections.emptyList();

        if (nMax < 1 || nMax > newsItems.size())
            return Collections.unmodifiableList(newsItems);

        return Collections.unmodifiableList(newsItems.subList(0, nMax));
    }


    @NotNull
    public List<EventItem> getEvents(List<EventItem> list, int nMax, boolean onlineEvents)
    {
        if (list.isEmpty())
            return Collections.emptyList();

        if (nMax == 0)
            nMax = list.size();

        int nAdded = 0;
        final List<EventItem> result = new ArrayList<>();
        for (int i = 0; nAdded < nMax && i < list.size(); i++)
        {
            final EventItem e = list.get(i);
            if (e.isOnline() == onlineEvents) {
                nAdded++;
                result.add(e);
            }
        };

        return result;
    }

    @NotNull
    public List<EventItem> getEvents(int nMax, boolean onlineEvents)
    {
        return getEvents(eventItems, nMax, onlineEvents);
    }

    @NotNull
    public static DataRepository get()
    {
        return get(null);
    }

    @NotNull
    public static DataRepository get(@Nullable Context context)
    {
        return new DataRepository(context);
    }

    public static void init(@Nullable Context context)
    {
        final DataRepository INSTANCE = new DataRepository(context);
        final int news = INSTANCE.loadNewsCache();
        final int events = INSTANCE.loadEventsCache();

        Log.i(TAG, "Cached events: " + events);
        Log.i(TAG, "Cached news: " + news);
    }

    public boolean fetchData(boolean forceRefresh)
    {
        final JSONObject data = getHttpJson(URL_GAMES_MELLON);
        countGamesMellon = JsonUtils.requireInteger(data, "games");

        if (!forceRefresh && !allowRefresh())
            return false;

        if (forceRefresh)
            Log.i(TAG, "Force cache update");

        if (loadNewsFromDUrl() && loadEventsFromDUrl())
        {
            updateTimeLastDataRefresh();
            return true;
        }
        else
            return false;
    }

    private static final long MILLIS_CACHED = 1000L * 60 * 15;

    private boolean allowRefresh()
    {
        if (newsItems.isEmpty() && eventItems.isEmpty())
        {
            Log.v(TAG, "No data yet.");
            return true;
        }

        final long lTime = System.currentTimeMillis() - super.getTimeLastDataRefresh();
        if (lTime >= MILLIS_CACHED)
        {
            Log.v(TAG, "Expired. go!");
            return true;
        }

        Log.v(TAG, "Not yet expired. Wait.");
        return false;
    }

    private boolean loadNewsFromDUrl()
    {
        final JSONObject json = getHttpJson(URL_NEWS);
        if (json.length() == 0)
            return false;

        final List<NewsItem> vpNews = loadNewsFromJson(json);
        if (!vpNews.isEmpty())
        {
            newsItems.clear();
            newsItems.addAll(vpNews);
        }

        saveCache(FILE_NEWS, json.toString());
        return true;
    }

    private boolean loadEventsFromDUrl()
    {
        final JSONObject json = getHttpJson(URL_EVENTS);
        if (json.length() == 0)
            return false;

        final List<EventItem> vpNews = loadEventsFromJson(json);
        if (!vpNews.isEmpty())
        {
            eventItems.clear();
            eventItems.addAll(vpNews);
        }

        saveCache(FILE_EVENTS, json.toString());
        return true;
    }


    private int loadNewsCache()
    {
        final JSONObject json = loadJsonFromCache(FILE_NEWS);
        final List<NewsItem> vpNews = loadNewsFromJson(json);
        if (!vpNews.isEmpty())
        {
            newsItems.clear();
            newsItems.addAll(vpNews);
        }

        return vpNews.size();
    }

    private int loadEventsCache()
    {
        final JSONObject json = loadJsonFromCache(FILE_EVENTS);
        final List<EventItem> vpNews = loadEventsFromJson(json);
        if (!vpNews.isEmpty())
        {
            eventItems.clear();
            eventItems.addAll(vpNews);
        }

        return vpNews.size();
    }

    private List<NewsItem> loadNewsFromJson(@NotNull JSONObject pJson)
    {
        @NotNull List<JSONObject> vpCandidates = normalizeJsonResult(JsonUtils.requireArray(pJson, "data"));
        if (vpCandidates.isEmpty()) {
            Log.w(TAG, "No news available");
            return Collections.emptyList();
        }

        final List<NewsItem> vpList = new ArrayList<>(vpCandidates.size());
        vpCandidates.forEach(_json ->
        {
            final NewsItem item = NewsItem.fromJson(_json);
            if (item != null)
                vpList.add(item);
        });

        Log.i(TAG, vpList.size() + " news available");

        Collections.sort(vpList);
        return vpList;
    }

    @NotNull
    private List<JSONObject> normalizeJsonResult(@NotNull JSONArray pArray)
    {
        final int len = pArray.length();
        if (len == 0)
            return Collections.emptyList();

        final List<JSONObject> vpResult = new ArrayList<>(len);
        for (int i = 0; i < len; i++)
        {
            try {
                final JSONObject data = pArray.getJSONObject(i);
                final JSONObject candidate = data.getJSONObject("attributes");
                candidate.put("id", "" + data.getInt("id"));
                vpResult.add(candidate);
            }
            catch (JSONException exIgnore)
            {
                /* ignore */
            }
        }
        return vpResult;
    }

    private List<EventItem> loadEventsFromJson(@NotNull JSONObject pJson)
    {
        @NotNull List<JSONObject> vpCandidates = normalizeJsonResult(JsonUtils.requireArray(pJson, "data"));
        if (vpCandidates.isEmpty()) {
            Log.w(TAG, "No events found");
            return Collections.emptyList();
        }

        final List<EventItem> vpList = new ArrayList<>(vpCandidates.size());
        vpCandidates.forEach(_json ->
        {
            final EventItem item = EventItem.formJson(_json);
            if (item != null)
                vpList.add(item);
        });

        Log.i(TAG, vpList.size() + " events available");

        Collections.sort(vpList);
        return vpList;
    }

    @Nullable
    public NewsItem getNewsById(String id) {
        if (id == null || id.isEmpty() || newsItems.isEmpty())
            return null;

        for (NewsItem item : newsItems)
        {
            if (id.equals(item.getId()))
                return item;
        }

        return null;
    }

    @Nullable
    public EventItem getEventById(String id)
    {
        if (id == null || id.isEmpty() || eventItems.isEmpty())
            return null;

        for (EventItem item : eventItems)
        {
            if (id.equals(item.getId()))
                return item;
        }

        return null;
    }


    public JSONObject getHttpJson(@NotNull String sUrl)  {

        HttpURLConnection urlConnection = null;

        try {
            Log.d(TAG, "Fetch data from " + sUrl);

            final URL url = new URL(sUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1000 * 20);
            urlConnection.setReadTimeout(1000 * 20);
            urlConnection.setRequestMethod("GET");

            final int code = urlConnection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK)
                throw new IllegalStateException("Cannot open connection");

            try(InputStream in = urlConnection.getInputStream() ) {
                return new JSONObject(new String(readStream(in), StandardCharsets.UTF_8));
            }
        }
        catch (IOException | JSONException | RuntimeException ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        Log.i(TAG, "No data available");
        return new JSONObject();
    }

    public int getCurrentGames() {
        return countGamesMellon;
    }
}

