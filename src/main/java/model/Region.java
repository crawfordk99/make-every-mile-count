package model;

/**
 * Enum of U.S. regions and their EIA DuoArea codes.
 * Supports states, cities, PADD regions, and national average.
 */
public enum Region
{
    // National
    US_NATIONAL("NUS", "U.S."),

    // PADD Regions
    PADD_1("R10", "PADD 1 (East Coast)"),
    PADD_1A("R1X", "PADD 1A (New England)"),
    PADD_2("R20", "PADD 2 (Midwest)"),
    PADD_3("R30", "PADD 3 (Gulf Coast)"),
    PADD_4("R40", "PADD 4 (Rocky Mountain)"),
    PADD_5("R50", "PADD 5 (West Coast)"),

    // All States supported by EIA
    NEW_YORK("SNY", "New York"),
    TEXAS("STX", "Texas"),
    CALIFORNIA("SCA", "California"),
    FLORIDA("SFL", "Florida"),
    COLORADO("SCO", "Colorado"),
    WASHINGTON("SWA", "Washington"),
    OHIO("SOH", "Ohio"),
    MINNESOTA("SMN", "Minnesota"),

    // All EIA Metro Areas
    HOUSTON("Y44HO", "Houston, TX"),
    LOS_ANGELES("Y05LA", "Los Angeles, CA"),
    NEW_YORK_METRO("Y35NY", "New York Metro"),
    CHICAGO("YORD", "Chicago, IL"),
    DENVER("YDEN", "Denver, CO"),
    BOSTON("YBOS", "Boston, MA"),
    CLEVELAND("YCLE", "Cleveland, OH");

    private final String duoAreaCode;
    private final String displayName;

    Region(String duoAreaCode, String displayName)
    {
        this.duoAreaCode = duoAreaCode;
        this.displayName = displayName;
    }

    public String getDuoAreaCode()
    {
        return duoAreaCode;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Lookup Region by duoAreaCode.
     */
    public static Region fromCode(String code)
    {
        for (Region region : Region.values())
        {
            if (region.duoAreaCode.equalsIgnoreCase(code))
            {
                return region;
            }
        }
        return US_NATIONAL; // fallback to national
    }
}
