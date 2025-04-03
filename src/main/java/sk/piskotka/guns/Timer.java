package sk.piskotka.guns;

/**
 * A Timer class that tracks elapsed time and checks if a target time has been reached.
 * The timer operates in milliseconds and must be updated manually.
 */
public final class Timer {
    private double currTime;
    private final double targetTime;

    /**
     * Constructs a Timer with a specified target time.
     * 
     * @param targetTime the target time in milliseconds
     */
    public Timer(double targetTime){
        this.targetTime = targetTime;
        reset();
    }

    /**
     * Resets the timer's current time to zero.
     */
    public void reset(){
        this.currTime = 0;
    }

    /**
     * Updates the timer by adding the elapsed time.
     * 
     * @param dt the elapsed time in milliseconds to add
     */
    public void tick(double dt){
        this.currTime += dt;
    }

    /**
     * Calculates the remaining time until the target time is reached.
     * 
     * @return the remaining time in milliseconds; a negative value indicates the target time has been exceeded
     */
    public double remainingTime(){
        return targetTime - currTime;
    }

    /**
     * Calculates the completion percentage of the timer.
     * 
     * @return the completion percentage as a value between 0 and 1; values greater than 1 indicate the target time has been exceeded
     */
    public double completion(){
        return currTime / targetTime;
    }

    /**
     * Checks if the timer has reached or exceeded the target time.
     * 
     * @return true if the timer is ready (i.e., the target time has been reached or exceeded), false otherwise
     */
    public boolean isReady(){
        return remainingTime() <= 0;
    }
}
