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
        for (int m = 0; m < size; m++){
            display.append(" ").append(m + 1);  //kolomnummers toevoegen

        }
        System.out.println(display);
    }

    // checken of een zet geldig is of niet
    public boolean allowsMove(int col){
        if (!(0 <= col && col < board[0].length)){
            return false;
        }
        if (board[0][col].equals(" ")){
            return true;
        }
        else return false;
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
        int row = move.charAt(0) - 'A';  // zet om naar rijen met gebruik van ascii waardes
        int col = Integer.parseInt(move.substring(1)) - 1;
        if (row >= 0 && row < size && col >= 0 && col < size && board[row][col].equals(" ")){
            board[row][col] = player;  // zet toevoegen
            return true;
        }
        return false; // ongeldige zet
    }

    // functie voor de ai move
    public boolean AiMove(String player) {
        // List<String> keuzes = Arrays.asList("A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3");

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

        // Vertraing toevoegen voordat de computer een zet maakt
        System.out.println("Computer denkt na...");
        try {
            Thread.sleep(2000); // 2 seconden wachten
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
        for (int i = 0; i < board.length; i++){
            if (board[i][0].equals(player) && board[i][1].equals(player) && board[i][2].equals(player)){
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
                    System.out.printf("Jij hebt gewonnen!\n", player);
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
