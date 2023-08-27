import java.io.*;
import java.util.*;

class Main {
    public static void main(String[] args) {
        
        System.out.println();
        System.out.println(Colours.RED + "Trivia board game" + Colours.RESET);
        System.out.println("Play the game and learn interesting facts about climate change or street safety.");
        System.out.println("Press enter to roll the dice and move accross the board.");
        System.out.println("But be ready to test your knowledge when you land on the globe!");
        System.out.println("If you answer correctly, you will move 2 steps forward.");
        System.out.println("But if you're not correct, you have to move back.");
        System.out.println("You can choose the topic and the size of your board (e.g. 6x6, 7x7, 10x10).");
        System.out.println(); 
         
        PlayGame.play();

        // when the 🏆 field is reached, the game ends
        System.out.println("congratulations, you have won! 🏆");
        System.out.println("Thank you for playing.");
                 
     } 
  }

class Board {
    
    public static String [][] boardNumbers(int size){
        // for some reason, the size needs to be the same for both variables, otherwise it's raising errors
        int columns = size;
        int rows = size;        
        String[][] board = new String[columns][rows];

        // generate numbers for the board fields
        String squareNum = "";
        int number = 1;
        for (int i = 0; i<rows; i++){
            
            for (int j = 0; j<columns; j++){

                // display trophy instead of the last number e.g. 100 for 10x10 board, 49 for 7x7
                if (i == (size-1) && j == (size-1)){
                    squareNum = "🏆";
                }
                // display Earth to represent 'question on every 3rd square
                else if ((number%3) == (0)){
                    squareNum = "🌍";
                }
                else{
                    squareNum = String.format("%02d",number);
                }
                board[i][j] = squareNum;
                number ++;
            }
        }
        return board;
    }

    public static void showBoard(String[][] board){
        for (int i = 0; i<board.length; i++){
            String row = "";
            String split = "";
            for (int j=0; j<board[i].length; j++){
                row = row+"|"+board[i][j]+"|";
                split+="+--+";                                               
            }
            if (i==0) {
                System.out.println(split);
            }
            System.out.println(row);
            System.out.println(split);
            
        }
    }
}

class Player{
    // functions to display counter & text in different colours for players
    
    public static String player1counter(){
        String counter = Colours.PURPLE_BACKGROUND + "[]" + Colours.RESET;
        return counter;
    }

    public static String player1text(String text){
        String colouredText = Colours.PURPLE + text + Colours.RESET;
        return colouredText;
    }
    
    public static String player2counter(){
        String counter = Colours.GREEN_BACKGROUND + "[]" + Colours.RESET;
        return counter;
    }

    public static String player2text(String text){
        String colouredText = Colours.GREEN + text + Colours.RESET;
        return colouredText;
    }
    
      
}

class Trivia {
    
    public static List<Dictionary<String, String>> readCSVtrivia(String file) {

        // read the CSV file and store in a list of dictionaries
        String line = "";
        String splitBy = ",";
        List<Dictionary<String, String>> triviaQuestions = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                Dictionary<String, String> question = new Hashtable<String, String>();
                String[] item = line.split(splitBy); // use comma separator to separate the fields
                question.put("Question",item[0]);
                question.put("A", item[1]);
                question.put("B", item[2]);
                question.put("C", item[3]);
                question.put("D", item[4]);
                question.put("Answer", item[5]);

                // append each question to the list as a dictionary
                triviaQuestions.add(question);
            }
            br.close();
        }
            
        catch (IOException e) {
            e.printStackTrace();
        }
        return triviaQuestions;
    }

    public static List<String> readCSVfacts(String file) {

        // read the CSV file and store in a list, no dictionary needed, as it's only 1 column
        String line = "";        
        List<String> facts = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String fact = line;
                
                // append each fact to the list of interesting facts
                facts.add(fact);
            }
            br.close();
        }
            
        catch (IOException e) {
            e.printStackTrace();
        }
        return facts;
    }
    
    public static int getRandomNumber(List triviaQuestions){
        // get random number between 1 and the number of questions (number of items on the list), 
        // the list has a line 0 which is just a header, so no need to add +1 as it's already +1
        // this function is also used to generate random facts, it was originally written for 
        // generating question number, hence the name
        
        Random rand = new Random();        
        int questionNumber = rand.ints(1, (triviaQuestions.size())).findFirst().getAsInt();
        
        return questionNumber;
    }
                
    public static void printQuestion(int questionNumber, List<Dictionary<String, String>> triviaQuestions){ 
        // print trivia question that is stored under the random number
        System.out.println(Colours.YELLOW+"Question "+questionNumber+": " + Colours.RESET + triviaQuestions.get(questionNumber).get("Question"));
        System.out.println("A: " + triviaQuestions.get(questionNumber).get("A"));
        System.out.println("B: " + triviaQuestions.get(questionNumber).get("B"));
        System.out.println("C: " + triviaQuestions.get(questionNumber).get("C"));
        System.out.println("D: " + triviaQuestions.get(questionNumber).get("D"));
    }

    
    public static String getAnswer (){
        //  ask user for answer
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your choice (a/b/c/d): ");
        String userAnswer = input.nextLine();
        return userAnswer;
    }
            
    public static int scoreAnswer (String userAnswer, List<Dictionary<String, String>> triviaQuestions, int questionNumber){
        // check if the answer is correct and notify the user of the result, return score 1 if correct, 0 if incorrect
        int score = 0;
        if (userAnswer.strip().toUpperCase().equals(triviaQuestions.get(questionNumber).get("Answer").toUpperCase())) {
            System.out.println("Correct!");
            // update score, if correct answer given
            score +=1;
            return score;
        } 
        else {
            System.out.println("Incorrect. The correct answer is " + triviaQuestions.get(questionNumber).get("Answer")+
                              ": " + triviaQuestions.get(questionNumber).get(triviaQuestions.get(questionNumber).get("Answer")));
            return score;
        }
    }
}

