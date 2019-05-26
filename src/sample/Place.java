package sample;

import java.util.ArrayList;

public class Place {

    private String number;
    private int x;
    private int y;
    private int flor;
    private PlaceEnum type;
    private boolean isTested = false;
    private boolean isExpanded = false;
    private double distance = 0.0;
    private int depth = -1;
    private Place parent = null;
    private double weight =0.0;


    public boolean isTested() {
        return isTested;
    }

    public void setTested(boolean tested) {
        isTested = tested;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Place getParent() {
        return parent;
    }

    public void setParent(Place parent) {
        this.parent = parent;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFlor() {
        return flor;
    }

    public void setFlor(int flor) {
        this.flor = flor;
    }

    public PlaceEnum getType() {
        return type;
    }

    public void setType(PlaceEnum type) {
        this.type = type;
    }

    public Place(String number, int x, int y, int flor, PlaceEnum type) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.flor = flor;
        this.type = type;
        this.isExpanded = false;
        this.isTested = false;
        this.parent = null;
        this.distance = 0.0;
        this.weight = 0.0;
    }

    public Place(String n, double w) {
        this.number = n;
        this.weight = w;
    }

    ArrayList<Transition> transitions = new ArrayList<Transition>();

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    public String getNumber() {
        return number;
    }

    public void reset(){
        this.isTested = false;
        this.isExpanded = false;
        this.depth =-1;
        this.distance = 0.0;
        this.weight = 0.0;
        this.parent = null;
    }


}
