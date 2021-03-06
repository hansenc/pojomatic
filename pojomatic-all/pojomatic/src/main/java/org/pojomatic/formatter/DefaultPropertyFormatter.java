package org.pojomatic.formatter;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

/**
 * The default property formatter used by Pojomatic.  While the particulars of the formatting
 * strategy are subject to change, the general principle is to provide a meaningful representation.
 * In particular, arrays are formatted "deeply", rather than simply showing the default toString
 * representation of Java arrays.
 *
 * @deprecated use {@link DefaultEnhancedPropertyFormatter} instead.
 */
@Deprecated
public class DefaultPropertyFormatter implements PropertyFormatter {
  //FIXME - this currently prevents formatter reusability, and for very little benefit.
  @Override
  public void initialize(AnnotatedElement element) {
    //Not applicable
  }

  @Override
  public String format(Object value) {
    if (value == null) {
      return "null";
    }
    else if (value.getClass().isArray()) {
      Class<?> componentClass = value.getClass().getComponentType();
      if (componentClass.isPrimitive()) {
        if (Boolean.TYPE == componentClass) {
          return Arrays.toString((boolean[]) value);
        }
        if (Character.TYPE == componentClass) {
          StringBuilder builder = new StringBuilder().append('[');
          boolean seenOne = false;
          for (char c: ((char[]) value)) {
            if(seenOne) {
              builder.append(", ");
            }
            else {
              seenOne = true;
            }
            builder.append('\'');
            if (Character.isISOControl(c)) {
              builder.append("0x").append(Integer.toHexString(c));
            }
            else {
              builder.append(c);
            }
            builder.append('\'');
          }
          return builder.append(']').toString();
        }
        if (Byte.TYPE == componentClass) {
          return Arrays.toString((byte[]) value);
        }
        if (Short.TYPE == componentClass) {
          return Arrays.toString((short[]) value);
        }
        if (Integer.TYPE == componentClass) {
          return Arrays.toString((int[]) value);
        }
        if (Long.TYPE == componentClass) {
          return Arrays.toString((long[]) value);
        }
        if (Float.TYPE == componentClass) {
          return Arrays.toString((float[]) value);
        }
        if (Double.TYPE == componentClass) {
          return Arrays.toString((double[]) value);
        }
        else {
          throw new IllegalStateException("unexpected primitive array base type: " + componentClass);
        }
      }
      else {
        return Arrays.deepToString((Object[]) value);
      }
    }
    else {
      return value.toString();
    }
  }

}
