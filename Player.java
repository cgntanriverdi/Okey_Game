/*
    Author of ToDos in this class: Kaan Uz - 22402376
*/
public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the
                           // game
    }

    /*
     * TODO: removes and returns the tile in given index
     */
    public Tile getAndRemoveTile(int index) {
        if (index < 0 || index >= this.numberOfTiles) {
            return null;
        }
        Tile willBeRemoved = this.playerTiles[index];

        for (int i = index; i < this.numberOfTiles - 1; i++) {
            this.playerTiles[i] = this.playerTiles[i + 1];
        }

        this.playerTiles[this.numberOfTiles - 1] = null; // Removing the last reference
        this.numberOfTiles--;

        return willBeRemoved;
    }

    /*
     * TODO: adds the given tile to the playerTiles in order
     * should also update numberOfTiles accordingly.
     * make sure playerTiles are not more than 15 at any time
     */
    public void addTile(Tile t) {
        if (this.numberOfTiles >= 15) {
            System.out.println("Maximum tile limit reached, cannot add a new tile!");
        }
        if (this.numberOfTiles == 0) {
            this.playerTiles[0] = t;
        } else {
            int index = 0;
            while (index < this.numberOfTiles && this.playerTiles[index].compareTo(t) < 0) {
                index++;
            }

            for (int i = this.numberOfTiles; i > index; i--) {
                this.playerTiles[i] = this.playerTiles[i - 1];
            }
            this.playerTiles[index] = t;
        }
        this.numberOfTiles++;
    }

    /*
     * TODO: checks if this player's hand satisfies the winning condition
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     * 
     * @return
     */
    public boolean isWinningHand() {
        int numberOfChains = 0;
        boolean[] used = new boolean[this.numberOfTiles]; // To mark the used tiles
        for (int i = 0; i < this.numberOfTiles; i++) {
            if (used[i] == false) {
                Tile[] chain = new Tile[4];
                chain[0] = this.playerTiles[i];
                int chainSize = 1;
                for (int j = i + 1; j < this.numberOfTiles && chainSize < 4; j++) {
                    if (used[j] == false && this.playerTiles[j].canFormChainWith(chain[chainSize - 1])) {
                        chain[chainSize] = this.playerTiles[j];
                        used[j] = true;
                        chainSize++;
                    }
                }
                if (chainSize == 4) {
                    numberOfChains++;
                    if (numberOfChains == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if (playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
