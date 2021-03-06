package org.pojomatic.formatter;

import java.lang.reflect.AnnotatedElement;

/**
 * A formatter for a property.
 * <p>
 * Any implementation of {@code PropertyFormatter} must have a public no-argument constructor.
 *
 * @deprecated implement {@link EnhancedPropertyFormatter} instead. This class is unaware of primitives, and does not
 * leverage string buffers.
 */
@Deprecated
public interface PropertyFormatter {
  /**
   * Initialize the formatter for use; this method will be called exactly once on an instance, prior
   * to any calls to {@link #format(Object)}.  This method does not need to be
   * thread-safe.
   * @param element the field or method this formatter will be used for.
   */
  public void initialize(AnnotatedElement element);

  /**
   * Format a given value.  This method must be thread safe.
   * @param value the value to format
   * @return the value, formatted (must not be null)
   */
  public String format(Object value);
}
