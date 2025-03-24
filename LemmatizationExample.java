import org.tartarus.snowball.ext.englishStemmer;
import java.util.*;
import java.io.*;

public class LemmatizationExample {
    private static final Map<String, String> irregularWords = new HashMap<>();

    // Function to load irregular words from a file
    public static void loadIrregularWords(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    irregularWords.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading irregular words file: " + e.getMessage());
        }
    }

    // Function to remove punctuation
    public static String removePunctuation(String text) {
        return text.replaceAll("[^a-zA-Z0-9\\s]", ""); // Remove non-alphanumeric characters
    }

    // Function to handle irregular words
    public static String handleIrregularWords(String word) {
        return irregularWords.getOrDefault(word, word);
    }

    public static void main(String[] args) {
        // Load irregular words from file
        loadIrregularWords("irregular_words.txt");

        // Read input text from file
        StringBuilder inputText = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputText.append(line).append(" ");
            }
        } catch (IOException e) {
            System.err.println("Error reading input text file: " + e.getMessage());
        }

        // Clean and tokenize text
        String cleanedText = removePunctuation(inputText.toString());
        String[] words = cleanedText.split("\\s+");

        // Initialize the Snowball stemmer for English
        englishStemmer stemmer = new englishStemmer();

        // Process each word
        System.out.println("Lemmatized Output:");
        for (String word : words) {
            // Handle irregular words first
            String processedWord = handleIrregularWords(word.toLowerCase());

            // If the word was not found in the dictionary, use Snowball
            if (!irregularWords.containsKey(word.toLowerCase())) {
                stemmer.setCurrent(processedWord);
                stemmer.stem();
                processedWord = stemmer.getCurrent();
            }

            // Print the result
            System.out.println(word + " -> " + processedWord);
        }
    }
}

