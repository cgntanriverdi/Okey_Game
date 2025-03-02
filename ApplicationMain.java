import java.util.Scanner;

public class ApplicationMain {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OkeyGame game = new OkeyGame();

        System.out.println("Play as human (H) or watch 4 computers (C)? ");
        char mode = sc.next().charAt(0);

        boolean humanPlayer = mode == 'H';

        if ( humanPlayer)
        {
            System.out.print("Please enter your name: ");
            String playerName = sc.next();
            game.setPlayerName(0, playerName);   
        } else 
        {
            game.setPlayerName(0, "Alice");
        }

        game.setPlayerName(1, "John");
        game.setPlayerName(2, "Jane");
        game.setPlayerName(3, "Ted");

        game.createTiles();
        game.shuffleTiles();
        game.distributeTilesToPlayers();

        // developer mode is used for seeing the computer players hands, to be used for
        // debugging
        System.out.print("Play in developer's mode with other player's tiles visible? (Y/N): ");
        char devMode = sc.next().charAt(0);
        boolean devModeOn = devMode == 'Y';

        boolean firstTurn = true;
        boolean gameContinues = true;

        while (gameContinues) {

            int currentPlayer = game.getCurrentPlayerIndex();
            System.out.println(game.getCurrentPlayerName() + "'s turn.");

            if (humanPlayer && currentPlayer == 0) {
                // this is the human player's turn
                game.displayCurrentPlayersTiles();
                game.displayDiscardInformation();

                System.out.println("What will you do?");
                int playerChoice;

                if (!firstTurn) 
                {
                    // after the first turn, player may pick from tile stack or last player's
                    // discard
                    System.out.println("1. Pick From Tiles");
                    System.out.println("2. Pick From Discard");
                    System.out.println("Your choice: ");

                    // to check whether player choice is correct and act accordingly.
                    playerChoice = isChoiceValid(sc, 1, 2);

                    if (playerChoice == 1)
                    {
                        String pickedTile = game.getTopTile();
                        
                        if (pickedTile == null)
                        {
                            System.out.println("No tile picked: stack is empty");
                        } else 
                        {
                            System.out.println("You picked up: " + pickedTile);
                        }
                        game.displayCurrentPlayersTiles();
                    } else if ( playerChoice == 2)
                    {
                        String pickedTile = game.getLastDiscardedTile();
                        
                        if ( pickedTile == null)
                        {
                            System.out.println("You picked nothing discard pile is empty!");
                            game.displayCurrentPlayersTiles();
                        } else
                        {
                            System.out.println("You picked up: " + pickedTile);
                            game.displayCurrentPlayersTiles();
                        }
                    }

                    if (game.didGameFinish())
                    {
                        getWinnerorTie(game);
                        gameContinues = false;
                        break;
                    }

                } else {
                    // on first turn the starting player does not pick up new tile
                    System.out.println("1. Discard Tile");
                    System.out.println("Your choice: ");
                    playerChoice = isChoiceValid(sc, 1, 1);
                    firstTurn = false;

                }

                if (gameContinues) 
                {
                    // if game continues we need to discard a tile using the given index by the
                    // player
                    System.out.println("Which tile you will discard?");
                    System.out.print("Discard the tile in index: ");
                    playerChoice = isChoiceValid(sc, 0, game.players[0].numberOfTiles - 1);

                    // TODO: make sure the given index is correct, should be 0 <= index <= 14
                    // fixed this issue by the helper method isChoiceValid @author : Ali Çağan Tanrıverdi | Date: 02.03.2025

                    game.discardTile(playerChoice);
                    game.passTurnToNextPlayer();
                } 
            } else 
            {
                // this is the computer player's turn
                if (devModeOn) {
                    game.displayCurrentPlayersTiles();
                }

                // computer picks a tile from tile stack or other player's discard
                game.pickTileForComputer();

                if (game.didGameFinish()) 
                {
                    getWinnerorTie(game);
                    gameContinues = false;
                    break;
                } 

                if (gameContinues)
                {
                    game.discardTileForComputer();
                    game.passTurnToNextPlayer();
                }
            }
        }
        sc.close();
    }

    /* 
     * checks whether the input is valid by comparing avaliable opitons
     * @author : Ali Çağan Tanrıverdi
     * Date : 02.03.2025
    */
    private static int isChoiceValid(Scanner sc, int min , int max)
    {
        int choice;

        // keep prompting the user to input a valid choice 
        while (!sc.hasNextInt() || (choice = sc.nextInt()) < min || choice > max)
        {
            System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + " : " );
            sc.nextLine(); // clearing the invalid output
        }
        sc.nextLine(); // clearing line
        return choice;
    }

    /*
     * 
     * handles the end game logic
     * @author : Ai Çağan Tanrıverdi
     * Date : 02.03.2025
     */
    private static boolean getWinnerorTie(OkeyGame game)
    {
        if (game.players[game.getCurrentPlayerIndex()].isWinningHand())
        {
            System.out.println(game.getCurrentPlayerName() + "wins!");
            return true;
        } else
        {
            System.out.println("The tile stack is empty!");
            boolean anyWinner = false;

            for ( Player aPlayer : game.players)
            {
                if (aPlayer.isWinningHand())
                {
                    System.out.println(aPlayer.getName() + " wins!");
                    anyWinner = true;
                    break;
                }
            }

            if (!anyWinner)
            {
                System.out.println("Game ends in a tie...");
            }
        }
        return true;
    }
}