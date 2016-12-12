import java.io.*;

/**
 * A filereader as efficient as I could make it in the last day of
 * the project that I procrastinated an incredible amount on.
 *
 * @author Michael Crinite
 * @version 0.1 12/11/2016
 *
 */
public class InputOutputHandler {

    private File file;
    private int rows;
    private int cols;
    private int[][] puzzle = null;

    /**
     * Constructor for Input class.
     *
     * @param f File to read
     */
    public InputOutputHandler(File f) {
        file = f;
    }

    /**
     * Reads a properly-formatted sudoku file and
     * @return
     */
    public int[][] parse(){
        int i = 0; //row counter
        int dimCounter = 0;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String curr;

            while((curr = br.readLine())!=null){
                if(curr.startsWith("c")){
                    //Line is a comment
                }else if(dimCounter == 0){
                    rows = Integer.parseInt(curr);
                    dimCounter++;
                }else if(dimCounter == 1){
                    cols = Integer.parseInt(curr);
                    dimCounter++;
                }else{
                    if(puzzle==null){
                        puzzle = new int[rows*cols][rows*cols];
                    }
                    String[] temp = curr.split(" ");
                    for(int index = 0; index < (rows*cols); index++){
                        puzzle[i][index] = Integer.parseInt(temp[index]);
                    }
                    i++;
                }
            }
            fr.close();
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }catch(NumberFormatException e){
            System.out.println("Check that the file is in the proper format:" +
                    "\nThe file can begin with any amount of comment lines (denoted by a 'c')." +
                    "\nThe file then MUST contain two lines denoting the dimensions of a box." +
                    "\nThe next 9 lines must contain 9 integers each separated by a single space.");
        }

        return puzzle;
    }

    public void writeToFile(String s){
        try{
            //Create file
            File f = new File("C:\\Users\\Mike\\IdeaProjects\\SudokuSATSolver\\src\\files" + "\\sat2");

            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s);
            bw.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}