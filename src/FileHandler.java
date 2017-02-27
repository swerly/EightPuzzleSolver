import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Seth on 2/26/2017.
 */
public class FileHandler {
    ArrayList<String> linesToPrint;

    public ArrayList<ArrayList<Integer>> getExamples(){
        ArrayList<ArrayList<Integer>> examples = new ArrayList<>();

        // Construct BufferedReader from FileReader
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("inputFile.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                ArrayList<Integer> currentLine = new ArrayList<>();
                Scanner sc = new Scanner(line);
                sc.useDelimiter(",");
                while (sc.hasNext()){
                    currentLine.add(sc.nextInt());
                }
                examples.add(currentLine);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return examples;
    }

    public FileHandler(){
        linesToPrint = new ArrayList<>();
    }

    public void addToOutput(String line){
        linesToPrint.add(line);
    }

    public void addToOutput(ArrayList<String> lines){
        linesToPrint.addAll(lines);
    }

    public void resetLines(){
        linesToPrint = new ArrayList<>();
    }

    public void writeToFile(String heuristic){
        String fName = "outputFiles/";
        switch (heuristic){
            case EightPuzzleSolver.H1:
                fName += "OutfileHeuristic1.txt";
                break;
            case EightPuzzleSolver.H2:
                fName += "OutfileHeuristic2.txt";
                break;
            case EightPuzzleSolver.H3:
                fName += "OutfileHeuristic3.txt";
                break;
        }
        try {
            File output = new File(fName);
            output.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(output);
            for (String str : linesToPrint){
                writer.write(str);
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
