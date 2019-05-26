package sample;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class DijkstraSearch extends BaseSearch {
    Build build;
    String skipTransition = null;
    String priorityTransition = null;

    public DijkstraSearch(Build b) {
        this.build = b;
    }

    @Override
    public boolean checkForPath(String startName, String endName) {
        build.resetAllNodes();

        if (!build.containsPlaceName(startName) ||
                !build.containsPlaceName(endName)) {
            return false;
        }
        Place startNode = build.getNode(startName);
        ArrayList<Place> queue = new ArrayList<>();
        queue.add(startNode);

        Place temp;

        while (!queue.isEmpty()) {
            temp = queue.get(0);

            Iterator<Transition> transition = temp.transitions.iterator();
            while (transition.hasNext()) {
                if (transition.next().transitionEnum.name().equals(skipTransition)) {
                    transition.remove();
                    continue;
                }
            }
            setParentAndCost(temp, startName);
            String transitionType = "";
            if (temp.getParent() != null) {

//                if(temp.transitions.isEmpty()){
//                    temp.isTested = true;
//                    temp.isExpanded = true;
//                }

                for (Transition t : temp.transitions) {
                    if (t.to.getNumber().equals(temp.getParent().getNumber())) {
                        transitionType = t.transitionEnum.name();
                    }
                }
                transitionType = transitionType.isEmpty() ? "Haven't transition!!!" : transitionType;
                System.out.println("Temp node is: " + temp.getNumber() + " transition type: " + transitionType
                        + " , parent " + temp.getParent().getNumber()
                        + " , weight " + temp.getWeight());
            }

            if (temp.getNumber().equals(endName)) {
                printPath(endName);
                return true;
            }

            temp.setTested(true);
            queue.remove(0);

            for (Place place : build.getLinkedNodes(temp.getNumber())) {

                if (!place.isExpanded() && !queue.contains(place)) {
                    queue.add(place);
                }
            }
            build.sortByWeight(queue);
            temp.setExpanded(true);


        }//end while

        printPath(endName);
        return (build.getNode(endName).getParent() != null);
    }

    @Override
    public boolean checkForPathByCoordinates(String startName, String endName) {
        {
            build.resetAllNodes();
            if (!build.containsPlaceName(startName) ||
                    !build.containsPlaceName(endName)) {
                return false;
            }
            Place startNode = build.getNode(startName);
            ArrayList<Place> queue = new ArrayList<>();
            queue.add(startNode);
            startNode.setDepth(0);

            Place temp;
            while (!queue.isEmpty()) {
                temp = queue.get(0);


                Iterator<Transition> transition = temp.transitions.iterator();
                while (transition.hasNext()) {
                    if (transition.next().transitionEnum.name().equals(skipTransition)) {
                        transition.remove();
                        continue;
                    }
                }

                setParentAndCostForCoordinatesSearch(temp, startName);
                String transitionType = "";
                if (temp.getParent() != null) {

//                if(temp.transitions.isEmpty()){
//                    temp.isTested = true;
//                    temp.isExpanded = true;
//                }

                    for (Transition t : temp.transitions) {
                        if (t.to.getNumber().equals(temp.getParent().getNumber())) {
                            transitionType = t.transitionEnum.name();
                        }
                    }
                    transitionType = transitionType.isEmpty() ? "Haven't transition!!!" : transitionType;
                    System.out.println("Temp place is: " + temp.getNumber() + " transition type: " + transitionType
                            + " , parent " + temp.getParent().getNumber()
                            + " , weight " + temp.getDepth());
                }
                if (temp.getNumber().equals(endName)) {

                    printPath(endName);
                    return true;
                }

                build.setDepths(temp.getNumber());
                temp.setTested(true);
                queue.remove(0);

                for (Place place : build.getLinkedNodes(temp.getNumber())) {
                    if (!place.isTested() && !queue.contains(place)) {
                        place.setDistance(build.findDistance(place.getNumber(), endName));
                        queue.add(place);
                    }
                }
                build.sortByDistance(queue);
                temp.setExpanded(true);

            }

            printPath(endName);
            return false;
        }
    }


    @Override
    public String skipTransition(boolean skip, TransitionEnum transition) {
        skipTransition = null;
        if (!skip) {
            return skipTransition;
        } else {
            return skipTransition = transition.name();
        }
    }

    @Override
    public String findPathByPriorityTransition(boolean skip, TransitionEnum transitionEnum) {
        priorityTransition = null;

        if (!skip) {
            return priorityTransition;
        } else
            doublingCosts(transitionEnum);
            return priorityTransition = transitionEnum.name();

    }

    public void doublingCosts(TransitionEnum transitionEnum){
        build.doubling(transitionEnum.name());
    }
    private void setParentAndCostForCoordinatesSearch(Place place, String startName) {
        Place temp;
        for (Transition transition : place.transitions) {
            if (transition.to.getNumber().equals(startName)) continue;
            temp = build.getNode(transition.to.getNumber());
            if ((temp.getParent() == null)) {
                temp.setParent(place);
            }
        }
    }

    private void setParentAndCost(Place place, String startName) {

        Place temp;

        //need for sorted by descending when have two or more transitions with same start and endname
        place.transitions.sort(Comparator.comparing(Transition::getWeight).reversed());
        for (Transition transition : place.transitions) {
            if (transition.to.getNumber().equals(startName)) continue;
            temp = build.getNode(transition.to.getNumber());
            if ((temp.getParent() == null) || (temp.getWeight() > place.getWeight() + transition.weight)) {
                temp.setParent(place);
                temp.setWeight(place.getWeight() + transition.weight);
            }
        }
    }

    public void printPath(String name) {
        Place node = build.getNode(name);
        StringBuilder path = new StringBuilder();
        do {
            path.insert(0, "->" + node.getNumber());
            node = node.getParent();
        } while (node != null);
        System.out.println(path + " weight: " + build.getNode(name).getWeight());
    }
}
