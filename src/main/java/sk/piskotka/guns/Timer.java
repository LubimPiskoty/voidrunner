package sk.piskotka.guns;

// Timer in miliseconds, has to be updated 
public class Timer {
    private double currTime;
    private double targetTime;

    public Timer(double targetTime){
        this.targetTime = targetTime;
        reset();
    }

    public void reset(){
        this.currTime = 0;
    }

    public void tick(double dt){
        this.currTime += dt;
    }

    public double remainingTime(){
        return targetTime - currTime;
    }

    public double completion(){
        return currTime/targetTime;
    }

    public boolean isReady(){
        return remainingTime() <= 0;
    }
    
}
