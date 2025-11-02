package lox.interpreter;

import java.util.HashMap;
import java.util.Map;
import lox.scanner.Token;

/**
 * Represents an instance of a Lox class. Each instance has its own set of fields
 * and inherits methods from its class definition.
 *
 * <p>
 *   Fields are stored in a hash map and can be dynamically added to the instance.
 *   When accessing a property, the instance first checks its fields, then falls back
 *   to looking up methods in its class. Methods are bound to the instance when accessed,
 *   allowing them to access 'this'.
 * </p>
 */
public class LoxInstance {
  private final LoxClass klass;
  private final Map<String, Object> fields = new HashMap<>();

  LoxInstance(LoxClass klass) {
    this.klass = klass;
  }

  Object get(Token name) {
    if (fields.containsKey(name.lexeme)) {
      return fields.get(name.lexeme);
    }

    LoxFunction method = klass.findMethod(name.lexeme);
    if (method != null) {
      return method.bind(this);
    }

    throw new RuntimeError(name, "Undefined property '" + name.lexeme + "'.");
  }

  void set(Token name, Object value) {
    fields.put(name.lexeme, value);
  }

  @Override
  public String toString() {
    return klass.name + " instance";
  }
}