class Move{

    public static int rolledNumber(){
        // to simulate the dice roll, randomly generate a number between 1 and 6
        Random rand = new Random();
        int rolled = rand.ints(1,7).findFirst().getAsInt();
        return rolled;
    }
    
    public static int diceRoll(){
        // ask user to press enter to 'roll' the dice
        Scanner input = new Scanner(System.in);
        System.out.println("please press enter to roll the dice 🎲");
        String roll = input.nextLine();
       
        int rolledNum = Move.rolledNumber();
        
        if (roll != null) {
                        
            System.out.println("You have rolled number " + rolledNum + " 🎲");
        }
        return rolledNum;    
    }

    public static int[] getCoordinates(int i, int j, int numberRolled, int size){
        // calculate coordinates where the counter will be placed after rolling dice/moving 2 steps forward or back
        j = j + numberRolled;
        // if j would end up being out of index range for the line, move to the next line/previous line
        // next line
        if (j > (size-1)) {
            j = j - size; // this needs to be -size, not -(size-1) because the indexing starts at 0, not 1
            i ++;
        }
        // previous line (for incorrect answers)
        else if (j < 0){
            i--;
            j = size + j;            
        }
         
        // if the last possible square would be exceeded, place i and j on the last square
        if ((i > (size-1))||(i == (size-1) && j > (size-1))){
            i = (size-1); 
            j = (size-1); 
        }
        int[] coordinates = new int[2];
        coordinates[0] = i;
        coordinates[1] = j;
        return coordinates;
    }

    public static int[] coordinatesOccupied(int i, int j, String[][] board, String[][] originalBoard, int size){
        // if the place is already occupied, move to the next spot, 
        if (!board[i][j].equals(originalBoard[i][j])){                
                j = j + 1;
                // if it would place the counter out of bounds, move to the next line
                if (j > (size-1)){
                            j = j - size;
                            i++;
                        }
            }   
        
        int [] occupiedCoordinates = new int[2];
        occupiedCoordinates[0] = i;
        occupiedCoordinates[1] = j;
        return occupiedCoordinates;
    }

    public static int[] makeMove(int i, int j, int rolled, int size, String[][] board, 
                                 String[][] originalBoard, int player){
        // function to move the game piece on the board and return new coordinates (after move)
        // get new coordinates
    	int [] coord = new int[2];
    	coord = Move.getCoordinates(i, j, rolled, size);            
        i = coord[0];
        j = coord[1];

        // check if the field is already occupied, and update coordinates to the next field
	    int[] occupiedCoord = new int[2];
        occupiedCoord = Move.coordinatesOccupied(i, j, board, originalBoard, size);
        i = occupiedCoord[0];
        j = occupiedCoord[1];

        // move as many spaces as you rolled, displaying [] instead of the number
        // Player 1
		if (player == 1){
            board[i][j] = Player.player1counter();
		}
        //Player 2
		else{
		 board[i][j] = Player.player2counter();
		}
                  
        // print the board again
        Board.showBoard(board);

        // return coordinates after move
    	int [] afterMove = new int[2];
    	afterMove[0] = i;
    	afterMove[1] = j;
        return afterMove;       
    }

