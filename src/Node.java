import java.util.ArrayList;

/**
 * Created by Seth on 2/20/2017.
 */
public class Node {
    private ArrayList<Integer> currentBoard;
    private Node currentBestParent;
    private int g, h;
    private int firstReachedIteration;

    public Node(){
        this(new ArrayList<>());
    }

    public Node(ArrayList<Integer> current){
        this.currentBoard = current;
        currentBestParent = null;
    }

    public ArrayList<Integer> getCurrentBoard() {
        return currentBoard;
    }

    public Node getCurrentBestParent() {
        return currentBestParent;
    }

    public int getFitness(){
        return g+h;
    }

    public void setCurrentBoard(ArrayList<Integer> currentBoard) {
        this.currentBoard = currentBoard;
    }

    public void setCurrentBestParent(Node currentBestParent) {
        this.currentBestParent = currentBestParent;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public int getFirstReachedIteration() {
        return firstReachedIteration;
    }

    public void setFirstReachedIteration(int firstReachedIteration) {
        this.firstReachedIteration = firstReachedIteration;
    }
}
