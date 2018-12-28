import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.logging.Logger;

public class Labyrinth {

    private static char[][] mapMatrix;
    private static Point currentPosition;
    private static int rowNumber;
    private static int totalMovements;
    private static Point[] path;
    private static boolean finished;

    public static void main(String[] args) {

       readLab("C:\\Users\\Julian\\Projects\\Labyrynth\\map.txt");
       startLab();

       while (finished==false && currentPosition.x > 0){
           move(findBestMovement());
           currentPosition.x--;
       };

       printMap();


    }

    private static void move(Point nextMove) {

        //check if the movement is right or left from my currentPosition
        int steps = nextMove.y - currentPosition.y ;
        if (steps<0) {
            //left movement

            for (int i = nextMove.y; i< currentPosition.y ; i++){
                mapMatrix[currentPosition.x][i] = '.';
            }

        } else {

            if (steps>0) {

                //right movement

                for (int i = currentPosition.y; i < nextMove.y; i++) {
                    mapMatrix[currentPosition.x][i] = '.';
                }
            } else {

                //straight
                mapMatrix[currentPosition.x][currentPosition.y] = '.';
            }
        }
        //mapMatrix[currentPosition.x-1][nextMove.y] = '.';
        String printLine = new String(mapMatrix[currentPosition.x-1]);
        //System.out.println(printLine);

    }
    private static void startLab() throws  InputMismatchException {


      char[] firstLine = mapMatrix[rowNumber];
      String firstLineStr = new String(firstLine);
      int colNumber = firstLineStr.indexOf('I');

      if (colNumber != -1){
          currentPosition = new Point(rowNumber, colNumber);
          totalMovements = 0;
          finished = false;
         //System.out.println("Movement number: "+ totalMovements +  " | Current currentPosition is: "+currentPosition.toString());
      } else {
          throw new InputMismatchException("Map does not contain starting point represented by letter I");
      }
    }

    private static void printMap() {

        System.out.println("============== Lab finished Map ====================");
        for (char[] matrix : mapMatrix) {
            String line = new String(matrix);
            System.out.println(matrix);
        }
        System.out.println("============== Lab finished Map ====================");
    }

    private static Point findBestMovement() {

        //get row above my current currentPosition
        int nextRow = currentPosition.x-1;
        char[] nextRowChars = mapMatrix[nextRow];
        int minSteps = nextRowChars.length;
        int currentCharPosition = 0;
        Point nextMovement = new Point(currentPosition.x-1, currentPosition.y);
        boolean foundMovement = false;

        for (char currentChar : nextRowChars){

            //stepsNeeded to get to currentChar being analyzed
            int stepsNeeded = Math.abs(currentCharPosition- currentPosition.y);

            if (currentChar == 'E'){
                //exit found!
                finished = true;
                minSteps = stepsNeeded;
                nextMovement.y = currentCharPosition;
                foundMovement = true;
                break;
            } else {
                if (currentChar != '_') {

                    //if not wall

                    if (currentChar == ' ' && (stepsNeeded < minSteps) || minSteps == 0) {
                        //if found a way through and it's the closest to my currentPosition...
                        minSteps = stepsNeeded;
                        nextMovement.y = currentCharPosition;
                        foundMovement = true;
                    } else {

                        int penalty = Character.getNumericValue(currentChar);
                        //Does the path through the penalty takes less than minSteps?
                        if (penalty + stepsNeeded < minSteps) {
                            minSteps = penalty + stepsNeeded;
                            nextMovement.y = currentCharPosition;
                            foundMovement = true;
                        }
                    }
                }
            }
            currentCharPosition++;
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

            System.out.println("============== Original Map ====================");
            while ((currentRow = br.readLine()) != null) {

                System.out.println(currentRow);
                mapMatrix[i] = currentRow.toCharArray();
                i++;
            }
            System.out.println("============== Original Map ====================");

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
