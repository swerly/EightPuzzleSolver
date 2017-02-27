import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

    private ArrayList<Node> closedList, openList;
    private Utilities utilities;
    private FileHandler fileHandler;
    private String currentHeuristic;
    private int expandedCount;


    public void run(){
        utilities = new Utilities();
        fileHandler = new FileHandler();
        //get the starting boards from the input file
        ArrayList<ArrayList<Integer>> startBoards = fileHandler.getExamples();
        //convert boards to starting nodes
        ArrayList<Node> startNodes = new ArrayList<>();
        for (ArrayList<Integer> startBoard : startBoards){
            startNodes.add(new Node(startBoard));
        }

        //for each heuristic
        for(int i = 0; i<3; i++){
            //set the current heuristic
            currentHeuristic = i == 0 ? H1 : (i == 1 ? H2 : H3);
            //reset our fileHandler
            fileHandler.resetLines();
            int set = -1;
            for (int j = 0; j < startNodes.size(); j++) {
                if (j%5 == 0){
                    if (j<5){
                        set = 1;
                    } else if (j<10){
                        set = 2;
                    } else{
                        set = 3;
                    }
                    fileHandler.addToOutput((set != 1 ? "\n\n" : "" ) + "START OF PROBLEM SET " + set);
                }
                fileHandler.addToOutput("    PROBLEM " + ((j%5)+1) + ", SET " + set);

                //SOLVE 8 PUZZLE
                ArrayList<Node> solved = solve(startNodes.get(j));
                fileHandler.addToOutput("        Start State: " + utilities.getBoardAsString(startNodes.get(j)));
                fileHandler.addToOutput("        Number Nodes Expanded: " + expandedCount);

                int totalNodes = closedList.size() + openList.size();
                int solutionDepth = solved.get(solved.size()-1).getG();
                double branchingFactor = Math.pow((double)totalNodes, 1f/(double)solutionDepth);
                String bf = String.format("%5.3f", branchingFactor);
                //fileHandler.addToOutput("        Branching Factor: " + bf);
                fileHandler.addToOutput(utilities.getStepsToPrint(solved));
            }
            fileHandler.writeToFile(currentHeuristic);
        }
    }



    public ArrayList<Node> solve(Node startNode) {
        //reset expanded
        expandedCount = 0;
        //create the lists
        closedList = new ArrayList<>();
        openList = new ArrayList<>();
        //add the starting node to the open list
        openList.add(startNode);
        //while open list is not empty
        while (!openList.isEmpty()) {
            //get the node with the lowest fitness
            Node current = getBestFitness();
            openList.remove(current);
            //if the best fitness is the goal node, return the path
            if (utilities.equalState(current.getCurrentBoard(), goalBoard)){
                return traverseUp(current);
            }

            //get the successors
            ArrayList<Node> successors = utilities.expand(current);
            expandedCount++;
            //for each successor of our current node
            for (Node successor : successors){
                int firstReachedIteration = -1;
                //set the cost to be current + 1
                successor.setG(current.getG() + 1);
                //find node in open list
                Node listChecker = nodeInOpen(successor);
                //if our successor is in the open list
                //but the existing one is as good or better
                if (listChecker != null && listChecker.getG() <= successor.getG()){
                    continue;
                }

                //find node in closed list
                listChecker = nodeInClosed(successor);
                //if successor is in the closed list
                //and the existing one is as good or better
                if (listChecker != null && listChecker.getG() <= successor.getG()){
                    continue;
                }

                //set parent of successor to be current node
                successor.setCurrentBestParent(current);
                firstReachedIteration = getFirstReached(successor);
                //remove occurrences from open/closed
                removeFromLists(successor);
                successor.setFirstReachedIteration(firstReachedIteration == -1 ? current.getG()+1: firstReachedIteration);
                //set heuristic value
                successor.setH(getHeuristic(successor));
                openList.add(successor);
            }
            //add current node to closed list
            closedList.add(current);
        }
        return null;
    }

    private int getFirstReached(Node n){
        Node listChecker = nodeInOpen(n);
        //if our successor is in the open list
        //but the existing one is as good or better
        if (listChecker != null){
            return listChecker.getFirstReachedIteration();
        }

        listChecker = nodeInClosed(n);
        if (listChecker != null){
            return listChecker.getFirstReachedIteration();
        }

        return expandedCount;
    }

    private void removeFromLists(Node toRemove){
        for (Node n : openList){
            if (utilities.equalState(n.getCurrentBoard(), toRemove.getCurrentBoard())){
                openList.remove(n);
                break;
            }
        }
        for (Node n : closedList){
            if (utilities.equalState(n.getCurrentBoard(), toRemove.getCurrentBoard())){
                closedList.remove(n);
                break;
            }
        }
    }

    private ArrayList<Node> traverseUp(Node end){
        Node tNode = end;
        ArrayList<Node> solution = new ArrayList<>();

        do{
            solution.add(tNode);
            tNode = tNode.getCurrentBestParent();
        } while(tNode!= null);

        Collections.reverse(solution);
        return solution;
    }

    private Node getBestFitness(){
        int best = openList.get(0).getFitness();
        Node bestNode = openList.get(0);
        for (Node n : openList){
            if (n.getFitness() < best){
                bestNode = n;
            }
        }
        return bestNode;
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

    private Node nodeInClosed(Node n){
        return nodeInList(closedList, n);
    }

    private Node nodeInOpen(Node n){
        return nodeInList(openList, n);
    }

    public Node nodeInList(ArrayList<Node> list, Node toCheck){
        for (Node n : list){
            if (utilities.equalState(n.getCurrentBoard(), toCheck.getCurrentBoard())){
                return n;
            }
        }
        return null;
    }

}
