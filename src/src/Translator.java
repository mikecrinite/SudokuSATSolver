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
 * When the Sudoku puzzle's SAT reduction is deemed satisfiable, this class will also convert the side-effect solution
 * to a Sudoku puzzle and print the results.
 *
 * @author Michael Crinite
 * @version 0.1 12/11/2016
 */
public class Translator {
    // Fields
    public int[][] puzzle;
    public String sat = "";                     //SAT solver input
    public String[] vars = new String[729];     //Will hold vars from solved sat solution
    public static Translator INSTANCE = null;   //Singleton instance
    public String [] str = new String[729];     //Store vars from 3A for use in 3B
    public int clauses = 11988;
    public String header = "p cnf 999 ";        //Strangely enough, you have to tell the solver that there
                                                //are 999 variables or else it will not display up to 999...

    /**
     * Default constructor for type Translator
     */
    public Translator(){
        //Default constructor
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
     *  1.1.B: At most one cell of row i has the value N
     * Iterates through row, value, column to create a line which ensures that at most one of the spaces in the
     * row contains N
     */
    public void constraint1B(){
        String curr;
        String next;
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
        String curr;
        String next;
        for(int col =1; col < 10; col++){
            for(int val = 1; val < 10; val++){
                for(int row = 1; row < 10; row++){
                    for(int nxt = (row + 1); nxt < 10; nxt++) {
                        curr = "" + row + col + val;
                        next = "" + nxt + col + val;
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
     *  4.1: In every space, there is at least one value
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
     *  4.2: In every space, there is at most one value
     * Iterates through row, column, value to create a line which ensures that there is only one value in each space
     */
    public void constraint4B(){
        String curr;
        String next;
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
                    clauses++;
                }
            }
        }
    }

    /**
     * Returns the current state of puzzle
     *
     * @return The input Sudoku puzzle or possibly the solved Sudoku puzzle.
     */
    public int[][] getPuzzle(){
        return puzzle;
    }

    /**
     * Returns the String which the SAT solver will utilize to solve the puzzle
     * @return The SAT condition string
     */
    public String getSat(){
        return sat;
    }

    /**
     * Sets the puzzle field to the paramater input
     * @param in The puzzle to set.
     */
    public void setPuzzle(int[][] in){
        puzzle = in;
    }

    /**
     * Sets the SAT field to the inputted string.
     * @param in
     */
    public void setSat(String in){
        sat = in;
    }

    /**
     * Runs each of the 8 (4 2-part) constraints to generate the SAT clauses that will appear in every puzzle.
     * Then runs puzzleValues which will create clauses specific to the inputted puzzle
     * Finally, consolidates all this information in the sat string to pass to the SAT-solver
     *
     * @return The final state of sat before it is passed to the solver.
     */
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
        header += clauses + "\n";
        sat = header + sat;
        return sat;
    }

    /**
     * Converts the solution passed by the SAT-solver back to a solution which represents a Sudoku puzzle
     *
     * @param solution The boolean satisfiability solution returned by the solver
     * @return The Sudoku solution corresponding to the SAT solution.
     */
    public int[][] convertSolution(String solution){
        vars = solution.split(" ");
        String s;
        int row, col, val;
        for(int i = 0; i < 729; i++){ //With indexes containing "0", the index of var 729 is 503
            s = vars[i];

            if (!s.startsWith("-")) {
                row = Integer.parseInt(s.substring(0, 1)) - 1;
                col = Integer.parseInt(s.substring(1, 2)) - 1;
                val = Integer.parseInt(s.substring(2));
                puzzle[row][col] = val;
            }
        }
        return puzzle;
    }

    /**
     * Prints a single row to console
     *
     * @param row The index of the row which is to be printed.
     */
    private void printRow(int row){
        for(int i : puzzle[row]){
            System.out.print(i + " ");
        }
    }

    /**
     * Prints the entire puzzle to the console by row using printRow
     */
    public void printAllRows(){
        for(int i = 0; i < 9; i++){
            printRow(i);
            System.out.println();
        }
    }
}
