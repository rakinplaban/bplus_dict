import java.util.*;
public class Main {
    public static void main(String[] args) {
        BPlusTree<String, String> dictionary = new BPlusTree<>(5);
        dictionary.insert("apple", "a fruit with a red or green skin and a round shape");
        dictionary.insert("banana", "a long curved fruit which grows in clusters");
        dictionary.insert("carrot", "a long, thin, orange vegetable");

        String definition = dictionary.search("banana");
        System.out.println("The definition of apple is: " + definition);
    }
}