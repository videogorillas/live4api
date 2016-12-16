package com.vg.live.model;

import static com.vg.live.js.Utils.defaultString;
import static com.vg.live.js.Utils.isNotBlank;
import static org.stjs.javascript.JSCollections.$array;

public class Address {
    public String line1;
    public String line2;
    public String city;
    public String state;
    public String zip;
    public String country;

    public String asOneLine() {
        String state_zip = defaultString(state, "") + " " + defaultString(zip, "");
        return $array(line1, line2, city, state_zip, country)
                .filter((s, aLong, strings) -> isNotBlank(s))
                .join(",");
    }
}
