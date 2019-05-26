package sample;

public class Transition {
    double distance;
    Place to;
    TransitionEnum transitionEnum;
    boolean biliteral;
    double weight;

    public double getDistance() {
        return distance;
    }

    public Place getTo() {
        return to;
    }

    public TransitionEnum getTransitionEnum() {
        return transitionEnum;
    }

    public boolean isBiliteral() {
        return biliteral;
    }

    public double getWeight() {
        return weight;
    }

    public Transition(Place to, TransitionEnum transitionEnum, boolean biliteral, double weight) {
        this.to = to;
        this.transitionEnum = transitionEnum;
        this.biliteral = biliteral;
        this.weight = weight;
    }

    public Transition(double distance, Place to, TransitionEnum transitionEnum, boolean biliteral) {
        this.distance = distance;
        this.to = to;
        this.transitionEnum = transitionEnum;
        this.biliteral = biliteral;
    }

    public Transition(Place to, TransitionEnum transitionEnum, double weight) {
        this.to = to;
        this.transitionEnum = transitionEnum;
        this.weight = weight;
    }

    public Transition(double distance, Place to) {
        this.distance = distance;
        this.to = to;
    }

    public Transition(double distance, Place to, TransitionEnum transitionEnum) {
        this.distance = distance;
        this.to = to;
        this.transitionEnum = transitionEnum;
    }
}
