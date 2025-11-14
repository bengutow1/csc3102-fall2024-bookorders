import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * @author Khalil El-abbassi, Benjamin Gutowski, Robert Ngo, Timothy Posley
 */
public class BookOrder {

    public static void main(String[] args) {
        AVLTree bookOrders = new AVLTree();
        addOrdersFromFile("orders.csv", bookOrders);
        Scanner scanner = new Scanner(System.in);
        String input;
        printMenu();
        System.out.println();

        while (true) {
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("\nEnding program");
                break;

            } else if (input.equalsIgnoreCase("menu")) {
                System.out.println();
                printMenu();

            } else if (input.equals("1")) { //Adding a book order
                System.out.println("\nEnter the order number for the order you'd like to add (Type \"cancel\" to cancel):\n");
                boolean cancelling = false;
                while (true) {
                    if (cancelling) {
                        break;
                    }
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                        System.out.println("\nCancelling command and returning to the menu\n");
                        printMenu();
                        break;
                    }
                    try {
                        int orderID = Integer.parseInt(input);
                        if (orderID < 0) {
                            System.out.println("\nThe order number can't be negative!\n");
                        } else {
                            System.out.println("\nEnter the order's book name (Type \"cancel\" to cancel):\n");
                            while (true) {
                                input = scanner.nextLine();
                                if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                                    System.out.println("\nCancelling command and returning to the menu\n");
                                    printMenu();
                                    cancelling = true;
                                    break;
                                }
                                if (input.equals("")) {
                                    System.out.println("\nThe order name can't be empty!\n");
                                } else {
                                    System.out.println();
                                    bookOrders.addBookOrder(input, orderID);
                                    System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
                                    cancelling = true;
                                    break;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\nYou must enter a number!\n");
                    }
                }

            } else if (input.equals("2")) { //Removing a book order
                System.out.println("\nEnter the order number for the order you'd like to remove (Type \"cancel\" to cancel):\n");
                while (true) {
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                        System.out.println("\nCancelling command and returning to the menu\n");
                        printMenu();
                        break;
                    }
                    try {
                        int orderID = Integer.parseInt(input);
                        System.out.println();
                        bookOrders.removeBookOrder(orderID);
                        System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("\nYou must enter a number!\n");
                    }
                }
            } else if (input.equals("3")) { //Printing order list
                System.out.println();
                bookOrders.printBookOrders();
                System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
            } else if (input.equals("4")) { //Finding order book name
                while (true) {
                    System.out.println("\nEnter the order number to search for (Type \"cancel\" to cancel):\n");
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                        System.out.println("\nCancelling command and returning to the menu\n");
                        printMenu(); // Display the command list for convenience
                        break;
                    }
                    try {
                        int orderID = Integer.parseInt(input);
                        System.out.println();
                        bookOrders.getName(orderID);
                        System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
                        break; // Exit the loop once a valid orderID is processed
                    } catch (NumberFormatException e) {
                        System.out.println("\nYou must enter a number!\n");
                }
            }
            } else if (input.equals("5")) { //Finding oldest order
                System.out.println();
                bookOrders.findOldestBookOrder();
                System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
            } else if (input.equals("6")) { //Finding latest order
                System.out.println();
                bookOrders.findLatestBookOrder();
                System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
            } else {
                System.out.println("\nInvalid command. For a list of commands, type \"menu\"");
            }
            System.out.println();
        }
        scanner.close();
    }

    public static void printMenu() {
        System.out.println("COMMAND LIST:");
        System.out.println("\"menu\" - View the command list");
        System.out.println("\"1\" - Manually add a book order");
        System.out.println("\"2\" - Manually remove a book order");
        System.out.println("\"3\" - Print the full list of orders");
        System.out.println("\"4\" - Find the name of a book for a specific order number");
        System.out.println("\"5\" - Find the oldest book order");
        System.out.println("\"6\" - Find the latest book order");
        System.out.println("\nTo execute any of these commands, type the associated number that's in quotations");
        System.out.println("To exit the program, type \"exit\"");

    }

    /*
* This method adds orders from the specified file name into the specified AVL Tree.
* input - fileName (String set to the name of the file. Pathways aren't necessary, but the...
*   	file needs to be located in the same folder as the BookOrder.java class)
* input - tree (AVLTree to modify)
     */
    public static void addOrdersFromFile(String fileName, AVLTree tree) {
        //Checks to see if the file does exist. If not, catches the exception and prints an error message.
        try (InputStream inputStr = AVLTree.class.getResourceAsStream(fileName); BufferedReader br = new BufferedReader(new InputStreamReader(inputStr))) {

            //Extra check for if the try doesn't throw an exception yet the input stream is null.
            if (inputStr == null) {
                System.out.println("File can't be found!\n");
                return;
            }

            /* Goes through each line in the file, checking to see if it follows the format ("order ID, order name").
            	If not, ignores the line and moves on to the next. */
            System.out.println("Reading the file: " + fileName + "\n");
            String line = br.readLine();
            while (line != null) {
                boolean validLineFormat = true;
                int curOrderID = 0;
                String curOrderName;
                String[] values = line.split(",");

                /* Checking if the first value (once the string is split by commas) is an integer.
                		If so, the line has the correct format. */
                try {
                    curOrderID = Integer.parseInt(values[0]);
                } catch (NumberFormatException e) {
                    validLineFormat = false;
                }
                if (validLineFormat) {
                    curOrderName = values[1];

                    //Combines the remaining values of the split String in case the title contains commas.
                    for (int i = 2; i < values.length; i++) {
                        curOrderName = "," + values[i];
                    }

                    //Adds the book order to the AVL Tree
                    tree.addBookOrder(curOrderName, curOrderID);

                }
                line = br.readLine();
            }
            System.out.println("Finished reading the file: " + fileName + "\n");
        } catch (Exception e) {
            System.out.println("File can't be found!\n");
        }
    }

}
