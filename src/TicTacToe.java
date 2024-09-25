import java.util.*;

public class TicTacToe {
    private String[][] board;  //het speelbord
    private int size;  //grootte van het bord

    // constructor
    public TicTacToe(int size){
        this.size = size;
        this.board = new String[size][size];

        for (int i = 0; i < size; i ++){
            for (int j = 0; j < size; j++){
                board[i][j] = " ";
            }
        }
    }

    // methode voor printen van het board
    public void printBoard(){
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < size; i++){
            display.append((char) ('A' + i)).append(" ");
            for (int j = 0; j < size; j++){
                display.append("|").append(board[i][j]);
            }
            display.append("|\n");
        }
        display.append("  ").append("-".repeat(size * 2 + 1)).append("\n  ");
        for (int m = 1; m <= size; m++){
            display.append(" ").append(m);  //kolomnummers toevoegen

        }
        System.out.println(display);
    }

    // Methode om te checken of een positie geldig is
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    // Methode om een string zoals "A1" naar rijen en kolommen te converteren
    public int[] getPosition(String move) {
        int row = move.charAt(0) - 'A';
        int col = Integer.parseInt(move.substring(1)) - 1;
        return new int[] {row, col};
    }

    // Methode om een positie (rij en kolom) om te zetten naar een string zoals "A1"
    public String getMove(int[] position){
        int row = position[0];
        int col = position[1];

        char rowChar = (char) ('A' + row);
        int colNumber = col + 1;

        return "" + rowChar + colNumber;
    }


    // functie checkt of het speelbord vol is
    public boolean isFull(){
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (board[i][j].equals(" ")){
                    return false;
                }
            }
        }
        return true;
    }

    //een set toevoegen
    public boolean AddMove(String move, String player){
        int[] pos = getPosition(move);
        int row = pos[0];
        int col = pos[1];

        if (isValidPosition(row, col) && board[row][col].equals(" ")){
            board[row][col] = player;  // zet toevoegen
            return true;
        }
        return false; // ongeldige zet
    }

    // een set verwijderen
    public boolean delMove(String move){
        int[] pos = getPosition(move);
        int row = pos[0];
        int col = pos[1];

        if (isValidPosition(row, col) && !board[row][col].equals(" ")){
            board[row][col] = " ";
            return true;
        }
        return false; // ongeldige zet om te verwijderen
    }

    // Kijken of een zet is om te winnen
    public List<String> moveToWin(String player){
        List<String> winlist = new ArrayList<>();

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (board[i][j].equals(" ")){
                    String move = getMove(new int[]{i,j});
                    AddMove(move, player);
                    if (checkWin(player)){
                        winlist.add(move);
                        delMove(move);
                    }
                    delMove(move);
                }
            }
        }
        return winlist;
    }

    // functie voor de ai move
    public boolean AiMove(String player) {
        List<String> winlist = moveToWin(player); //verkrijg de winnende zetten
        if (!winlist.isEmpty()){
            String winningMove = winlist.getFirst();
            AddMove(winningMove, player);  // maak de winnende zet
        }

        // Lijst van lege posities verzamelen
        List<String> legePosities = new ArrayList<>();
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (board[i][j].equals(" ")){
                    // De rijletter en kolomnummer combineren (bijv. A1, B2)
                    String positie = (char) ('A' + i) + Integer.toString(j + 1);
                    legePosities.add(positie);
                }
            }
        }

        // Controleer of er geen lege posities zijn (spel zou klaar moeten zijn)
        if (legePosities.isEmpty()){
            return false;
        }

        // Willekeurige zet maken uit de lege posities
        Random random = new Random();
        String randomkeuze = legePosities.get(random.nextInt(legePosities.size()));

        // Vertraging toevoegen voordat de computer een zet maakt
        System.out.println("Computer denkt na...");
        try {
            Thread.sleep(1000); // 2 seconden wachten
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // interupt flag resetten als er een onderbreking is
            System.out.println("Fout: De vertraging is onderbroken.");
        }


        // Voeg de willekeurige zet toe aan het bord
        AddMove(randomkeuze, player);

        System.out.println("Computer kiest: " + randomkeuze);
        printBoard();
        return true;
    }

    public boolean checkWin(String player){
        // rijen controleren
        for (String[] row : board) {
            if (row[0].equals(player) && row[1].equals(player) && row[2].equals(player)) {
                return true;
            }
        }

        // kolommen controleren
        for (int j = 0; j < board.length; j++ ){
            if (board[0][j].equals(player) && board[1][j].equals(player) && board[2][j].equals(player)){
                return true;
            }
        }

        // diagonalen controleren
        if (board[0][0].equals(player) && board[1][1].equals(player) && board[2][2].equals(player)){
            return true;
        }
        if (board[2][0].equals(player) && board[1][1].equals(player) && board[0][2].equals(player)) {
            return true;
        }
        return false; // geen winnaar
    }


    public static void main(String[] args){
        System.out.println("Welkom bij TicTacToe\n");
        TicTacToe game = new TicTacToe(3);
        game.printBoard();
        Scanner scan = new Scanner(System.in);

        //vraag de speler om X of O te kiezen
        String player;
        while (true){
            System.out.println("Kies X of O");
            player = scan.next().toUpperCase();
            if (player.equals("X") || player.equals("O")){
                break;
            } else{
                System.out.println("Ongeldige keuze, probeer opnieuw.");
            }
        }

        String opponent = player.equals("X") ? "O" : "X"; // bepaal symbool van de tegenstander

        while (true){
            System.out.println("Maak uw keuze (bijv. A1): ");
            String keuze = scan.next().toUpperCase();

            // lijst met geldige zetten
            List<String> geldigeZetten = Arrays.asList("A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3");

            while (!geldigeZetten.contains(keuze)){
                System.out.println("Ongeldige keuze, probeer nogmaals");
                keuze = scan.next().toUpperCase();
            }

            if (game.AddMove(keuze, player)){
                game.printBoard();
                if (game.checkWin(player)){
                    System.out.println("Jij hebt gewonnen!\n");
                    break;
                }

            } else {
                System.out.println("Ongeldige zet, probeer nogmaals.");
            }
            if (game.isFull()){
                System.out.println("Het is een gelijkspel!");
                break;
            }
           game.AiMove(opponent);
            if (game.checkWin(opponent)){
                System.out.println("Computer heeft Heeft gewonnen!" );
                break;
            }

        }
    }
}
