package github.heinrichbarth.meccgevents.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class SharedPrefsData {
    private static final String TAG = "SharedPrefsData";

    private static final String SHARED_FILE = "github.heinrichbarth.meccgevents";
    private static final String KEY_DATA_REFRESH = "data_refresh";
    private static final String KEY_TERMS = "terms";

    private final Context context;

    SharedPrefsData(@Nullable Context applicationContext) {
        this.context = applicationContext;
    }

    protected long getTimeLastDataRefresh()
    {
        return getSharedPrefLong(context, KEY_DATA_REFRESH, 0L);
    }

    protected void updateTimeLastDataRefresh()
    {
        setSharedPrefLong(context, KEY_DATA_REFRESH, System.currentTimeMillis());
    }

    public boolean hasAgreedToTerms()
    {
        return getSharedPrefBool(context, KEY_TERMS, false);
    }

    public void agreeToTerms()
    {
        Log.i(TAG, "User agreed to terms and conditions");
        setSharedPrefBool(context, KEY_TERMS, true);
    }

    protected void saveCache(String sFile, String jsonData)
    {
        if (this.context == null || sFile == null || jsonData == null || sFile.isEmpty() || jsonData.isEmpty()) {
            Log.w(TAG, "No context or data provided");
            return;
        }

        try (FileOutputStream fos = context.openFileOutput(sFile, Context.MODE_PRIVATE)) {
            fos.write(jsonData.getBytes(StandardCharsets.UTF_8));
            Log.i(TAG, "Saving content to cache file " + sFile);
        } catch (IOException | SecurityException ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    @NotNull
    protected JSONObject loadJsonFromCache(@NotNull String sFile)
    {
        if (this.context == null || sFile.isEmpty())
            return new JSONObject();

        try (InputStream in = context.openFileInput(sFile)) {
            byte[] bytes = readStream(in);
            if (bytes.length > 0)
                return new JSONObject(new String(bytes, StandardCharsets.UTF_8));
        } catch (IOException | SecurityException | JSONException ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }

        return new JSONObject();
    }

    private long getSharedPrefLong(@Nullable Context context, @NotNull String sField, long lDefault)
    {
        final SharedPreferences prefs = getSharedPreferences(context);
        if (prefs == null)
        {
            Log.e(TAG, "Cannot get shared prefs");
            return lDefault;
        }
        else
            return prefs.getLong(sField, lDefault);
    }

    @Nullable
    private SharedPreferences getSharedPreferences(@Nullable Context context)
    {
        if (context == null) {
            Log.w(TAG, "Context not available. Cannot access shared preferences");
            return null;
        }
        else
            return context.getSharedPreferences(SHARED_FILE, Context.MODE_PRIVATE);
    }

    private boolean getSharedPrefBool(@Nullable Context context, @NotNull String sField, boolean bDefault)
    {
        final SharedPreferences prefs = getSharedPreferences(context);
        if (prefs == null)
        {
            Log.e(TAG, "Cannot get shared prefs");
            return bDefault;
        }
        else
            return prefs.getBoolean(sField, bDefault);
    }

    private void setSharedPrefLong(@Nullable Context context, @NotNull String sField, long lValue)
    {
        final SharedPreferences.Editor editor = getSharedPrefEditor(context);
        if (editor != null) {
            editor.putLong(sField, lValue);
            editor.apply();
        }
    }

    @Nullable
    private SharedPreferences.Editor getSharedPrefEditor(@Nullable Context context)
    {
        final SharedPreferences prefs = getSharedPreferences(context);
        if (prefs == null)
        {
            Log.e(TAG, "Cannot get shared prefs");
            return null;
        }
        else
            return prefs.edit();
    }

    private void setSharedPrefBool(@Nullable Context context, @NotNull String sField, boolean bValue)
    {
        final SharedPreferences.Editor editor = getSharedPrefEditor(context);
        if (editor != null) {
            editor.putBoolean(sField, bValue);
            editor.apply();
        }
    }

    @NotNull
    protected byte[] readStream(@Nullable InputStream in) throws IOException {
        if (in == null)
            return new byte[0];

        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        final byte[] data = new byte[1024];
        while ((nRead = in.read(data, 0, data.length)) != -1)
            buffer.write(data, 0, nRead);

        buffer.flush();
        return buffer.toByteArray();
    }

}
