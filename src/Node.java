import java.util.ArrayList;

/**
 * Created by Seth on 2/20/2017.
 */
public class Node {
    private ArrayList<Integer> currentBoard;
    private Node currentBestParent;
    double g, h;

    public Node(){
        currentBoard = new ArrayList<>();
        currentBestParent = null;
    }
}
