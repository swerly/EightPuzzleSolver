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
        ArrayList<Node> expanded = new ArrayList<>();
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
            Node x = new Node(child);
            x.setCurrentBestParent(node);
            expanded.add(x);
        }

        return expanded;
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

    public ArrayList<Node> order(ArrayList<Node> frontier){
        ArrayList<Node> newFront = new ArrayList<>(frontier);
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
}
