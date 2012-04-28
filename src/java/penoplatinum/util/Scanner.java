package penoplatinum.util;

/**
 * This is a basic implementation to interpret strings.
 * Made to replace the unsupported Scanner on Lejos
 * By default spaces and colons are threathed as delimiters.
 * 
 * @author Team Platinum
 */

import java.util.StringTokenizer;


public class Scanner {

  private final StringTokenizer tokenizer;

  public Scanner(String msg) {
    msg = this.replace(msg, ',', ' ');
    this.tokenizer = new StringTokenizer(msg, " ");
  }

  public boolean hasNext() {
    return this.tokenizer.hasMoreTokens();
  }

  public String next() {
    return this.tokenizer.nextToken();
  }

  public int nextInt() {
    return Integer.parseInt(this.next());
  }

  private String replace(String s, char oldChar, char newChar) {
    char[] characters = s.toCharArray();

    for(int i = 0; i < characters.length; i++) {
      if(characters[i] == oldChar) {
        characters[i] = newChar;
      }
    }
    return new String(characters);
  }
}
