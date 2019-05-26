package sample;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Build {
    public HashMap<String, Place> build = new HashMap<>();
    Comparator<Place> byWeight = (Place n1,Place n2)
            -> Double.compare(n1.getWeight(), n2.getWeight());
    Comparator<Place> byName = (Place n1, Place n2)
            -> n1.getNumber().compareTo(n2.getNumber());
    Comparator<Place> byDistance = (Place n1,Place n2)
            -> Double.compare(n1.getDistance(), n2.getDistance());

    public void addPlaceToBuild(Place place) {

        if (build.containsKey(place.getNumber())) {
            System.err.println("Place is duplicated!");
            return;
        }

        build.put(place.getNumber(), place);
    }

    public void resetAllNodes() {
        build.forEach((k,v) -> v.reset());
    }

    public void doubling(String s) {
        build.forEach((k,v) -> v.transitions.forEach(e-> {
            if(e.transitionEnum.name().equals(s)) {
                e.to.setWeight(e.weight * 2);
                e.weight = e.weight * 2;
            }
        }));
        System.out.println("");
    }

    public void createTransition(String from, String to, TransitionEnum transition,int cost,String bilateralString
    ) {
        boolean bilateral = bilateralString.equals("yes") ? true : false;

        Place startPoint = build.get(from);
        Place endPoint = build.get(to);

        if (startPoint != null && endPoint != null) {
            //refactor distance
            startPoint.addTransition(new Transition(cost, endPoint,transition));

            if (bilateral) {
                endPoint.addTransition(new Transition(cost, startPoint,transition));
            }

        }
    }
    public void createTransitionWithWight(String from, String to, TransitionEnum transition,double weight,String bilateralString
    ) {
        boolean bilateral = bilateralString.equals("yes") ? true : false;

        Place startPoint = build.get(from);
        Place endPoint = build.get(to);

        if (startPoint != null && endPoint != null) {
            //refactor distance
            startPoint.addTransition(new Transition(endPoint,transition,weight));

            if (bilateral) {
                endPoint.addTransition(new Transition(startPoint,transition,weight));
            }

        }
    }
    public void sortByWeight(ArrayList<Place> list) {

        list.sort(byWeight.thenComparing(byName));
    }

    public void sortByDistance(ArrayList<Place> list) {

        list.sort(byDistance.thenComparing(byName));
    }

    public void setDepths(String name) {
        Place node = build.get(name);
        for(Place n : getLinkedNodes(name)) {
            if(n.getDepth() == -1) {
                n.setDepth(node.getDepth() +1);
            }
        }
    }

    public ArrayList<Place> getLinkedNodes(String name){
        ArrayList<Place> linkedNodes = new ArrayList<>();
        Place node = build.get(name);
        for(Transition t : node.transitions) {
            linkedNodes.add(build.get(t.to.getNumber()));
        }
        return linkedNodes;
    }
    public void printPathCoordinate(Place current) {
        StringBuilder path = new StringBuilder();

        while(current.getDepth() != 0) {
            path.append(current.getNumber());

            for(Place palce : getLinkedNodes(current.getNumber())) {
                if(palce.getDepth() == current.getDepth() - 1) {
                    current = palce;
                    break;
                }
            }
        }

        path.append(current.getNumber());


        System.out.println(path.reverse());
    }

    public double findDistance(String nameOne, String nameTwo) {

        Place placeOne = build.get(nameOne);
        Place placeTwo = build.get(nameTwo);

        double distance = Math.sqrt(Math.pow(placeOne.getX() - placeTwo.getX(), 2)
                + Math.pow(placeOne.getY() - placeTwo.getY(), 2));
        return distance;

    }

    public boolean containsPlaceName(String startName) {
            return build.containsKey(startName);
    }

    public Place getNode(String name){
        return build.get(name);
    }
}
