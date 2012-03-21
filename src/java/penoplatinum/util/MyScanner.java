package penoplatinum.util;

import java.util.StringTokenizer;

/**
 * This is a basic implementation to interpret strings.
 * Made to replace the unsupported Scanner on Lejos
 * @author Team Platinum
 */
public class MyScanner {

  private final StringTokenizer tokenizer;

  public MyScanner(String msg) {
    msg = replace(msg, ',', ' ');
    this.tokenizer = new StringTokenizer(msg, " ");
  }

  public String replace(String s, char oldChar, char newChar) {
    char[] characters = s.toCharArray();

    for (int i = 0; i < characters.length; i++) {
      if (characters[i] == ',') {
        characters[i] = ' ';
      }
    }
    return new String(characters);
  }

  public String next() {
    return this.tokenizer.nextToken();
  }

  public boolean hasNext() {
    return this.tokenizer.hasMoreTokens();
  }

  public int nextInt() {
    return Integer.parseInt(next());
  }
}
