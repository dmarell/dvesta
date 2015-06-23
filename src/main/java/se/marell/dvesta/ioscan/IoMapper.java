/*
 * Created by daniel Jan 22, 2010 8:59:14 AM
 */
package se.marell.dvesta.ioscan;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface IoMapper {
    @NotNull
    Collection<IoDevice> getIoDevices();

    @NotNull
    Collection<BitInput> getBitInputs();

    @Nullable
    BitInput findBitInput(@NotNull String name);

    @NotNull
    Collection<BitOutput> getBitOutputs();

    @Nullable
    BitOutput findBitOutput(@NotNull String name);

    @NotNull
    Collection<FloatInput> getFloatInputs();

    @Nullable
    FloatInput findFloatInput(@NotNull String name);

    @NotNull
    Collection<FloatOutput> getFloatOutputs();

    @Nullable
    FloatOutput findFloatOutput(@NotNull String name);

    @NotNull
    Collection<IntegerInput> getIntegerInputs();

    @Nullable
    IntegerInput findIntegerInput(@NotNull String name);

    @NotNull
    Collection<IntegerOutput> getIntegerOutputs();

    @Nullable
    IntegerOutput findIntegerOutput(@NotNull String name);

    @NotNull
    BitInput getBitInput(@NotNull String name, boolean unmappedState);

    @NotNull
    BitOutput getBitOutput(@NotNull String name, boolean unmappedState);

    @NotNull
    FloatInput getFloatInput(@NotNull String name, @NotNull String unit, float unmappedValue, int numDecimals, float min, float max) throws IoMappingException;

    @NotNull
    FloatInput getFloatInput(@NotNull String name, @NotNull String unit, float unmappedValue, int numDecimals) throws IoMappingException;

    @NotNull
    FloatOutput getFloatOutput(@NotNull String name, @NotNull String unit, float unmappedValue, int numDecimals, float min, float max) throws IoMappingException;

    @NotNull
    IntegerInput getIntegerInput(@NotNull String name, @NotNull String unit, int unmappedValue, int min, int max) throws IoMappingException;

    @NotNull
    IntegerOutput getIntegerOutput(@NotNull String name, @NotNull String unit, int unmappedValue, int min, int max) throws IoMappingException;

    @NotNull
    Collection<AlarmInput> getAlarmInputs();

    @NotNull
    AlarmInput getAlarmInput(@NotNull String name) throws IoMappingException;

    @Nullable
    AlarmInput findAlarmInput(@NotNull String name);

    void mapBitInput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription);

    void mapBitOutput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription);

    void mapFloatInput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription, @NotNull String unit, int numDecimals, float min, float max) throws IoMappingException;

    void mapFloatOutput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription, @NotNull String unit, int numDecimals, float min, float max) throws IoMappingException;

    void mapIntegerInput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription, @NotNull String unit, int min, int max) throws IoMappingException;

    void mapIntegerOutput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription, @NotNull String unit, int min, int max) throws IoMappingException;

    void mapAlarmInput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription) throws IoMappingException;
}