    public static int[] question(int i, int j, String[][] board, String[][] originalBoard, int size, int player, List<Dictionary<String, String>> triviaQuestions) {
        // if counter lands on the earth field, display trivia questions and move (based on the answer)

        // initialise variable for coordinates that will be returned from the function after the move
        int [] afterMove = new int[2];
        afterMove[0] = i;
        afterMove[1] = j;
        
        while (originalBoard[i][j].equals("🌍")){
            // check if Player 1 or Player 2 has landed on the globe field and notify them
            // it is Question time
            if (player == 1){
                System.out.print(Player.player1text("Player 1"));
            }
    
            else{
                System.out.print(Player.player2text("Player 2"));
            }
            
            System.out.println(Colours.YELLOW+", it is Question time!"+Colours.RESET);
            System.out.println("");
            
            // run the trivia code, if answer correct, move forward 2 spots, if incorrect, move back 2 spots
            // unless the field is occupied, then move 1 step back and 3 forward
            int questionNumber = Trivia.getRandomNumber(triviaQuestions);
            Trivia.printQuestion(questionNumber, triviaQuestions);
            String userAnswer = Trivia.getAnswer();
            System.out.println();
            int score = Trivia.scoreAnswer(userAnswer, triviaQuestions, questionNumber);
                            
            // if the answer is correct, move forward 2 places
            if (score == 1) {
                System.out.println("You have moved forward 2 places 😊");
                        
                // re-set the current field to original
                board[i][j] = originalBoard[i][j];
                        
                // make a move and update coordinates after move
                // number 2 is passed insead of var rolled, as they are moving 2 steps forward
                afterMove = Move.makeMove(i, j, 2, size, board, originalBoard, player);
                i = afterMove[0];
                j = afterMove[1];
            }
    
            // move back 2 places for incorrect answers
            else{
                System.out.println("You have moved back 2 places  😞");
                // re-set the current field to original
                board[i][j] = originalBoard[i][j];
                                        
                // make a move and update coordinates after move
                // number -2 passed insead of var rolled, as they are moving 2 steps back
                afterMove = Move.makeMove(i, j, -2, size, board, originalBoard, player);
                i = afterMove[0];
                j = afterMove[1];
            }
        
        }
        
        return afterMove;
    }
    
}

class PlayGame {

    public static int numOfPlayers(){
        // ask user how many players wish to play, only accept expected number (1, 2)
        // and keep asking until the user enters valid number, ignore white spaces
        int numPlayers = 0;
        Scanner input = new Scanner(System.in);

        while(true){
            try{
                System.out.println("Please enter number of players (1 or 2).");
                numPlayers = Integer.parseInt(input.nextLine().strip()); // this converts the string to integer
                if (numPlayers == 1 || numPlayers == 2){
                    break;
                }
                else{
                    System.out.println("Invalid entry");
                    continue;
                }
            }
            catch (Exception e){
                System.out.println("Invalid entry.");
            }
        }
                
        // show which colours players will be playing with
        // Player 1
        System.out.print(Player.player1text("Player 1"));
        System.out.print("*");
        System.out.print(Player.player1counter());
        System.out.println("*");
            
            if (numPlayers == 2){
                // Player 2 (if 2 players selected)
                System.out.print(Player.player2text("Player 2"));
                System.out.print("*");
                System.out.print(Player.player2counter());
                System.out.println("*");                
            }
        
        return numPlayers;
    }

    public static int boardSize(){
        // To draw the initial board to begin the game, ask user what size they would like it to be
        // if it's not a number between 6 and 10 or if it is any other character, ask user again
        int size = 0;
        Scanner input = new Scanner(System.in);
        
        while (true) {
            try{
                System.out.println("Please enter the size of the board (6-10)");
                size = Integer.parseInt(input.nextLine().strip()); 
                if (size > 5 && size < 11){
                    break;
                }
    
                else{
                    System.out.println("Invalid entry.");
                    continue;
                }                
            }
            catch (Exception e){
                System.out.println("Invalid entry.");
            }
        }
        return size;
        
    }

    public static int chooseGame(){
        // ask user to select what game they want to play (1=Climate change, 2=Street safety)
        // if they enter anything else, keep asking again
        int game = 0;
        Scanner input = new Scanner(System.in);
        
        while (true) {
            try{
                System.out.println("Please select "+Colours.BLUE + "1 for Climate change" + 
                                   Colours.RESET+" or "+Colours.YELLOW + "2 for Street safety." + Colours.RESET);
                game = Integer.parseInt(input.nextLine().strip()); 
                if (game == 1 || game == 2){
                    break;
                }
    
                else{
                    System.out.println("Invalid entry.");
                    continue;
                }                
            }
            catch (Exception e){
                System.out.println("Invalid entry.");
            }
        }
        return game;
    }
    
