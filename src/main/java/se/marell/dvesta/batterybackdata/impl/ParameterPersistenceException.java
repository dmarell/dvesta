/**
 * Created by Daniel Marell 2004-jan-15 22:05:15
 */
package se.marell.dvesta.batterybackdata.impl;

/**
 * This exception can be thrown by file readers/writers to indicate that read/write
 * failed due to an exceptional condition.  If the condition was, for example,
 * an IOException, the original exception can be included in the ParameterPersistenceException,
 * but the calling code does not have to catch all possible internal exception types,
 * only ParameterPersistenceException.
 */
public class ParameterPersistenceException extends RuntimeException {
  private Throwable throwable;

  public ParameterPersistenceException(String msg) {
    super(msg);
  }

  public ParameterPersistenceException(String msg, Throwable throwable) {
    super(msg);
    this.throwable = throwable;
  }

  public Throwable getWrappedException() {
    return throwable;
  }
}
