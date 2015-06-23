/*
 * Created by Daniel Marell Feb 10, 2010 9:36:30 PM
 */
package se.marell.dvesta.ioscan.impl;

import org.jetbrains.annotations.NotNull;
import se.marell.dvesta.ioscan.BitOutput;
import se.marell.dvesta.ioscan.IoType;

public class IoScanBitOutput extends IoScanBitInput implements BitOutput {
  private static final long serialVersionUID = 1;
  private boolean isTouched;

  public IoScanBitOutput(@NotNull String name, @NotNull String unit, boolean state) {
    super(IoType.DIGITAL_OUTPUT, name, unit, state);
  }

  @Override
  public void setOutputStatus(boolean status) {
    this.inputStatus = status;
    isTouched = true;
    setTimestamp(System.currentTimeMillis());
    addSample();
  }

  @Override
  public boolean isTouched() {
    return isTouched;
  }

  @Override
  public void clearTouched() {
    isTouched = false;
  }
}
