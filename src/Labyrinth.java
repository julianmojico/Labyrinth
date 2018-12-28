import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;

public class Labyrinth {

    private static char[][] mapMatrix;
    private static Point position;
    private static int rowNumber;
    private static int totalMovements;
    private static Point[] path;
    private static boolean finished;

    public static void main(String[] args) {

       readLab("C:\\Users\\Julian\\Projects\\Labyrynth\\mapmio.txt");
       startLab();

       while (finished==false && position.x > 0){
           move(findBestMovement());
           position.x--;
       };

       printMap();


    }

    private static void move(Point nextMove) {

        //check if the movement is right or left from my position
        int steps = nextMove.y - position.y;
        if (steps<0) {
            //left movement

            for (int i=nextMove.y; i<position.y ; i++){
                mapMatrix[position.x][i] = '.';
            }

        } else {

            if (steps>0) {

                //right movement
                for (int i = position.y; i < nextMove.y; i++) {
                    mapMatrix[position.x][i] = '.';
                }
            } else {
                mapMatrix[position.x][position.y] = '.';
            }
        }
        mapMatrix[position.x-1][nextMove.y] = '.';
        String printLine = new String(mapMatrix[position.x-1]);
        //System.out.println(printLine);

    }
    private static void startLab() throws  InputMismatchException {


      char[] firstLine = mapMatrix[rowNumber];
      String firstLineStr = new String(firstLine);
      int colNumber = firstLineStr.indexOf('I');

      if (colNumber != -1){
          position = new Point(rowNumber, colNumber);
          totalMovements = 0;
          finished = false;
         //System.out.println("Movement number: "+ totalMovements +  " | Current position is: "+position.toString());
      } else {
          throw new InputMismatchException("Map does not contain starting point represented by letter I");
      }
    }

    private static void printMap() {

        for (char[] matrix : mapMatrix) {
            String line = new String(matrix);
            System.out.println(matrix);
        }
    }

    private static Point findBestMovement() {

        //get row above my current position
        int nextRow = position.x-1;
        char[] nextRowChars = mapMatrix[nextRow];
        int minSteps = nextRowChars.length;
        int i = 0;
        Point nextMovement = new Point(position.x-1,position.y);
        boolean foundMovement = false;

        for (char currentChar : nextRowChars){

            //stepsNeeded to get to currentChar being analyzed
            int stepsNeeded = Math.abs(i-position.y);

            if (currentChar == 'E'){
                //exit found!
                finished = true;
                minSteps = stepsNeeded;
                nextMovement.y = currentChar;
                foundMovement = true;
            }
            if (currentChar!='_'){

                //if not wall

                if (currentChar==' ' && (stepsNeeded < minSteps ) || minSteps == 0){
                    //if found a way through and it's the closest to my position...
                    minSteps = stepsNeeded;
                    nextMovement.y = i;
                    foundMovement = true;
                } else {

                    //Is the penalty lower?
                    int penalty = new Integer(currentChar);
                    if ( penalty + stepsNeeded < minSteps) {
                        minSteps = penalty + stepsNeeded;
                        nextMovement.y = i;
                        foundMovement = true;
                    }
                }
            }
            i++;
        }

        if (!foundMovement){
            throw new InputMismatchException("Map does not contain any possible path to exit");
        }

        return nextMovement;
    }
    private static void readLab(String filename) {

        BufferedReader br = null;
        FileReader fr = null;

        //Assuming max map size 20x20
        mapMatrix = new char[20][20];
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String currentRow;
            int i=0;
            int j=0;

            while ((currentRow = br.readLine()) != null) {
                System.out.println(currentRow);
                mapMatrix[i] = currentRow.toCharArray();
                i++;
            }

            //assuming the last line in the file always contains starting point
            rowNumber = i-1;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }
}
