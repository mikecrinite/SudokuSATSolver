import java.io.File;

/**
 * Created by Mike on 12/11/2016.
 */
public class Driver {
    public static void main(String[] args){
        try{
            final long startTime = System.currentTimeMillis();//Take start time

            if(args.length > 0){
                File file = new File(args[0]);
                InputReader input = new InputReader(file);
                Translator t = Translator.getInstance();
                String translation = t.translate();

                //Do the things
                if(false //The puzzle isn't valid)
                        || true){ //There is no solution)){
                    System.out.println("This puzzle has no valid solution.");
                }else{
                    //Print the solution
                }
            }else{
                System.out.println("You must provide a filename.");
            }

            final long endTime = System.currentTimeMillis();  //Take end time
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
        }catch(Exception e){
            System.out.println("SudokuSolver cannot read that file, or a valid path was not provided.");
        }

    }
}
