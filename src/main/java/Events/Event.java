package Events;

import Domain.EventQueueManager;

public abstract class Event {

    private double time;
    private int id;
    EventQueueManager eventQueueManager;
    public static long id_generator = 1;

    public Event(double time, EventQueueManager eventQueueManager) {
        this.time=time;
        this.eventQueueManager=eventQueueManager;
        this.id_generator++;
    }

    public double getTime() {
        return time;
    }

    public int getId(){
        return id;
    }

    public abstract void runEvent();
}
