package github.heinrichbarth.meccgevents.data;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JsonUtils
{
    private JsonUtils()
    {
        /* avoid instance */
    }

    @NotNull
    static String convertToDate(int value)
    {
        if (value < 20240000)
            return "";

        final String sValue = "" + value;
        if (sValue.length() != 8)
            return "";

        return sValue.substring(0, 4) + "-" + sValue.substring(4, 6) + "-" + sValue.substring(6, 8);
    }

    static boolean requireBoolean(@NotNull JSONObject json, @NotNull String sField, boolean bDef)
    {
        try
        {
            if (!json.isNull(sField))
                return json.getBoolean(sField);
        }
        catch (JSONException exIgnore) {
            // ignore
        }

        return bDef;
    }

    @NotNull
    static JSONArray requireArray(@NotNull JSONObject json, @NotNull String sField)
    {
        try
        {
            if (!json.isNull(sField))
                return json.getJSONArray(sField);
        }
        catch (JSONException exIgnore) {
            // ignore
        }

        return new JSONArray();

    }

    @NotNull
    static String requireString(@NotNull JSONObject json, @NotNull String sField)
    {
        try
        {
            if (!json.isNull(sField))
                return json.getString(sField);
        }
        catch (JSONException exIgnore) {
            // ignore
        }

        return "";
    }
    static int requireInteger(@NotNull JSONObject json, @NotNull String sField)
    {
        try
        {
            if (!json.isNull(sField))
                return json.getInt(sField);
        }
        catch (JSONException exIgnore) {
            // ignore
        }

        return -1;
    }

    static long requireLong(@NotNull JSONObject json, @NotNull String sField)
    {
        try
        {
            if (!json.isNull(sField))
                return json.getLong(sField);
        }
        catch (JSONException exIgnore) {
            // ignore
        }

        return 0L;
    }
    static int requireInteger(@NotNull JSONObject json, @NotNull String sField, int def)
    {
        try
        {
            return json.getInt(sField);
        }
        catch (JSONException exIgnore) {
            // ignore
        }

        return def;
    }
    @NotNull
    static JSONArray parsseJsonArray(@NotNull String sJson)
    {
        try
        {
            if (!sJson.isEmpty())
                return new JSONArray(sJson);
        }
        catch (JSONException | RuntimeException ex)
        {
            Log.e("News", ex.getMessage(), ex);
        }

        return new JSONArray();
    }
}
