import groovyx.net.http.HttpResponseException
import org.junit.Test
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static org.junit.Assert.*

/**
 * Tests for the Unit API.
 *
 */
class UnitIT extends BaseApiTest {

    def unitUids = [
            '1BB3DAA7A390',
            '2BB3DAA7A390'];

    def unitNames = [
            'Test Unit One',
            'Test Unit Two'];

    /**
     * Tests for creation, fetch and deletion of a Unit using JSON responses.
     *
     * Create a new Unit by POSTing to '/units/types/{UID|name}/units'
     *
     * Supported POST parameters are:
     *
     * <ul>
     * <li>name
     * <li>internalSymbol
     * <li>externalSymbol
     * </ul>
     *
     * NOTE: For detailed rules on these parameters see the validation tests below.
     *
     * Delete (TRASH) a Unit by sending a DELETE request to '/units/types/{UID|name}/units/{UID/symbol}'.
     *
     */
    @Test
    void createAndRemoveUnitJson() {
        versions.each { version -> createAndRemoveUnitJson(version) }
    }

    def createAndRemoveUnitJson(version) {
        if (version >= 3.5) {

            setAdminUser();

            def name = 'Unit To Be Deleted';
            def internalSymbol = 'ee';
            def externalSymbol = 'ff';

            // Create a new Unit.
            def responsePost = client.post(
                    path: "/${version}/units/types/1AA3DAA7A390/units",
                    body: [
                            name: name,
                            internalSymbol: internalSymbol,
                            externalSymbol: externalSymbol],
                    requestContentType: URLENC,
                    contentType: JSON);
            assertEquals 201, responsePost.status;

            // TODO: Fetch the Unit.

            // Then delete the Unit.
            def responseDelete = client.delete(path: "/${version}/units/types/1AA3DAA7A390/units/${internalSymbol}");
            assertEquals 200, responseDelete.status;

            // We should get a 404 here.
            try {
                client.get(path: "/${version}/units/types/1AA3DAA7A390/units/${internalSymbol}");
                fail 'Should have thrown an exception';
            } catch (HttpResponseException e) {
                assertEquals 404, e.response.status;
            }
        }
    }

    /**
     * Tests fetching a list of Units for a Unit Type using JSON.
     *
     * Units GET requests support the following matrix parameters to modify the response.
     *
     * <ul>
     * <li>full - include all values.
     * <li>audit - include the status, created and modified values.
     * </ul>
     *
     * Units are sorted by symbol.
     */
    @Test
    void getAllUnitsForUnitTypeJson() {
        versions.each { version -> getAllUnitsForUnitTypeJson(version) }
    }

    def getAllUnitsForUnitTypeJson(version) {
        if (version >= 3.5) {
            def response = client.get(
                    path: "/${version}/units/types/AAA3DAA7A390/units",
                    contentType: JSON);
            assertEquals 200, response.status;
            assertEquals 'application/json', response.contentType;
            assertTrue response.data instanceof net.sf.json.JSON;
            assertEquals 'OK', response.data.status;
            assertEquals unitUids.size(), response.data.units.size();
            assertEquals unitUids.sort(), response.data.units.collect {it.uid}.sort();
            assertEquals unitNames.sort { a, b -> a.compareToIgnoreCase(b) }, response.data.units.collect {it.name};
        }
    }

    /**
     * Tests the validation rules for the Unit name field.
     *
     * The rules are as follows:
     *
     * <ul>
     * <li>Mandatory.
     * <li>Unique on lower case of entire string amongst all Units.
     * <li>No longer than 255 characters.
     * </ul>
     */
    @Test
    void updateWithInvalidName() {
        setAdminUser();
        updateUnitFieldJson('name', 'empty', '');
        updateUnitFieldJson('name', 'long', String.randomString(256));
        updateUnitFieldJson('name', 'duplicate', 'Test Unit Two'); // Normal case.
        updateUnitFieldJson('name', 'duplicate', 'test unit two'); // Lower case.
    }

    /**
     * Submits a single Unit field value and tests the result. An error is expected.
     *
     * @param field that is being updated
     * @param code expected upon error
     * @param value to submit
     */
    def updateUnitFieldJson(field, code, value) {
        updateInvalidFieldJson("/units/types/AAA3DAA7A390/units/1BB3DAA7A390", field, code, value, 3.5)
    }
}