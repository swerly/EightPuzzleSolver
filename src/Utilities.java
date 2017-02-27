import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Seth on 2/22/2017.
 */
public class Utilities {
    public static int BOARD_DIM = 3;
    private static int[][] CORRECT_BOARD;
    static{
        CORRECT_BOARD = getBoardAs2DArray(EightPuzzleSolver.goalBoard);
    }

    public ArrayList<Node> expand(Node node){
        ArrayList<Node> closedList = new ArrayList<>();
        ArrayList<Integer> original = node.getCurrentBoard();
        ArrayList<ArrayList<Integer>> children = new ArrayList<>();
        int zeroPosition = node.getCurrentBoard().indexOf(0);

        //don't really feel like writing logic to do this
        //I AM MY OWN LOGIC
        switch(zeroPosition){
            case 0:
                children.add(getSwapped(original, 1, zeroPosition));
                children.add(getSwapped(original, 3, zeroPosition));
                break;
            case 1:
                children.add(getSwapped(original, 0, zeroPosition));
                children.add(getSwapped(original, 2, zeroPosition));
                children.add(getSwapped(original, 4, zeroPosition));
                break;
            case 2:
                children.add(getSwapped(original, 1, zeroPosition));
                children.add(getSwapped(original, 5, zeroPosition));
                break;
            case 3:
                children.add(getSwapped(original, 0, zeroPosition));
                children.add(getSwapped(original, 4, zeroPosition));
                children.add(getSwapped(original, 6, zeroPosition));
                break;
            case 4:
                children.add(getSwapped(original, 1, zeroPosition));
                children.add(getSwapped(original, 3, zeroPosition));
                children.add(getSwapped(original, 5, zeroPosition));
                children.add(getSwapped(original, 7, zeroPosition));
                break;
            case 5:
                children.add(getSwapped(original, 2, zeroPosition));
                children.add(getSwapped(original, 4, zeroPosition));
                children.add(getSwapped(original, 8, zeroPosition));
                break;
            case 6:
                children.add(getSwapped(original, 3, zeroPosition));
                children.add(getSwapped(original, 7, zeroPosition));
                break;
            case 7:
                children.add(getSwapped(original, 4, zeroPosition));
                children.add(getSwapped(original, 6, zeroPosition));
                children.add(getSwapped(original, 8, zeroPosition));
                break;
            case 8:
                children.add(getSwapped(original, 5, zeroPosition));
                children.add(getSwapped(original, 7, zeroPosition));
                break;
        }

        for (ArrayList<Integer> child : children){
            //Node x = new Node(child);
            //x.setCurrentBestParent(node);
            closedList.add(new Node(child));
        }

        return closedList;
    }

    private ArrayList<Integer> getSwapped(ArrayList<Integer> original, int p1, int p2){
        ArrayList<Integer> swapper = new ArrayList<>(original);
        Collections.swap(swapper, p1, p2);
        return swapper;
    }

    public boolean equalState(ArrayList<Integer> state1, ArrayList<Integer> state2){
        if (state1.size() != state2.size()) return false;

        for (int i = 0; i<state1.size(); i++){
            if (state1.get(i) != state2.get(i)) return false;
        }

        return true;
    }

    public ArrayList<Node> order(ArrayList<Node> openList){
        ArrayList<Node> newFront = new ArrayList<>(openList);
        Collections.sort(newFront, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.getFitness() - o2.getFitness();
            }
        });

        return newFront;
    }

    public int manhattanDistances(Node toTest){
        int distance = 0;
        //convert board to 2d array so we can calculate the distance easier
        int[][] currentBoard = getBoardAs2DArray(toTest.getCurrentBoard());

        for (int i = 0; i<BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                int currentElement = currentBoard[i][j];
                int[] correctPosition = getCorrectPosition(currentElement);
                if (currentElement != 0){
                    int diffI = i - correctPosition[0];
                    int diffJ = j - correctPosition[1];
                    distance += Math.abs(diffI) + Math.abs(diffJ);
                }
            }
        }

        return distance;
    }

    private static int[][] getBoardAs2DArray(ArrayList<Integer> board){
        int[][] boardAs2D = new int[BOARD_DIM][BOARD_DIM];

        //i is row, j is col
        for (int i = 0; i<BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                boardAs2D[i][j] = board.get(i*BOARD_DIM+j);
            }
        }

        return boardAs2D;
    }

    private int[] getCorrectPosition(int current) {
        for (int i = 0; i < BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                if (CORRECT_BOARD[i][j] == current) return new int[] {i, j};
            }
        }
        //shouldn't ever return null but the ide is complaining
        return null;
    }

    public int displacedTiles(Node toTest){
     int displaced = 0;

     for (int i = 0; i<9; i++){
         int cur = toTest.getCurrentBoard().get(i);
         if (cur != 0 && cur != EightPuzzleSolver.goalBoard.get(i)) displaced++;
     }
     return displaced;
    }

    public int combinedManhattanDisplaced(Node toTest){
        return manhattanDistances(toTest) + displacedTiles(toTest);
    }

    public String getDirection(Node n1, Node n2){
        int[][] b1 = getBoardAs2DArray(n1.getCurrentBoard());
        int[][] b2 = getBoardAs2DArray(n2.getCurrentBoard());
        int i1=-1, i2=-1, j1=-1, j2=-1;

        for (int i = 0; i<BOARD_DIM; i++){
            for (int j = 0; j<BOARD_DIM; j++){
                if (b1[i][j] == 0){
                    i1 = i;
                    j1 = j;
                    break;
                }
            }
        }

        for (int i = 0; i<BOARD_DIM; i++){
            for (int j = 0; j<BOARD_DIM; j++){
                if (b2[i][j] == 0){
                    i2 = i;
                    j2 = j;
                    break;
                }
            }
        }
        int diffI = i1-i2;
        int diffJ = j1-j2;

        if (diffI != 0){
            return diffI < 0 ? "DOWN" : "UP";
        } else {
            return diffJ < 0 ? "RIGHT" : "LEFT";
        }

    }

    public ArrayList<String> getStepsToPrint(ArrayList<Node> steps){
        ArrayList<String> stepsToPrint = new ArrayList<>();
        for (int i = 0; i<steps.size(); i++){
            StringBuilder sb = new StringBuilder();
            if (i != 0) {
                String step = String.format("%2d", i);
                sb.append("        Step " + step + ": ");
                sb.append(getBoardAsString(steps.get(i)));
                String action = String.format("%5s", getDirection(steps.get(i-1), steps.get(i)));
                sb.append(", Action: " + action);
                String g = String.format("%2d", steps.get(i).getG());
                String h = String.format("%2d", steps.get(i).getH());
                sb.append(", g = " + g);
                sb.append(", h = " + h);
                String firstIter = String.format("%3d", steps.get(i).getFirstReachedIteration());
                sb.append(", First reached in iteration: " + firstIter);
                stepsToPrint.add(sb.toString());
            }
        }
        return stepsToPrint;
    }

    public String getBoardAsString(Node n){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i : n.getCurrentBoard()){
            sb.append(i);
            if (i != n.getCurrentBoard().get(n.getCurrentBoard().size()-1)){
                sb.append(",");
            }
        }
        sb.append("}");

        return sb.toString();
    }
}
