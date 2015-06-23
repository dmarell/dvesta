/*
 * Created by Daniel Marell 2011-11-16 00:29
 */
package se.marell.dvesta.ioscan.resources;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    //super(Response.status(404).entity(message).type(MediaType.TEXT_PLAIN).build());
  }
}