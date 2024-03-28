package github.heinrichbarth.meccgevents.data;

import static github.heinrichbarth.meccgevents.data.JsonUtils.requireLong;
import static github.heinrichbarth.meccgevents.data.JsonUtils.requireString;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class TrackRecord
{
    private long id = System.currentTimeMillis();

    @NotNull
    private String opponentName = "";

    @NotNull
    private String opponentTournamentPoints = "";
    @NotNull
    private String opponentPoints = "";
    @NotNull
    private String tournamentPoints = "";

    @NotNull
    private String points = "";

    @NotNull
    private String notes = "";

    @NotNull
    public long getId()
    {
        return id;
    }

    public long getTime()
    {
        return id;
    }

    @NotNull
    public String getOpponentName()
    {
        return opponentName;
    }

    @NotNull
    public String getOpponentTournamentPoints()
    {
        return opponentTournamentPoints;
    }

    @NotNull
    public String getPoints()
    {
        return points;
    }

    @NotNull
    public String getTournamentPoints()
    {
        return tournamentPoints;
    }

    @NotNull
    public String getNotes()
    {
        return notes;
    }

    public void setOpponentName(String value)
    {
        opponentName = value == null ? "" : value.trim();
    }

    public void setOpponentTournamentPoints(String value)
    {
        opponentTournamentPoints = value == null ? "" : value.trim();
    }

    public void setOpponentPoints(String value)
    {
        opponentPoints = value == null ? "" : value.trim();
    }

    public void setPoints(String value)
    {
        points = value == null ? "" : value.trim();
    }

    public void setTournamentPoints(String value)
    {
        tournamentPoints = value == null ? "" : value.trim();
    }

    public void setNotes(String value)
    {
        notes = value == null ? "" : value.trim();
    }

    static TrackRecord fromJson(@Nullable JSONObject pJson)
    {
        if (pJson == null) {
            Log.w("TrackRecord", "Invalid record provided");
            return null;
        }

        final TrackRecord pItem = new TrackRecord();

        pItem.opponentName = requireString(pJson, "opponentName");
        pItem.opponentTournamentPoints = requireString(pJson, "opponentTournamentPoints");
        pItem.tournamentPoints = requireString(pJson, "tournamentPoints");

        pItem.points = requireString(pJson, "points");
        pItem.opponentPoints = requireString(pJson, "opponentPoints");

        pItem.notes = requireString(pJson, "notes");
        pItem.id = requireLong(pJson, "id");

        Log.w("TrackRecord", "record id = " + pItem.id);
        return pItem.id > 1 ? pItem : null;
    }

    @NotNull
    JSONObject toJson()
    {
        try {
            final JSONObject json = new JSONObject();
            json.put("opponentName", this.opponentName);
            json.put("opponentPoints", this.opponentPoints);
            json.put("opponentTournamentPoints", this.opponentTournamentPoints);
            json.put("tournamentPoints",this.tournamentPoints);
            json.put("points", this.points);
            json.put("notes", this.notes);
            json.put("id", this.id);
            return json;
        }
        catch (JSONException exIgnore)
        {
            /* ignore */
        }

        return new JSONObject();
    }
}
