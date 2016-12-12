import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class retrieves the necessary file from the file system, according to the argument given to it
 * on the command line. It then calls code from the Translator and InputOutputHandler as well as SAT4J modules
 * in order to parse, solve, retranslate, and print the solution to the Sudoku puzzle.
 *
 * @author Michael Crinite
 * @version 1.0 12/12/2016
 */
public class Driver {
    public static void main(String[] args){
        try{
            final long startTime = System.currentTimeMillis();//Take start time

            if(args.length > 0){
                File file = new File(args[0]);                              // Retrieve the file from the command line
                InputOutputHandler input = new InputOutputHandler(file);    // Pass the file to the IR to parse
                Translator t = Translator.getInstance();                    // Get an instance of Translator
                t.setPuzzle(input.parse());                                 // Parse the input and store the puzzle in t
                String translation = t.translate();                         // Retrieve the translation
                input.writeToFile(translation.trim());                             // Write the translation to a file

                //Set up the solver
                ISolver solver = SolverFactory.newDefault();
                solver.setTimeout(3600); // 1 hour timeout
                Reader reader = new DimacsReader(solver);
                boolean satisfiable = false;

                //Present puzzle to solver
                IProblem problem = reader.parseInstance("./sat.txt");
                satisfiable = problem.isSatisfiable();

                //Handle puzzle solution or lack thereof
                if(!satisfiable){//TODO: Add clause for invalid input
                    System.out.println("This puzzle has no valid solution.");
                    System.out.println(solver.unsatExplanation());
                }else{
                    System.out.println("Solution:");
                    String s = reader.decode(problem.model());
                    //System.out.println(s);
                    t.convertSolution(s);
                    t.printAllRows();
                }
            }else{
                System.out.println("You must provide a filename.");
            }

            final long endTime = System.currentTimeMillis();  //Take end time
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
        }catch(FileNotFoundException e){
            System.out.println("The specified file was not able to be located by SAT4J");
        }catch(IOException e){
            System.out.println("The solver cannot read that file, or a valid path was not provided.");
        }catch(ParseFormatException e){
            System.out.println("The parsed file is not in the specified format");
        }catch(ContradictionException e){
            System.out.println("Unsatisfiable (trivial!)");
        }catch(TimeoutException e){
            System.out.println("The solver timed out.");
        }
    }
}
