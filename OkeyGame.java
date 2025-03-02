import java.util.Random;
public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    private int counter;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[112];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i, 'Y');
                tiles[currentTile++] = new Tile(i, 'B');
                tiles[currentTile++] = new Tile(i, 'R');
                tiles[currentTile++] = new Tile(i, 'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {// Assuming that the tiles array is shuffled, this method assigns the first
                                            // 15 tiles to player[0] and next 14 to player[1] and so on.
        counter = 0; // Counter represents the current index of tile array.
        for (int i = 0; i < players.length; i++) {// Loop through each player.
            for (int j = 0; j < 14; j++) {// Loop through each tile.
                players[i].addTile(tiles[counter]);
                counter++;
            }
            if (i == 0) {// If it is the first player, add another tile.
                players[i].addTile(tiles[counter]);
                counter++;
            }

        }
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we
     * picked
     * 
     *  @author : Ali Çağan Tanrıverdi
     *  Date : 28.02.2025
     */
    public String getLastDiscardedTile() 
    {
        if ( lastDiscardedTile != null )
        {
            players[currentPlayerIndex].addTile(lastDiscardedTile);
            String pickedTile = lastDiscardedTile.toString();
            lastDiscardedTile = null;
            return pickedTile;
        } else
        {
            return null;
        }
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top
     * tile)
     * it should return the toString method of the tile so that we can print what we
     * picked
     * 
     *  @author : Ali Çağan Tanrıverdi
     *  Date : 28.02.2025
     */
    public String getTopTile() 
    {
        
        if (counter < 112) 
        {
            Tile pickedTile = tiles[counter];
            players[currentPlayerIndex].addTile(pickedTile);
            counter++;
            return pickedTile.toString();

        } else 
        {
            return null;
        }
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     * @author: Ali Batu Sarıca
     * Date: 01.03.2025
     */
    public void shuffleTiles() 
    {
        Random random = new Random();
        for(int i = tiles.length - 1; i > 0; i--)
        {
            int j = random.nextInt(i + 1);
            Tile temp = tiles[i];
            tiles[i] = tiles[j];
            tiles[j] = temp;
        }
    }

    /*
     *TODO: check if game still continues, should return true if current player
     * finished the game, use isWinningHand() method of Player to decide
     * @author: Ali Batu Sarıca
     * Date: 01.03.2025
     */
    public boolean didGameFinish() 
    {   
        if ( this.counter == 112 ) {
        return true; 
        }
        return players[currentPlayerIndex].isWinningHand();
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You should consider if the discarded tile is useful for the computer in
     * the current status. Print whether computer picks from tiles or discarded
     * ones.
     */
    public void pickTileForComputer() {
        /*
        * author : Eren Gürbüz
        * Date: 27.02.2025
        */
        boolean canChain = false;
        String playerName = this.players[currentPlayerIndex].getName();
        for ( Tile aTile : this.players[currentPlayerIndex].getTiles()) {
            if  ( aTile!= null && aTile.canFormChainWith(lastDiscardedTile)) {
                canChain = true;
                break;
            }
        }
        if ( canChain ) {
            System.out.println(playerName + " has picked the last discarded tile: " + getLastDiscardedTile());
        }
        else {
            System.out.println(playerName + " has picked the top tile: " + getTopTile());
        }
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * this method should print what tile is discarded since it should be
     * known by other players. You may first discard duplicates and then
     * the single tiles and tiles that contribute to the smallest chains.
     */
    public void discardTileForComputer() {
        /*
        * author : Eren Gürbüz
        * Date: 27.02.2025
        */
        Player current = this.players[currentPlayerIndex];
        Tile [] currentTiles = current.getTiles();
        int index = 0 ;
        int minChain = Integer.MAX_VALUE ;
        Tile discardingTile = currentTiles[index];
        boolean duplicated = false;
        int possibleChainCount = 0 ;
        
        for ( int i = 0 ; i < current.numberOfTiles-1 ; i++) {
            possibleChainCount = 0 ;
            for ( int k = i + 1 ; k < current.numberOfTiles ; k++) {
                if ( currentTiles[k].compareTo(currentTiles[i]) == 0) {
                    duplicated = true;
                    index = i ;
                    discardingTile = currentTiles [index];
                    break;
                }
                else if ( currentTiles[i].canFormChainWith(currentTiles[k])) {
                    possibleChainCount++;
                }
                
            }
            if ( duplicated ) {
                break;
            }
            if ( possibleChainCount < minChain) {
                minChain = possibleChainCount;
                index = i ;
                discardingTile = currentTiles[index];
            } 
        }
        System.out.println(current.getName() + " discarded: " + discardingTile);
        this.lastDiscardedTile = current.getAndRemoveTile(index);
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {// Discards tile for the user player. User's index is 0.
        // getAndRemoveTile removes the tile at index titleIndex and returns that tile,
        // which will be the lastDiscardedTile.
        lastDiscardedTile = players[0].getAndRemoveTile(tileIndex);
    }

    public void displayDiscardInformation() {
        if (lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if (index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
