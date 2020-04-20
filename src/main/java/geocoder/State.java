package main.java.geocoder;

import java.util.HashMap;
import java.util.Map;

public enum State {

    ALABAMA("Alabama", "AL", 1), ALASKA("Alaska", "AK", 2), AMERICAN_SAMOA("American Samoa", "AS", 60),
    ARIZONA("Arizona", "AZ", 4), ARKANSAS("Arkansas", "AR", 5), CALIFORNIA("California", "CA", 6),
    COLORADO("Colorado", "CO", 8), CONNECTICUT("Connecticut", "CT", 9), DELAWARE("Delaware", "DE", 10),
    DISTRICT_OF_COLUMBIA("District of Columbia", "DC", 11), FEDERATED_STATES_OF_MICRONESIA("Federated States of Micronesia", "FM", 64),
    FLORIDA("Florida", "FL", 12), GEORGIA("Georgia", "GA", 13), GUAM("Guam", "GU", 66), HAWAII("Hawaii", "HI", 15),
    IDAHO("Idaho", "ID", 16), ILLINOIS("Illinois", "IL", 17), INDIANA("Indiana", "IN", 18), IOWA("Iowa", "IA", 19),
    KANSAS("Kansas", "KS", 20), KENTUCKY("Kentucky", "KY", 21), LOUISIANA("Louisiana", "LA", 22),
    MAINE("Maine", "ME", 23), MARYLAND("Maryland", "MD", 24), MARSHALL_ISLANDS("Marshall Islands", "MH", 68),
    MASSACHUSETTS("Massachusetts", "MA", 25), MICHIGAN("Michigan", "MI", 26), MINNESOTA("Minnesota", "MN", 27),
    MISSISSIPPI("Mississippi", "MS", 28), MISSOURI("Missouri", "MO", 29), MONTANA("Montana", "MT", 30),
    NEBRASKA("Nebraska", "NE", 31), NEVADA("Nevada", "NV", 32), NEW_HAMPSHIRE("New Hampshire", "NH", 33),
    NEW_JERSEY("New Jersey", "NJ", 34), NEW_MEXICO("New Mexico", "NM", 35), NEW_YORK("New York", "NY", 36),
    NORTH_CAROLINA("North Carolina", "NC", 37), NORTH_DAKOTA("North Dakota", "ND", 38),
    NORTHERN_MARIANA_ISLANDS("Northern Mariana Islands", "MP", 69), OHIO("Ohio", "OH", 39),
    OKLAHOMA("Oklahoma", "OK", 40), OREGON("Oregon", "OR", 41), PALAU("Palau", "PW", 70),
    PENNSYLVANIA("Pennsylvania", "PA", 42), PUERTO_RICO("Puerto Rico", "PR", 72),
    RHODE_ISLAND("Rhode Island", "RI", 44), SOUTH_CAROLINA("South Carolina", "SC", 45),
    SOUTH_DAKOTA("South Dakota", "SD", 46), TENNESSEE("Tennessee", "TN", 47), TEXAS("Texas", "TX", 48),
    UTAH("Utah", "UT", 49), VERMONT("Vermont", "VT", 50), VIRGIN_ISLANDS("Virgin Islands", "VI", 78),
    VIRGINIA("Virginia", "VA", 51), WASHINGTON("Washington", "WA", 53), WEST_VIRGINIA("West Virginia", "WV", 54),
    WISCONSIN("Wisconsin", "WI", 55), WYOMING("Wyoming", "WY", 56),
    UNKNOWN("Unknown", "", -1);

    /**
     * The state's name.
     */
    private final String name;

    /**
     * The state's abbreviation.
     */
    private final String abbreviation;

    /**
     * The state's numerical code in ANSI
     */
    private final int numCode;

    /**
     * The set of states addressed by abbreviations.
     */
    private static final Map<String, State> STATES_BY_ABBR = new HashMap<String, State>();
    private static final Map<Integer, State> STATES_BY_CODE = new HashMap<Integer, State>();

    /* static initializer */
    static {
        for (State state : values()) {
            STATES_BY_ABBR.put(state.getAbbreviation(), state);
            STATES_BY_CODE.put(state.getNumericalCode(), state);
        }
    }

    /**
     * Constructs a new state.
     *
     * @param name the state's name.
     * @param abbreviation the state's abbreviation.
     * @param code the state's code.
     */
    State(String name, String abbreviation, int code) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.numCode = code;
    }

    /**
     * Returns the state's abbreviation.
     *
     * @return the state's abbreviation.
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * @return the state's code.
     */
    public int getNumericalCode() {
        return numCode;
    }

    /**
     * Gets the enum constant with the specified abbreviation.
     *
     * @param abbr the state's abbreviation.
     * @return the enum constant with the specified abbreviation.
     */
    public static State valueOfAbbreviation(final String abbr) {
        final State state = STATES_BY_ABBR.get(abbr);
        if (state != null) {
            return state;
        } else {
            return UNKNOWN;
        }
    }
    
    public static State valueOfName(final String name) {
        final String enumName = name.toUpperCase().replaceAll(" ", "_");
        try {
            return valueOf(enumName);
        } catch (final IllegalArgumentException e) {
            return State.UNKNOWN;
        }
    }

    public static State valueOfCode(final int code) {
        final State state = STATES_BY_CODE.get(code);
        if (state != null) {
            return state;
        } else {
            return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
