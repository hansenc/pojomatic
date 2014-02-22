package org.pojomatic.formatter;

import org.pojomatic.PropertyElement;
import org.pojomatic.Pojomator;

/**
 * A formatter to aid in creating a {@code String} representation of a POJO.  A new instance will be
 * created for each time that {@link Pojomator#doToString(Object)} is called.
 *
 * @deprecated use {@link EnhancedPojoFormatter} instead. Use of this interface typically requires creating additional
 * StringBuidler instances.
 */
@Deprecated
public interface PojoFormatter {

  /**
   * Get the {@code String} which should appear at the beginning of the result of
   * {@code toString()}.
   *
   * @param pojoClass the class for which {@code toString()} is being called
   * @return    the prefix to appear at the beginning of the result of {@code toString()}
   * @see Object#toString()
   *
   * @deprecated Use {@link EnhancedPojoFormatter#appendToStringPrefix(StringBuilder, Class)} instead
   */
  @Deprecated
  String getToStringPrefix(Class<?> pojoClass);

  /**
   * Get the {@code String} which should appear at the end of the result of
   * {@code toString()}.
   * @param pojoClass the class for which {@code toString()} is being called
   * @return    the suffix to appear at the end of the result of {@code toString()}
   * @see Object#toString()
   *
   * @deprecated Use {@link EnhancedPojoFormatter#appendToStringSuffix(StringBuilder, Class)} instead
   */
  @Deprecated
  String getToStringSuffix(Class<?> pojoClass);

  /**
   * Get the {@code String} prefix for a given {@code PropertyElement}. This method will be called
   * once for each property used in the result of {@code toString()}, in the order in which
   * those properties will appear in that result, and before the call to
   * {@link PropertyFormatter#format(Object)} for the property's value.
   * @param property the property for which to generate a prefix
   * @return the prefix for the given property
   *
   * @deprecated Use {@link EnhancedPojoFormatter#appendPropertyPrefix(StringBuilder, PropertyElement)}
   */
  @Deprecated
  String getPropertyPrefix(PropertyElement property);

  /**
   * Get the {@code String} suffix for a given {@code PropertyElement}. This method will be called
   * once after each call to {@link PropertyFormatter#format(Object)} for the property's value.
   * @param property the property for which to generate a suffix
   * @return the suffix for the given property
   *
   * @deprecated Use {@link EnhancedPojoFormatter#appendPropertySuffix(StringBuilder, PropertyElement)}
   */
  @Deprecated
  String getPropertySuffix(PropertyElement property);
}
