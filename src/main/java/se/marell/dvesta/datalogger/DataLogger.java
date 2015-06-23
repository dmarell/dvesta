/*
 * Created daniel 2008-sep-13 21:27:44
 */
package se.marell.dvesta.datalogger;

public interface DataLogger {
    DataItemDescriptor registerDoubleDataItem(String name, String description, int decimalPlaces);

    DataItemDescriptor registerIntegerDataItem(String name, String description);

    DataItemDescriptor registerBooleanDataItem(String name, String description);

    void logDoubleValue(DataItemDescriptor descriptor, double value);

    void logIntegerValue(DataItemDescriptor descriptor, int value);

    void logBooleanValue(DataItemDescriptor descriptor, boolean value);
}
