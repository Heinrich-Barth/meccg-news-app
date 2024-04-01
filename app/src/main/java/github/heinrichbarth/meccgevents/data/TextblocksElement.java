package github.heinrichbarth.meccgevents.data;

import static github.heinrichbarth.meccgevents.data.JsonUtils.requireString;

import android.annotation.SuppressLint;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

abstract class TextblocksElement implements CardDataItem
{
    @NotNull
    public abstract List<HtmlTextElement> getText();

    public abstract boolean hasText();

    private static int NOW = -1;

    @SuppressLint("SimpleDateFormat")
    TextblocksElement()
    {
        if (NOW == -1)
            NOW = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));
    }

    protected static String getDate(@NotNull JSONObject pJson)
    {
        final String val = requireString(pJson, "date");
        if (!val.isEmpty())
            return val;

        final String createdAt = requireString(pJson, "createdAt");
        if (!createdAt.contains("T"))
            return "";

        final String[] parts = createdAt.split("T");
        return parts.length == 0 ? "" : parts[0];
    }
    protected boolean hasExpired(int nTime)
    {
        return nTime > 0 && nTime < NOW;
    }

    @NotNull
    protected static List<HtmlTextElement> getTexts(@NotNull JSONObject pJson, @NotNull String sField)
    {
        if (!pJson.has(sField))
            return Collections.emptyList();

        try
        {
            final JSONArray pField = pJson.getJSONArray(sField);
            final int size = pField.length();
            if (size == 0)
                return Collections.emptyList();

            final List<HtmlTextElement> vpList = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
            {
                final JSONObject pElem = pField.getJSONObject(i);
                final String title = requireString(pElem, "title").trim();
                final String text = requireString(pElem, "text").trim();

                if (!title.isEmpty() || !text.isEmpty())
                    vpList.add(new HtmlTextElementImpl(title, text));
            }

            return vpList;
        }
        catch (JSONException eIgnore)
        {
            /* ignore */
        }

        return Collections.emptyList();
    }


    protected static int toTime(@NotNull String sInput)
    {
        if (sInput.isEmpty())
            return 0;

        final String[] parts = sInput.split("T");
        if (parts.length == 0)
            return 0;
        else
            return toInt(parts[0].replaceAll("-", ""));
    }

    private static int toInt(@NotNull String sVal)
    {
        try
        {
            if (!sVal.isEmpty())
                return Integer.parseInt(sVal);
        }
        catch (NumberFormatException exIgnore)
        {
            /* ignore */
        }

        return 0;
    }
    private static class HtmlTextElementImpl implements HtmlTextElement {

        @NotNull
        private final String title;

        @NotNull
        private final String text;

        private HtmlTextElementImpl(@NotNull String title, @NotNull String text)
        {
            this.title = title;
            this.text = text;
        }
        @Override
        public @NotNull String getTitle() {
            return title;
        }

        @Override
        public @NotNull String getText() {
            return text;
        }
    }
}
