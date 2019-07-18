package nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache;

import nl.ica.oose.a2.zorgrit.service.ridematcher.Strategies.IStrategy;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;

import java.util.Date;

public class RideMatchState {
    private RideDTO ride;
    private IStrategy strategy;
    private int counter;
    private int maxCounter;
    private Date createdAt;

    public RideMatchState(RideDTO ride, IStrategy strategy, int maxCounter) {
        this.ride = ride;
        this.strategy = strategy;
        this.counter = 0;
        this.maxCounter = maxCounter;
        this.createdAt = new Date();
    }

    public RideDTO getRide() {
        return ride;
    }

    public void setRide(RideDTO ride) {
        this.ride = ride;
    }

    public IStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(IStrategy strategy) {
        this.strategy = strategy;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getMaxCounter() {
        return maxCounter;
    }

    public void setMaxCounter(int maxCounter) {
        this.maxCounter = maxCounter;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void Execute(RideDTO ride) {
        this.strategy.execute(ride);
    }

    @Override
    public String toString() {
        return "RideDTO: " + ride.getId() + ", with " + strategy.getName() + ", counter: " + counter + "/" + maxCounter;
    }
}
