package sample;

public  abstract  class BaseSearch {

    public abstract boolean
    checkForPath(String from, String to);

    public abstract boolean
    checkForPathByCoordinates(String from, String to);

    public abstract String skipTransition(boolean skip, TransitionEnum transitionEnum);
    
    
    protected void printPath(Place current) {
        StringBuilder sb = new StringBuilder();

        while(current.getDepth() != 0) {
            sb.append(current.getNumber());
            sb.append(",");

            for(Transition transition : current.transitions) {
                if(transition.to.getDepth() == current.getDepth() - 1) {
                    current = transition.to;
                    break;
                }
            }
        }

        sb.append(current.getNumber());

        System.out.println("The path is:" + sb.reverse());

    }

    public abstract String findPathByPriorityTransition(boolean b, TransitionEnum climb);
}
