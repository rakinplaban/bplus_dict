import javax.lang.model.type.NullType;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        BPlusTree<String, String> dictionary = new BPlusTree<>(5);
        Scanner enter = new Scanner(System.in);

        System.out.print("Press 1 to insert a new word.\nPress 2 to find out a word.\nPress 3 to delete a word.\n");
        int option = enter.nextInt();

        switch (option){
            case 1:
                System.out.print("Please insert a word: ");
                String word = enter.next();
                enter.nextLine();
                System.out.print("Now, enter the meaning of the word: ");
                String meaning = enter.nextLine();
                dictionary.insert(word,meaning);
                break;
            case 2:
                System.out.print("Enter a word you want to find out: ");
                String key_in = enter.next();
                String definition = dictionary.search(key_in);

                System.out.println("The definition of " + key_in + " is: " + definition);
                
                break;
            case 3:
                System.out.print("Which word you want to delete from dictionary? ");
                String key_dlt = enter.next();
                dictionary.remove(key_dlt);

        }

    }
}