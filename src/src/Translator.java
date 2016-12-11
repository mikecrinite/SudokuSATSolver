/**
 * This Class should complete the reduction from the input Sudoku puzzle to a boolean satisfiability problem. It will
 * also be able to complete the conversion from the resultant boolean SAT solution to the resultant Sudoku solution.
 *
 * The constraints of a Sudoku puzzle can be modeled using a boolean forumla in conjunctive normal form.
 *     - 729 variables will be needed to represent the possibility of each value in each space (111) = 1 in [1,1]
 *     - 4 Sudoku constraints:
 *         - 1. Each row must have all the digits 1-9
 *         - 2. Each column must have all the digits 1-9
 *         - 3. Each 3x3 box must have all the digits 1-9
 *         - 4. Each cell must have exactly one value in the range 1-9
 * For each above constraint, we will need two methods.
 *
 * @author Michael Crinite
 * @version 0.1 12/11/2016
 */
public class Translator {
    // Fields
    public int[] vars = new int[729];           //May not use TODO: remove if not used
    public String sat = "";                     //SAT solver input
    public static Translator INSTANCE = null;   //Singleton instance

    public static Translator getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Translator();
        }
        return INSTANCE;
    }

    /**
     * 1.1.A: At least one cell of row i has the value N
     */
    public void constraint1A(){
        String temp = "";
        for(int row = 1; row < 10; row++){
            for(int val = 1; val < 10; val++){
                for(int col = 1; col < 10; col++){
                    sat += "" + row + col + val + " ";
                    if(col == 9){
                        sat += "0\n";
                    }
                }
            }
        }
    }

    /**
     * 1.1.B: At most one cell of row i has the value N
     */
    public void constraint1B(){

    }

    /**
     * 2.1.A: At least one cell of column i has the value N
     */
    public void constraint2A(){
        String temp = "";
        for(int col = 1; col < 10; col++){
            for(int val = 1; val < 10; val++){
                for(int row = 1; row < 10; row++){
                    sat += "" + row + col + val + " ";
                    if(row == 9){
                        sat += "0\n";
                    }
                }
            }
        }
    }

    public void constraint2B(){

    }

    /**
     * 3.1.A: At least one cell of box i has the value N
     */
    public void constraint3A(){
        String temp = "";
    }
    public void constraint4(){}

    public int[] getVars(){
        return vars;
    }

    public String getSat(){
        return sat;
    }

    /**
     * For testing purposes only
     * TODO: Remove when finished
     * @param args Command line arguments
     */
    public static void main(String[] args){
        Translator t = Translator.getInstance();
        t.constraint2A();
        System.out.println(t.getSat());
    }
}
