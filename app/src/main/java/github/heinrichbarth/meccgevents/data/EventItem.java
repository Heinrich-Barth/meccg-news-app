package github.heinrichbarth.meccgevents.data;

import static github.heinrichbarth.meccgevents.data.JsonUtils.convertToDate;
import static github.heinrichbarth.meccgevents.data.JsonUtils.requireBoolean;
import static github.heinrichbarth.meccgevents.data.JsonUtils.requireInteger;
import static github.heinrichbarth.meccgevents.data.JsonUtils.requireString;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventItem extends TextblocksElement implements Comparable<EventItem>  {
    private String title = "";
    private String date = "";

    private String dateText = "";
    private int dateTime = 0;
    private String summary = "";
    private List<HtmlTextElement> text = Collections.emptyList();
    private String id;
    private int duration = 1;
    private String geo = "";
    private String url = "";

    private boolean online = false;

    private String venue = "";

    private String address = "";

    public String getSummary()
    {
        return summary;
    }

    private EventItem()
    {
    }
    @Override
    public int compareTo(EventItem d) {
        return this.dateTime - d.dateTime;
    }
    @NotNull
    public String getId()
    {
        return id;
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    public String getDate()
    {
        return date;
    }
    @Override
    public @NotNull String getSubheadline() {
        return dateText;
    }

    @NotNull
    public List<HtmlTextElement> getText()
    {
        return Collections.unmodifiableList(text);
    }

    public boolean hasText()
    {
        return !text.isEmpty();
    }

    public String getGeo()
    {
        return geo;
    }

    public String getVenue()
    {
        return venue;
    }

    public String getAddress()
    {
        return address;
    }

    public String getUrl(){
        return url;
    }

    public boolean isOnline()
    {
        return online;
    }

    @Nullable
    static EventItem formJson(@Nullable JSONObject pJson)
    {
        if (pJson == null)
            return null;

        final EventItem event = new EventItem();
        event.id = requireString(pJson, "id");
        event.title = requireString(pJson, "title");
        event.date = getDate(pJson);
        event.dateText = requireString(pJson, "dateText");
        event.dateTime = toTime(event.date);
        event.duration = requireInteger(pJson, "duration", 1);
        event.geo = requireString(pJson, "geo");
        event.venue = requireString(pJson, "venue");
        event.address = requireString(pJson, "address");
        event.summary = requireString(pJson, "summary");
        event.text = getTexts(pJson, "texts");
        event.url = requireString(pJson, "url");
        event.online = requireBoolean(pJson, "onlineEvent", false);

        return event.isEmpty() ? null : event;
    }

    private boolean isEmpty()
    {
        return id.isEmpty() || title.isEmpty() || date.isEmpty();
    }

    @Override
    public boolean hasExpired()
    {
        return hasExpired(dateTime);
    }
}
