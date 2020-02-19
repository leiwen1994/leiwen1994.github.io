import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;


public class MarkovText {

  /** You are supplied with text in the form of a list of words. You are guaranteed
   * that the first two elements in the list are the String "<START>", and the last 
   * element of the list is the String "<END>". You want to update the map whatComesNext
   * with information about which words come after each pair of words.
   * 
   * For each sequence of three adjacent words, do the following:
   * <ul>
   * <li> Create a sublist of the first two words. That will be your key for the map.
   * <lI> If there isn't already a value for that key, you will want to put an empty list
   * as the value for that key (just like with BuildIndex, where you'd put an empty Set as the value).
   * <li> Take the value for that key, and add the third word to the end of the list.
   * </ul>
   */
  static void learnFromText(Map<List<String>, List<String>> whatComesNext, List<String> text) {
	  for(int i = 0; i < text.size() - 2; i++){
			List<String> sublist = text.subList(i, i+2);
			if(!whatComesNext.containsKey(sublist)){
				whatComesNext.put(sublist, new ArrayList<String>());
			}
			whatComesNext.get(sublist).add(text.get(i+2));
		}
  }
  
  
  /** Return a word list generated by using the information in whatComesNext. 
   * The result should always start with the two occurrences of "<START>" and
   * end with an occurrence of "<END>". 
   * 
   * Then repeat the following process:
   * <ul>
   *   <li> Use a sublist of the last two words in the result to get a list
   *     of possible next words from the map.
   *   <li> Use the supplied random number generate to select one of them.
   *   <li> append that word to the result list
   *   <li> If the word is "<END>", you are done, return the result list
   *   <li> Otherwise, go to the top of the loop and repeat
   *   </ul>
   */
  static ArrayList<String> generateText(Map<List<String>, List<String>> whatComesNext, Random r) {
	  ArrayList<String> result = new ArrayList<String>();
		
		//Two initial conditions
		result.add("<START>");
		result.add("<START>");
		String selected = "";
		int i = 2;
		
		do{
			List<String> listToLook = result.subList(i - 2, i);
			
			
			List<String> nextWords = whatComesNext.get(listToLook);
			
			selected = nextWords.get(r.nextInt(nextWords.size()));
			result.add(selected);
			i++;
	    
		} while(!selected.equals("<END>"));

		return result;
  }


  /** Learn from 4 Sherlock Holmes short stories, then generate a story */
  public static void main(String args[]) throws IOException {
    Map<List<String>, List<String>> whatComesNext = new LinkedHashMap<>();
    learnFromFile(whatComesNext, new File("ScandalInBohemia.txt"));
    learnFromFile(whatComesNext, new File("adventureOfTheEngineersThumb.txt"));
    learnFromFile(whatComesNext, new File("FiveOrangePips.txt"));
    learnFromFile(whatComesNext, new File("RedHeadedLeague.txt"));
    List<String> result = generateText(whatComesNext, new Random());
    result = excludeStartAndEndMarkers(result);
    for (String w : result)
      printWord(w);

    System.out.println();
  }
  
  
  /**
   *  given a list that starts with two occurrences of "<START>" and
   *  ends with the String "<END>", return the sublist that 
   *  excludes those three elements. 
   */
  static List<String> excludeStartAndEndMarkers(List<String> list) {
    return list.subList(2, list.size()-1);
  }
  
  /** Load the words from a file, and use that to update whatComesNext */
  static void learnFromFile(Map<List<String>, List<String>> whatComesNext, File file) throws IOException {
    List<String> text = new ArrayList<String>();
    text.add("<START>");
    text.add("<START>");
    addToList(text, file);
    text.add("<END>");
    
    learnFromText(whatComesNext, text);
  }

  

  /** Read all the words from a file and add them to a List.
   * Where there is a blank line, add the String "\n" to the list.
   */
  public static void addToList(List<String> text, File f) throws IOException {
    Scanner scanner = new Scanner(f);
    boolean lineBreakHandled = false;
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) {
        if (!lineBreakHandled) {
          lineBreakHandled = true;
          text.add("\n");
        }
      } else {
        text.addAll(Arrays.asList(line.split("\\s")));
        lineBreakHandled = false;
      }

    }
  }
  
  static int lineLength = 0;
  /** print a word out to System.out, word wrapping if word would extend past 80 characters.
   * An empty string is treated as a new line. 
   */
  static void printWord(String word) {
    if (word.equals("\n")) {
      System.out.println();
      lineLength = 0;
    } else if (lineLength + 1 + word.length() > 80) {
      System.out.println();
      lineLength = 0;
    } else {
      System.out.print(" ");
      lineLength = lineLength + 1;
    }
    System.out.print(word);
    lineLength += word.length();

  }

}