package github.heinrichbarth.meccgevents.data;

import org.jetbrains.annotations.NotNull;


public interface CardDataItem extends DataItem
{
    @NotNull
    public String getTitle();

    @NotNull
    public String getSubheadline();

    public boolean hasExpired();

}
