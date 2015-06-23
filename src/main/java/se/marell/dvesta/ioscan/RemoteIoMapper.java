/*
 * Created by Daniel Marell 2012-01-25 18:08
 */
package se.marell.dvesta.ioscan;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface RemoteIoMapper extends Remote {
    @NotNull
    Collection<IoDevice> getIoDevices() throws RemoteException;

    @NotNull
    Collection<BitInput> getBitInputs() throws RemoteException;

    @NotNull
    Collection<BitOutput> getBitOutputs() throws RemoteException;

    @NotNull
    Collection<FloatInput> getFloatInputs() throws RemoteException;

    @NotNull
    Collection<FloatOutput> getFloatOutputs() throws RemoteException;

    @NotNull
    Collection<IntegerInput> getIntegerInputs() throws RemoteException;

    @NotNull
    Collection<IntegerOutput> getIntegerOutputs() throws RemoteException;

    void mapBitInput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription) throws RemoteException;

    void mapBitOutput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription);

    void mapFloatInput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription, @NotNull String unit, int numDecimals, float min, float max) throws IoMappingException;

    void mapFloatOutput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription, @NotNull String unit, int numDecimals, float min, float max) throws IoMappingException;

    void mapIntegerInput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription, @NotNull String unit, int min, int max) throws IoMappingException;

    void mapIntegerOutput(@NotNull String name, @NotNull String deviceAddress, @NotNull String addressDescription, @NotNull String unit, int min, int max) throws IoMappingException;
}
