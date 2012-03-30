package penoplatinum.driverevents;

/**
 * This implements the base linked-list implementation of the events.
 * @author Team Platinum
 */
public abstract class BaseEvent implements DriverEvent{
  private DriverEvent nextEvent;

  public BaseEvent() {
  }
  
  public BaseEvent(DriverEvent next){
    this.nextEvent = next;
  }

  @Override
  public DriverEvent nextEvent() {
    return nextEvent;
  }
  
}
