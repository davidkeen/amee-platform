package com.amee.platform.science;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Represents a single-valued amount data point at a single instance of time.
 */
public class DataPoint implements Comparable<DataPoint> {

    private static final DateTime EPOCH = new DateTime(0);

    /**
     * Represents the concept of a zero-valued DataPoint occurring at the EPOCH (<code>DateTime(0)</code>)
     */
    public static final DataPoint NULL = new DataPoint(EPOCH, Amount.ZERO);

    private DateTime dateTime;
    private Amount amount;

    /**
     * Construct a DataPoint with a amount value occurring at the epoch.
     *
     * @param decimal - the amount value
     */
    public DataPoint(float decimal) {
        this(new Amount(decimal));
    }

    /**
     * Construct a DataPoint with a amount value occurring at the epoch.
     *
     * @param amount - the amount value
     */
    public DataPoint(Amount amount) {
        this(EPOCH, amount);
    }

    /**
     * Construct a DataPoint with a amount value occurring at a specific point in time.
     *
     * @param amount  - the amount value
     * @param dateTime - the point in time at which this data point occurs.
     */
    public DataPoint(DateTime dateTime, Amount amount) {
        this.dateTime = dateTime;
        this.amount = amount;
    }

    /**
     * A copy constructor.
     *
     * @param dataPoint to copy
     */
    protected DataPoint(DataPoint dataPoint) {
        dateTime = dataPoint.getDateTime();
        amount = dataPoint.getValue();
    }

    /**
     * Return a copy of this object.
     *
     * @return a copy
     */
    public DataPoint copy() {
        return new DataPoint(this);
    }

    public String toString() {
        try {
            return getJSONArray().toString();
        } catch (JSONException e) {
            throw new RuntimeException("Caught JSONException: " + e.getMessage(), e);
        }
    }

    public JSONArray getJSONArray() throws JSONException {
        JSONArray arr = new JSONArray();
        arr.put((dateTime == null) ? "NULL" : dateTime.toString());
        arr.put((amount == null) ? "NaN" : amount.toString());
        return arr;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public Amount getValue() {
        return amount;
    }

    /**
     * Add a DataPoint to this DataPoint.
     *
     * @param point - the DataPoint to add
     * @return a new DataPoint representing the addition of the two DataPoint values
     */
    public DataPoint plus(DataPoint point) {
        return new DataPoint(dateTime, amount.add(point.getValue()));
    }

    /**
     * Add a float value to this DataPoint.
     *
     * @param f - the float to add
     * @return a new DataPoint representing the addition of the DataPoint and float values
     */
    public DataPoint plus(float f) {
        return new DataPoint(dateTime, amount.add(new Amount(f)));
    }

    /**
     * Subtract a DataPoint from this DataPoint.
     *
     * @param point - the DataPoint to subtract
     * @return a new DataPoint representing the subtraction of the DataPoint from this DataPoint
     */
    public DataPoint subtract(DataPoint point) {
        return new DataPoint(dateTime, amount.subtract(point.getValue()));
    }

    /**
     * Subtract a float value from this DataPoint.
     *
     * @param f - the float to subtract
     * @return a new DataPoint representing the subtraction of the float value from this DataPoint
     */
    public DataPoint subtract(float f) {
        return new DataPoint(dateTime, amount.subtract(new Amount(f)));
    }

    /**
     * Divide this DataPoint by another DataPoint.
     *
     * @param point - the DataPoint by which to divide this DataPoint
     * @return a new DataPoint representing the division of this DataPoint by the DataPoint
     */
    public DataPoint divide(DataPoint point) {
        return new DataPoint(dateTime, amount.divide(point.getValue()));
    }

    /**
     * Divide this DataPoint by a float value.
     *
     * @param f - the float value by which to divide this DataPoint
     * @return a new DataPoint representing the division of this DataPoint by the float value
     */
    public DataPoint divide(float f) {
        return new DataPoint(dateTime, amount.divide(new Amount(f)));
    }

    /**
     * Multiply this DataPoint by another DataPoint.
     *
     * @param point - the DataPoint to multiply this DataPoint
     * @return a new DataPoint representing the multiplication of the two DataPoints
     */
    public DataPoint multiply(DataPoint point) {
        return new DataPoint(dateTime, amount.multiply(point.getValue()));
    }

    /**
     * Multiply this DataPoint by a float value.
     *
     * @param f - the float value to multiply this DataPoint
     * @return a new DataPoint representing the multiplication of this DataPoint by the float value.
     */
    public DataPoint multiply(float f) {
        return new DataPoint(dateTime, amount.multiply(new Amount(f)));
    }

    public int compareTo(DataPoint that) {
        // TODO: Collections of DataPoint values will be ordered by what?
        return getDateTime().compareTo(that.getDateTime());
    }
}