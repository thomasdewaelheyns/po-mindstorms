/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import java.util.StringTokenizer;

/**
 *
 * @author MHGameWork
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
