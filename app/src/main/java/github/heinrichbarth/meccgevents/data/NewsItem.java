package github.heinrichbarth.meccgevents.data;

import static github.heinrichbarth.meccgevents.data.JsonUtils.requireLong;
import static github.heinrichbarth.meccgevents.data.JsonUtils.requireString;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class NewsItem extends TextblocksElement implements Comparable<NewsItem> {
    private String title = "";
    private String date = "";

    private int dateTime = 0;

    private List<HtmlTextElement> text = Collections.emptyList();
    private String id = "";

    private NewsItem()
    {
        super();
    }

    @Override
    public int compareTo(NewsItem d) {
        return d.dateTime - this.dateTime;
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

    @Override
    public @NotNull String getSubheadline() {
        return date;
    }

    @Override
    @NonNull
    public List<HtmlTextElement> getText()
    {
        return Collections.unmodifiableList(text);
    }

    @Override
    public boolean hasText() {
        return !this.text.isEmpty();
    }

    @Nullable
    static NewsItem fromJson(@Nullable JSONObject pJson)
    {
        if (pJson == null)
            return null;

        final NewsItem pItem = new NewsItem();

        pItem.id = "" + requireLong(pJson, "id");
        pItem.title = requireString(pJson, "title");
        pItem.date = getDate(pJson);
        pItem.dateTime = toTime(pItem.date);
        pItem.text = getTexts(pJson, "texts");

        return pItem.isEmpty() ? null : pItem;
    }

    @NotNull
    public String getDate()
    {
        return date;
    }

    private boolean isEmpty()
    {
        return title.isEmpty();
    }

    @Override
    public boolean hasExpired()
    {
        return hasExpired(dateTime);
    }

}