    public static void play(){ 
        // ask for number of players
        int numOfPlayers = PlayGame.numOfPlayers();
        
        // get board size
        int size = PlayGame.boardSize();

        // ask user to choose game
        int game = PlayGame.chooseGame();

        // display if Climate change or Steet safety option was chosen
        if (game == 1){
            System.out.println();
            System.out.println(Colours.BLUE + "Climate Change Trivia" + Colours.RESET);
        }
        else{
            System.out.println();
            System.out.println(Colours.YELLOW + "Street Safety Trivia" + Colours.RESET);
        }
        

        // draw the initial board
        String[][] board = Board.boardNumbers(size);
        String[][] originalBoard = Board.boardNumbers(size);
        Board.showBoard(board);
              
        // initialise rolled dice as 0, coordinates variable, i and j on the field 'before' number 1 - board[0][-1],        
        // as well as list of all the facts and the trivia questions
        int rolled = 0;
        int player1 = 1;
        int i1 = 0;
        int j1 = -1;
        int player2 = 2;
        int i2 = 0;
        int j2 = -1;        
        int[] afterMove = new int[2];
        
        String fileNameFacts = "";
        String fileNameTrivia = "";

        // load questions based on user's choice
        // for climate change
        if (game == 1) {
            fileNameFacts = "facts_climate.csv";
            fileNameTrivia = "trivia_climate.csv";
        }
            
        // for street safety
        else{
            fileNameFacts = "facts_street.csv";
            fileNameTrivia = "trivia_street.csv";
        }
        List<String> facts = Trivia.readCSVfacts(fileNameFacts);            
        List<Dictionary<String, String>> triviaQuestions = Trivia.readCSVtrivia(fileNameTrivia);
    
        // play the game until the counter lands on the trophy (no need to roll the exact number in the last move
        // it can be equal to or more than the number of remaining fields)
        while (board[size-1][size-1].equals("🏆")){
            // if only 1 player selected
            // display interesting facts
            int factNumber = Trivia.getRandomNumber(facts);
            System.out.println(Colours.YELLOW+"Fact "+ factNumber + ": "+Colours.RESET+facts.get(factNumber));
            System.out.println("");
    
            // Player 1
            //roll the dice
            System.out.print(Player.player1text("Player 1"+ ", "));
            rolled = Move.diceRoll();     
                
            // make a move and update coordinates after move
            afterMove = Move.makeMove(i1, j1, rolled, size, board, originalBoard, player1);
            i1 = afterMove[0];
            j1 = afterMove[1];
                             
            // check if the player landed on a question and re-update coordinates after move
            afterMove = Move.question(i1, j1, board, originalBoard, size, player1, triviaQuestions);
            i1 = afterMove[0];
            j1 = afterMove[1];
    
            // check if player 1 landed on the last field, if yes, break out of the loop to announce the winner
            if (i1 == (size-1) && j1 == (size-1)){
                System.out.print(Player.player1text("Player 1") + ", ");
                break;
            }
            
    
            // if 2 players selected, also run this part of code
            if (numOfPlayers == 2){
                // Player 2
                // re-set the square to the usual number/character for player 2 unless this is the first move
                if (!((i2==0) && (j2==-1))){                
                    board[i2][j2] = originalBoard[i2][j2];                
                }

                // display the fact
                factNumber = Trivia.getRandomNumber(facts);
                System.out.println(Colours.YELLOW+"Fact "+ factNumber + ": "+Colours.RESET+facts.get(factNumber));
                System.out.println("");
                    
                //roll the dice
                System.out.print(Player.player2text("Player 2" + ", "));
                rolled = Move.diceRoll();     
                    
                // make a move and update coordinates after move
                afterMove = Move.makeMove(i2, j2, rolled, size, board, originalBoard, player2);
                i2 = afterMove[0];
                j2 = afterMove[1];
        
                // check if the player landed on a question and re-update coordinates after move
                afterMove = Move.question(i2, j2, board, originalBoard, size, player2, triviaQuestions);
                i2 = afterMove[0];
                j2 = afterMove[1];
                                
                // check if player 2 landed on the last field, if yes, break out of the loop to announce the winner
                if (i2 == (size-1) && j2 == (size-1)){
                    System.out.print(Player.player2text("Player 2") + ", ");
                    break;
                }
                    
            }
            
            // re-set the square to the usual number/character for player 1
            board[i1][j1] = originalBoard[i1][j1];                    
        }            
    }
}      
    
class Colours {

    // all of the below code to change colours in the terminal window
    // was taken from https://www.w3schools.blog/ansi-colors-java
    
    // Reset
    public static final String RESET = "\033[0m";  

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    // Underline
    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

    // Background
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE    

}