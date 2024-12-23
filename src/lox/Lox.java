package lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lox.scanner.Scanner;
import lox.scanner.Token;
import lox.scanner.TokenType;

/**
 * Jlox Interpreter.
 */
public class Lox {
  static boolean hadError = false;

  /**
   * Entry point for the Lox interpreter.
   *
   * <p>
   *   Runs a Lox script from a file if a path is provided, or
   *   starts an interactive REPL if no arguments are provided.
   * </p>
   *
   * @param args command-line arguments: optionally a path to a Lox script
   * @throws IOException if an error occurs while reading input or the script
   */
  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Usage: jlox [script]");
      System.exit(64);
    } else if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

  /**
   * Starts an interactive prompt (REPL) for executing Lox code.
   *
   * <p>
   *   In this mode, users can interact with the interpreter by entering
   *   Lox code line by line. Each line is executed immediately after it
   *   is entered. The prompt ends when the user sends an end-of-file (EOF)
   *   signal or terminates the process.
   * </p>
   *
   * <aside>
   *   REPL stands for Read-Eval-Print-Loop, describing the cycle of reading
   *   input, evaluating it, printing the result, and looping back.
   * </aside>
   *
   * @throws IOException if an error occurs while reading input
   */
  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    for (;;) {
      System.out.print("> ");
      String line = reader.readLine();
      if (line == null) {
        break; // EOF
      }
      run(line);
      hadError = false; // mistake should not kill session
    }
  }

  /**
   * Reads and executes a Lox script from a file.
   *
   * <p>
   *   This method takes the path to a Lox source file, reads its contents
   *   into a string using the default system charset, and executes the script
   *   by passing it to the interpreter.
   * </p>
   *
   * @param path the path of the Lox source file to execute
   * @throws IOException if an error occurs while reading the file
   */
  private static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));

    // Indicate an error in the exit code
    if (hadError) {
      System.exit(65);
    }
  }

  private static void run(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();

    // for now, just print the tokens
    for (Token token : tokens) {
      System.out.println(token);
    }
  }

  /**
   * Reports an error at a specific line in the source code.
   *
   * <p>
   *   This method generates an error message, indicating the line where
   *   the error occurred, and marks that an error has been encountered to
   *   prevent code from being run.
   * </p>
   *
   * @param line the line number where the error occurred
   * @param message a description of the error
   */
  public static void error(int line, String message) {
    report(line, "", message);
  }

  /**
   * Reports an error at a specific token in the source code during parsing.
   *
   * <p>
   *   This method generates an error message with context about where the error
   *   occurred in relation to a specific token. For end-of-file tokens, it indicates
   *   the error occurred at the end of input. For other tokens, it shows the specific
   *   lexeme where the error was found.
   * </p>
   *
   * @param token the token where the error occurred
   * @param message a description of the error
   */
  public static void error(Token token, String message) {
    if (token.type == TokenType.EOF) {
      report(token.line, " at end", message);
    } else {
      report(token.line, " at '" + token.lexeme + "'", message);
    }
  }

  /**
   * Internal helper method to format and print error messages.
   *
   * @param line the line number where the error occurred
   * @param where additional context about error location
   * @param message description of the error
   */
  private static void report(int line, String where, String message) {
    System.err.println("[line " + line + "] Error" + where + ": " + message);
    hadError = true;
  }


}
