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
    public int[][] puzzle;
    public int[] vars = new int[729];           //May not use TODO: remove if not used
    public String sat = "";                     //SAT solver input
    public static Translator INSTANCE = null;   //Singleton instance
    public String [] str = new String[729];     //Store vars from 3A for use in 3B

    /**
     * Default constructor for type Translator
     */
    public Translator(){
        //Default constructor
    }

    /**
     * Translator which passes puzzle as a parameter
     * @param p
     */
    public Translator(int[][] p){
        puzzle = p;
    }

    /**
     * Retrieves an instance of the Translator Class.
     * @return The single instance of class Translator
     */
    public static Translator getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Translator();
        }
        return INSTANCE;
    }

    /**
     * Satisfies:
     *  1.1.A: At least one cell of row i has the value N
     * Iterates through row, value, column to create a line which ensures that at least one of the spaces in the
     * row contains N
     */
    public void constraint1A(){
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
     * Satisfies:
     *  1.1.B: At least one cell of row i has the value N
     * Iterates through row, value, column to create a line which ensures that at most one of the spaces in the
     * row contains N
     */
    public void constraint1B(){
        String curr = "";
        String next = "";
        for(int row =1; row < 10; row++){
            for(int val = 1; val < 10; val++){
                for(int col = 1; col < 10; col++){
                    for(int nxt = (col + 1); nxt < 10; nxt++) {
                        curr = "" + row + col + val;
                        next = "" + row + nxt + val;
                        sat += "-" + curr + " " + "-" + next + " 0\n";
                    }
                }
            }
        }
    }

    /**
     * Satisfies:
     *  2.1.A: At least one cell of column i has the value N
     * Iterates through column, value, row to create a line which ensures that at least one of the spaces in the
     * column contains N
     */
    public void constraint2A(){
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

    /**
     * Satisfies:
     *  2.1.B: At most one cell of column i has the value N
     * Iterates through column, value, row to create a line which ensures that at most one of the spaces in the
     * column contains N
     */
    public void constraint2B(){
        String curr = "";
        String next = "";
        for(int col =1; col < 10; col++){
            for(int val = 1; val < 10; val++){
                for(int row = 1; row < 10; row++){
                    for(int nxt = (row + 1); nxt < 10; nxt++) {
                        curr = "" + row + col + val;
                        next = "" + row + nxt + val;
                        sat += "-" + curr + " " + "-" + next + " 0\n";
                    }
                }
            }
        }
    }

    /**
     * Satisfies:
     *  3.1.A: At least one cell of box i has the value N
     * Iterates through a box to create a line which ensures that at least one space in the box contains N.
     * As it is progressing through each value, it creates an array that will be iterated through in constraint3B()
     */
    public void constraint3A(){
        //rowIndex + colIndex + val_1-9
        String temp;
        int index = 0;

        for (int val = 1; val < 10; val++) {
            for (int row = 1; row < 10; row += 3) {
                for (int col = 1; col < 10; col += 3) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            temp = (row + i) + "" + (col + j) + "" + val + " ";
                            str[index] = temp;
                            sat += temp;
                            index++;
                        }
                    }
                    sat += "0\n";
                    //System.out.println("Index: " + index);
                }
            }
        }
    }

    /**
     * Satisfies
     *  3.1.B: At most one cell of box i has the value N
     * Iterates through the array created dynamically above in order to create lines which ensure that at most one
     * cell in the box contains N
     */
    public void constraint3B(){
        String curr;        //Current Value
        String next;        //Next value
        int address = 0;    //Array address to begin at
        int remain = 8;     //Remaining items in box

        while(address < 728) {
            for (int j = 1; j <= remain; j++) {
                curr = str[address];
                next = str[address + j];
                sat += "-" + curr + " " + "-" + next + " 0\n";
            }

            address++;
            if(remain == 0){
                //address++;
                remain = 8;
            }else{
                remain--;
            }

        }
    }
    /**
     * Satisfies:
     *  4.1.A: In every space, there is at least one value
     * Iterates through row, column, value to create a line which ensures that there is at least one value in each space
     */
    public void constraint4A(){
        for(int row = 1; row < 10; row++){
            for(int col = 1; col < 10; col++){
                for(int val = 1; val < 10; val++){
                    sat += "" + row + col + val + " ";
                    if(val == 9){
                        sat += "0\n";
                    }
                }
            }
        }
    }

    /**
     * Satisfies:
     *  4.1.B: In every space, there is at most one value
     * Iterates through row, column, value to create a line which ensures that there is only one value in each space
     */
    public void constraint4B(){
        String curr = "";
        String next = "";
        for(int row = 1; row < 10; row++){
            for(int col = 1; col < 10; col++){
                for(int val = 1; val < 10; val++){
                    for(int nxt = (val + 1); nxt < 10; nxt++) {
                        curr = "" + row + col + val;
                        next = "" + row + col + nxt;
                        sat += "-" + curr + " " + "-" + next + " 0\n";
                    }
                }
            }
        }
    }

    /**
     * Adds each preset value in the puzzle to the sat string
     */
    public void puzzleValues(){
        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 9; col++){
                if(puzzle[row][col] != 0) {
                    sat += "" + (row + 1) + (col + 1) + puzzle[row][col] + " 0\n";
                }
            }
        }
    }


    public int[][] getPuzzle(){
        return puzzle;
    }

    public int[] getVars(){
        return vars;
    }

    public String getSat(){
        return sat;
    }

    public void setPuzzle(int[][] in){
        puzzle = in;
    }

    public void setVars(int[] in){
        vars = in;
    }

    public void setSat(String in){
        sat = in;
    }


    public String translate(){
        constraint1A();
        constraint1B();
        constraint2A();
        constraint2B();
        constraint3A();
        constraint3B();
        constraint4A();
        constraint4B();
        puzzleValues();
        return sat;
    }

    /**
     * For testing purposes only
     * TODO: Remove when finished
     * @param args Command line arguments
     */
    public static void main(String[] args){
        Translator t = Translator.getInstance();
        t.constraint3A();
        t.constraint3B();
        System.out.println(t.getSat());
    }
}
