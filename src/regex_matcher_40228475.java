import java.io.*;
import java.util.*;

// Trie data stucture to effeicntly sort the words alphabetically
class SortTrie {

  // Trie node
  private class Trie {

    int index = -1;
    Trie children[] = new Trie[52]; // for a-z and A-Z

    Trie() {
      for (int i = 0; i < 52; i++) {
        children[i] = null;
      }
    }
  }

  Trie root;

  public SortTrie() {
    root = new Trie();
  }

  // Function to insert a string in trie
  public void insert(String s, int index) {
    Trie node = root;
    for (int i = 0; i < s.length(); i++) {
      char curC = s.charAt(i);
      //check if it is a capital letter or small letter and store accordingly
      int pos = curC <= 'Z' && curC >= 'A'
        ? 2 * (curC - 'A')
        : 2 * (curC - 'a') + 1;
      if (node.children[pos] == null) node.children[pos] = new Trie();
      node = node.children[pos];
    }
    //store the index of the word in the leaf node
    node.index = index;
  }

  // Function to get the words in sorted order
  public void preorder(
    Trie node,
    List<String> words,
    List<String> matched,
    PatternHelper patternHelper
  ) {
    // if node is null or we have found 3 words then return
    if (node == null || matched.size() == 3) {
      return;
    }

    for (int i = 0; i < 52; i++) {
      if (node.children[i] != null) {
        // if leaf node then print key
        if (node.children[i].index != -1) {
          String word = words.get(node.children[i].index);
          //check if the word matches the pattern and store it in matched list
          if (patternHelper.isWordMatches(word)) {
            matched.add(word);
            //              System.out.println(word);
          }
        }

        preorder(node.children[i], words, matched, patternHelper);
      }
    }
  }
}

class PatternHelper {

  private String pattern = "";
  private int countOfAsterics = 0; // count of * in pattern
  private int n = 0; //length of pattern

  public PatternHelper(String pattern) {
    this.pattern = pattern;
    this.n = pattern.length();
    for (int i = 0; i < n; i++) {
      //count the number of asterics in pattern
      if (pattern.charAt(i) == '*') {
        //                System.out.println(i);
        countOfAsterics++;
      }
    }
  }

  //check if the characters are same or not, ignoring the case
  static boolean isCharSame(char a, char b) {
    return Character.toLowerCase(a) == Character.toLowerCase(b);
  }

  // Function to check if the word matches the pattern
  public boolean isWordMatches(String word) {
    int len = word.length();
    //        System.out.println(len+" "+n+" "+countOfAsterics);

    // if removing all the asterics from with the previous character pattern
    // is greater than the length of word then return false
    if (countOfAsterics != 0 && len < n - countOfAsterics * 2) return false;

    // if there are no asterics in pattern and
    // length of word is not equal to length of pattern then return false
    if (countOfAsterics == 0 && len != n) return false;

    boolean dp[][] = new boolean[len + 1][n + 1];
    dp[0][0] = true;

    for (int i = 2; i <= n; i++) {
      char currentCharOfPattern = pattern.charAt(i - 1); // pattern never starts with * => means pattern is valid
      if (currentCharOfPattern == '*' && dp[0][i - 2]) {
        dp[0][i] = true;
      }
    }

    for (int j = 1; j <= n; j++) {
      for (int i = 1; i <= len; i++) {
        char currentCharOfPattern = pattern.charAt(j - 1);
        char currentCharOfWord = word.charAt(i - 1);
        if (
          currentCharOfPattern == '.' ||
          isCharSame(currentCharOfPattern, currentCharOfWord)
        ) dp[i][j] = dp[i - 1][j - 1]; else if (currentCharOfPattern == '*') { //skip the current character of pattern and word
          char prevCharOfPattern = pattern.charAt(j - 2);
          //if 0 occurences of previous character of pattern then => true
          //OR previous character of pattern matches the current character of word then previous character of pattern is repeated =>true
          //OR previous character of pattern is . then it can be replaced by any character => true
          dp[i][j] =
            dp[i][j - 2] ||
            (
              dp[i - 1][j] &&
              (
                isCharSame(prevCharOfPattern, currentCharOfWord) ||
                prevCharOfPattern == '.'
              )
            );
        }
      }
    }

    return dp[len][n];
  }
}

public class regex_matcher_40228475 {

  // Function to get the matching most 3 words
  static List<String> getMatchingMostThreeStrings(
    List<String> words,
    PatternHelper patternHelper
  ) {
    List<String> matchedStrings = new ArrayList<>();
    SortTrie sortTrie = new SortTrie();

    //insert all the words in trie
    for (int i = 0; i < words.size(); i++) {
      sortTrie.insert(words.get(i), i);
    }

    //get the words in sorted order
    sortTrie.preorder(sortTrie.root, words, matchedStrings, patternHelper);
    // System.out.println("new " + matchedStrings);
    //        words.sort(String::compareTo);
    //
    //        for(String word: words){
    //            if(patternHelper.isWordMatches(word)){
    //                matchedStrings.add(word);
    //            }
    //        }
    return matchedStrings;
  }

  // Function to write the output to file
  public static void writeToFile(String lcs) throws IOException {
    String outputFile = System.getProperty("user.dir") + "\\src\\output.txt";
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
    out.write(lcs);
    out.newLine();
    out.close();
  }

