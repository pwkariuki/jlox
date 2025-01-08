package lox.interpreter;

/**
 * A specialized exception used to implement return statements in the Lox interpreter.
 * This class extends RuntimeException and is used to unwind the call stack when a
 * return statement is executed while carrying the return value from the function.
 *
 * <p>
 *   The exception is constructed with no stack trace or message to minimize overhead,
 *   since it's used for control flow rather than error handling. This is achieved by
 *   passing null for both the message and cause, and false for both suppression and
 *   stack trace parameters to the parent constructor.
 * </p>
 */
public class Return extends RuntimeException {
  final Object value;

  Return(Object value) {
    super(null, null, false, false);
    this.value = value;
  }
}
