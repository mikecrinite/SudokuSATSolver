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
 * Created by Mike on 12/11/2016.
 */
public class Driver {
    public static void main(String[] args){
        try{
            final long startTime = System.currentTimeMillis();//Take start time

            //if(args.length > 0){
            if(true){
                //File file = new File(args[0]);                              // Retrieve the file from the command line
                File file = new File("C:\\Users\\Mike\\Desktop\\p3.txt");
                InputOutputHandler input = new InputOutputHandler(file);    // Pass the file to the IR to parse
                Translator t = Translator.getInstance();                    // Get an instance of Translator
                t.setPuzzle(input.parse());                                 // Parse the input and store the puzzle in t
                String translation = t.translate();                         // Retrieve the translation
                input.writeToFile(translation);

                //Set up the solver
                ISolver solver = SolverFactory.newDefault();
                solver.setTimeout(3600); // 1 hour timeout
                Reader reader = new DimacsReader(solver);
                boolean satisfiable = false;

                IProblem problem = reader.parseInstance("C:\\Users\\Mike\\IdeaProjects\\SudokuSATSolver\\src\\files\\sat");
                satisfiable = problem.isSatisfiable();



                if(false //The puzzle isn't valid)
                        || !satisfiable){ //There is no solution)){
                    System.out.println("This puzzle has no valid solution.");
                    System.out.println(solver.unsatExplanation());
                }else{
                    System.out.println("Solution:");
                    System.out.println(reader.decode(problem.model()));
                }
            }else{
                System.out.println("You must provide a filename.");
            }

            final long endTime = System.currentTimeMillis();  //Take end time
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
        }catch(FileNotFoundException e){
            System.out.println("The specified file was not able to be located by SAT4J");
        }catch(IOException e){
            System.out.println("SudokuSolver cannot read that file, or a valid path was not provided.");
        }catch(ParseFormatException e){
            System.out.println("The parsed file is not in the specified format");
        }catch(ContradictionException e){
            System.out.println("Unsatisfiable (trivial!)");
        }catch(TimeoutException e){
            System.out.println("The solver timed out.");
        }
    }
}
