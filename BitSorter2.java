import java.util.Random;

/**
*  Lets build and test some bitsorter turing-machine-like computers
*/
public class BitSorter {

    public static final void main(String[] args) {
        BitSorter bs = new BitSorter();
    }
    /**We expect j from 0 to 3 */
    // simply a way to index the states, a serialization
    public boolean getBool(boolean i, int j) {
        if (j==0) return false;
        else if (j==3) return true;
        else if (j==1) {
            if (i) return false; else return true;
        }
        else if (j==2) {
            if (i) return true; else return false;
        } 
        System.out.println("HELP I'm in another bubble of the metverse");
        return false;
    }

    public BitSorter() {
        //lets start with all three-state programs, the 0th being the stopped state
        int counter = 1;
        int currentMax = 0;
        int totalStopped = 0;
        Program p;  
        System.out.println("Starting computer simuations");
        // all state pointing options for first state
        for (int a = 0; a<3; a++) {
            for (int b=0; b<3; b++) {
                
                // all state pointing options for second state 
                for(int c=0; c<3; c++) {
                    for (int d=0; d<3; d++) {
                        
                        // all sort options for first state
                        for (int e=0; e<4; e++) {
                            // all sort options for second state
                            for (int f=0; f<4; f++){

                                // finally time to build a program
                                counter++;
                                SortState s1 = new SortState(getBool(false,e),getBool(true,e),a,b);
                                SortState s2 = new SortState(getBool(false,f),getBool(true,f),c,d);

                                p = new Program(s1,s2);
                                p.currentState = 1;  // start at the state in 1 position..  
                                String weOut = p.run(50);
                                if (!p.overflowed) {

                                    System.out.println("Program: " + a + " " + b + " "
                                     + c + " " + d + " " + e + " " + f + " running " + weOut);

                                    totalStopped ++;

                                     try {
                                        int q = Integer.parseInt(weOut);
                                        if (q>currentMax) currentMax = q;
                                     }
                                     catch (Exception ttt) {ttt.printStackTrace();}
                                }
                                
                            }
                        }
                    }
                }
            }
        }
        System.out.println("max is " + currentMax + " \n");
        System.out.println("total programs tested: " + counter);
        System.out.println("total programs stopped: " + totalStopped);
    }

    /**
    *  A program consists of a collection of sort states which 
    *   may or may not be pointed to by each other, as the next state
    *   to handle the input stream.  
    *
    */
    public class Program {
        public String output;
        public int stateSize; 
        public int currentState; 
        public boolean overflowed; // indicates we didn't stop in time 
        public SortState[] states;
        public InputTape it;  
        
        public Program(int size) {
            stateSize = size;
            states = new SortState[size];
            it = new InputTape();
            output = "";  
        }  

        public Program(SortState s1, SortState s2) {
            overflowed = false;
            stateSize = 3;
            states = new SortState[3];
            SortState stopState = new SortState();
            states[0] = stopState;
            states[1] = s1;
            states[2] = s2;
            it = new InputTape();
            output = "";
        }

        public String run(int depth) {
            if (depth == 0) {
                overflowed=true;
                output+=("MAX");
                return output;
            }
            else if (states[currentState].isStopState) return output;
            else if (!it.next()) {
                if (states[currentState].outputZero) output+=("0");
                currentState = states[currentState].stateOnZero;
            }
            else {
                if (states[currentState].outputOne) output+=("1");
                currentState = states[currentState].stateOnOne;
            }
            return run(depth-1);
        }
    }

    /**
    The bitsorter processes and sorts bits from a random input  
    this is the random bits inputter 
     */
    public class InputTape {
        Random r;
        public InputTape() {
            r=new Random();
        }
        public boolean next() {
            return r.nextBoolean();
        }
    }

    /**
    A given state now at the mouth of the bitsream will make some choices..  
    shall I keep it if it's a zero?  shall i keep it if it's sa one?  
    where shall I go next if it's a zero?  where shall I go next if it's a one? 
     */
    public class SortState {
        public boolean isStopState;
        public boolean outputZero;
        public boolean outputOne;
        public int stateOnZero;
        public int stateOnOne;

        public SortState() {
            isStopState = true;
        }

        public SortState(boolean a, boolean b, int c, int d) {
            outputZero = a;  outputOne = b;  stateOnZero = c;  stateOnOne = d; 
            isStopState = false;
        }
    }
}
