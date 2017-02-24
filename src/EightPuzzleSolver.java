import javax.rmi.CORBA.Util;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Seth on 2/22/2017.
 */
public class EightPuzzleSolver {
    public static final String H1 = "Displaced";
    public static final String H2 = "Manhattan";
    public static final String H3 = "Displaced+Manhattan";
    public static ArrayList<Integer> goalBoard;
    static{
        Integer[] goal = {1,2,3,8,0,4,7,6,5};
        goalBoard = new ArrayList<>(Arrays.asList(goal));
    }

    private ArrayList<Node> expanded, frontier;
    private Utilities utilities;
    private String currentHeuristic;


    public void run(){
        utilities = new Utilities();
        //get input file
        //create initial nodes from lines in file
        //solve puzzle for each starting node

        currentHeuristic = H1;

        Integer[] boardx = {2,8,3,1,6,4,7,0,5};
        ArrayList<Integer> board = new ArrayList<>(Arrays.asList(boardx));
        Node startNode = new Node(board);
        ArrayList<Node> solved = solve(startNode);
        System.out.println("Done");
    }

    public ArrayList<Node> solve(Node startNode){
        int expansions = 0;
        expanded = new ArrayList<>();
        frontier = new ArrayList<>();

        frontier.add(startNode);

        //while we still have nodes that can be expanded
        while (frontier.size() > 0){
            Node toExpand = getBestFitness();
            frontier.remove(toExpand);
            ArrayList<Node> children = utilities.expand(toExpand);
            expansions++;
            for (Node child : children){
                //if this is the end state
                if (utilities.equalState(goalBoard, child.getCurrentBoard())){
                    expanded.add(toExpand);
                    expanded.add(child);
                    return expanded;
                }

                child.setG(expansions);
                child.setH(getHeuristic(child));

                checkChild(child);
            }
            expanded.add(toExpand);
        }
        return expanded;
    }

    private Node getBestFitness(){
        ArrayList<Node> ordered = utilities.order(frontier);
        return ordered.get(0);
    }

    private int getHeuristic(Node n){
        switch(currentHeuristic){
            case H1:
                return utilities.displacedTiles(n);
            case H2:
                return utilities.manhattanDistances(n);
            case H3:
                return utilities.combinedManhattanDisplaced(n);
            default:
                System.out.println("INVALID HEURISTIC");
                return -1;
        }
    }

    private void checkChild(Node child) {
        for (Node inFrontier : frontier) {
            //we found the correct node in the frontier
            if (utilities.equalState(inFrontier.getCurrentBoard(), child.getCurrentBoard())
                    && child.getFitness() > inFrontier.getFitness()) {
                return;
            }
        }

        for (Node inExpanded : expanded) {
            //we found the correct node in the frontier
            if (utilities.equalState(inExpanded.getCurrentBoard(), child.getCurrentBoard())
                    && child.getFitness() > inExpanded.getFitness()) {
                return;
            }
        }

        frontier.add(child);
    }

    public void testing(){
        Integer[] boardx = {1,2,3,7,8,4,0,6,5};
        ArrayList<Integer> board = new ArrayList<>(Arrays.asList(boardx));
        Node n = new Node();
        n.setCurrentBoard(board);

        System.out.println("Displaced: " + utilities.displacedTiles(n));

        /*
        ArrayList<Node> expanded = utilities.expand(n);
        for (Node node : expanded){
            System.out.print("Child: ");
            for (int i = 0; i< node.getCurrentBoard().size() ; i++){
                System.out.print(node.getCurrentBoard().get(i));
                if (i != 8) System.out.print(", ");
            }
            System.out.println();
        }*/
    }
}
