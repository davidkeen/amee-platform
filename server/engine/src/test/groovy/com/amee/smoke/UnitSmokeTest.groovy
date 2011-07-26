package com.amee.smoke

import org.junit.Test
import org.junit.Ignore
import static org.junit.Assert.*


class UnitSmokeTest extends BaseSmokeTest {

    @Ignore("No units data yet.")
    @Test
    void listUnits() {
        def response = client.get(path: "/3/units")
        assertResponseOk response
        assertTrue response.data.units.size() > 1
    }

    //TODO: Complete when units data has been loaded.
}