  // Function to get the LCS of 2 strings
  public static String getLCSof2(int[][] dp, int i, int j, String a, String b) {
    if (i == 0 || j == 0) return "";

    //        if(dp[i][j]==(dp[i-1][j-1]+1)){
    if (a.charAt(i - 1) == b.charAt(j - 1)) {
      return getLCSof2(dp, i - 1, j - 1, a, b) + a.charAt(i - 1);
    }
    if (dp[i][j - 1] > dp[i - 1][j]) {
      return getLCSof2(dp, i, j - 1, a, b);
    }
    return getLCSof2(dp, i - 1, j, a, b);
  }

  // Function to get the LCS of 3 strings
  public static String getLCSof3(
    int[][][] dp,
    int i,
    int j,
    int k,
    String a,
    String b,
    String c
  ) {
    if (i == 0 || j == 0 || k == 0) return "";

    if (
      PatternHelper.isCharSame(a.charAt(i - 1), b.charAt(j - 1)) &&
      PatternHelper.isCharSame(a.charAt(i - 1), c.charAt(k - 1))
    ) {
      return getLCSof3(dp, i - 1, j - 1, k - 1, a, b, c) + a.charAt(i - 1);
    }
    if (
      dp[i - 1][j][k] >= dp[i][j - 1][k] && dp[i - 1][j][k] >= dp[i][j][k - 1]
    ) {
      return getLCSof3(dp, i - 1, j, k, a, b, c);
    }
    if (
      dp[i][j - 1][k] >= dp[i - 1][j][k] && dp[i][j - 1][k] >= dp[i][j][k - 1]
    ) {
      return getLCSof3(dp, i, j - 1, k, a, b, c);
    }
    return getLCSof3(dp, i, j, k - 1, a, b, c);
  }

  // Function to compute the LCS of 2 strings
  public static String LCSof2(String a, String b) {
    int n = a.length();
    int m = b.length();
    int dp[][] = new int[n + 1][m + 1];

    for (int i = 0; i <= n; i++) {
      dp[i][0] = 0;
    }
    for (int i = 0; i <= m; i++) {
      dp[0][i] = 0;
    }

    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= m; j++) {
        if (PatternHelper.isCharSame(a.charAt(i - 1), b.charAt(j - 1))) {
          dp[i][j] = 1 + dp[i - 1][j - 1];
        } else {
          dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
        }
      }
    }

    //        for(int i=0;i<=n;i++){
    //            for(int j=0;j<=m;j++){
    //                System.out.print(dp[i][j]+" ");
    //            }
    //            System.out.println();
    //        }

    return getLCSof2(dp, n, m, a, b);
  }

  // Function to compute the LCS of 3 strings
  public static String LCSof3(String a, String b, String c) {
    int n = a.length();
    int m = b.length();
    int o = c.length();
    int dp[][][] = new int[n + 1][m + 1][o + 1];

    for (int i = 0; i <= n; i++) {
      dp[i][0][0] = 0;
    }
    for (int i = 0; i <= m; i++) {
      dp[0][i][0] = 0;
    }
    for (int i = 0; i <= o; i++) {
      dp[0][0][i] = 0;
    }

    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= m; j++) {
        for (int k = 1; k <= o; k++) {
          if (
            PatternHelper.isCharSame(a.charAt(i - 1), b.charAt(j - 1)) &&
            PatternHelper.isCharSame(b.charAt(j - 1), c.charAt(k - 1))
          ) {
            dp[i][j][k] = 1 + dp[i - 1][j - 1][k - 1];
          } else {
            dp[i][j][k] =
              Math.max(
                Math.max(dp[i - 1][j][k], dp[i][j - 1][k]),
                dp[i][j][k - 1]
              );
          }
        }
      }
    }

    return getLCSof3(dp, n, m, o, a, b, c);
  }

  // Function to find the longest common subsequence
  public static String findLongestCommonSubsequence(List<String> matchedWords) {
    switch (matchedWords.size()) {
      case 0:
        return "No Common Sub Sequence";
      case 1:
        return matchedWords.get(0);
      case 2:
        return LCSof2(matchedWords.get(0), matchedWords.get(1));
      case 3:
      default:
        return LCSof3(
          matchedWords.get(0),
          matchedWords.get(1),
          matchedWords.get(2)
        );
    }
  }

  public static void main(String[] args) throws IOException {
    //reading input from file
    String inputURL = System.getProperty("user.dir") + "\\src\\input.txt";
    BufferedReader br = new BufferedReader(new FileReader(inputURL));
    int n = Integer.parseInt(br.readLine().trim());
    List<String> words = new ArrayList<>(n);
    //adding words to list
    for (int i = 0; i < n; i++) {
      words.add(br.readLine().trim());
    }

    String regex = br.readLine().trim();
    br.close();

    PatternHelper patternHelper = new PatternHelper(regex);

    List<String> matchedWords = getMatchingMostThreeStrings(
      words,
      patternHelper
    );
    // System.out.println(matchedWords);
    //        System.out.println(regex+" "+words+" "+n);
    String lcs = findLongestCommonSubsequence(matchedWords);
    // System.out.println(lcs);
    writeToFile(lcs);
  }
}
//4
//AAABAA
//AACBCA
//AAABCA
//DDEEDD
//AA.B.A
//4
//AAABAABBXYAYX
//AABAEX
//AAABCA
//DDEEDD
//AA.*B.*A.X
//6
//Apple
//Maple
//Apply
//Couple
//Pledge
//Please
//..pl.
