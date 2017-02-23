/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import java.text.MessageFormat;
import java.util.*;

import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of this class is inspired from the org.eclipse.equinox.metatype.impl.ValueTokenizer class
 * in the Eclipse Equinox project.
 *
 * @since 1.0
 */
public class ValueTokenizer {

    private static final Logger logger = LoggerFactory.getLogger(ValueTokenizer.class);

    private static final String VALUE_INVALID = "Value is invalid";
    private static final String NULL_VALUE_INVALID = "Null cannot be validated";
    private static final String CARDINALITY_VIOLATION = "Cardinality violation: \"{0}\" has {1} value(s) but must have between {2} and {3} value(s).";
    private static final String VALUE_OUT_OF_OPTION = "Value {0} is out of Option";
    private static final String VALUE_OUT_OF_RANGE = "Value {0} is out of range";
    private static final String INTERNAL_ERROR = "Internal error: {0}";

    private static final char DELIMITER = ',';
    private static final char ESCAPE = '\\';

    private final List<String> values = new ArrayList<>();

    /**
     * Constructs a tokenizer for a string
     *
     * @param values_str
     */
    public ValueTokenizer(String values_str) {

        if (values_str == null)
            return;
        // The trick is to strip out unescaped whitespace characters before and
        // after the input string as well as before and after each 
        // individual token within the input string without losing any escaped 
        // whitespace characters. Whitespace between two non-whitespace
        // characters may or may not be escaped. Also, any character may be
        // escaped. The escape character is '\'. The delimiter is ','.
        StringBuffer buffer = new StringBuffer();
        // Loop over the characters within the input string and extract each
        // value token.
        for (int i = 0; i < values_str.length(); i++) {
            char c1 = values_str.charAt(i);
            switch (c1) {
            case DELIMITER:
                // When the delimiter is encountered, add the extracted
                // token to the result and prepare the buffer to receive the
                // next token.
                values.add(buffer.toString());
                buffer.delete(0, buffer.length());
                break;
            case ESCAPE:
                // When the escape is encountered, add the immediately
                // following character to the token, unless the end of the
                // input has been reached. Note this will result in loop
                // counter 'i' being incremented twice, once here and once
                // at the end of the loop.
                if (i + 1 < values_str.length()) {
                    buffer.append(values_str.charAt(++i));
                } else {
                    // If the ESCAPE character occurs as the last character
                    // of the string, log the error and ignore it.
                    logger.error(VALUE_INVALID);
                }
                break;
            default:
                // For all other characters, add them to the current token
                // unless dealing with unescaped whitespace at the beginning
                // or end. We know the whitespace is unescaped because it
                // would have been handled in the ESCAPE case otherwise.
                if (Character.isWhitespace(c1)) {
                    // Ignore unescaped whitespace at the beginning of the
                    // token.
                    if (buffer.length() == 0) {
                        continue;
                    }
                    // If the whitespace is not at the beginning, look
                    // forward, starting with the next character, to see if
                    // it's in the middle or at the end. Unescaped
                    // whitespace in the middle is okay.
                    for (int j = i + 1; j < values_str.length(); j++) {
                        // Keep looping until the end of the string is
                        // reached or a non-whitespace character other than
                        // the escape is seen.
                        char c2 = values_str.charAt(j);
                        if (!Character.isWhitespace(c2)) {
                            // If the current character is not the DELIMITER, all whitespace
                            // characters are significant and should be added to the token.
                            // Otherwise, they're at the end and should be ignored. But watch
                            // out for an escape character at the end of the input. Ignore it
                            // and any previous insignificant whitespace if it exists.
                            if (c2 == ESCAPE && j + 1 >= values_str.length()) {
                                continue;
                            }
                            if (c2 != DELIMITER) {
                                buffer.append(values_str.substring(i, j));
                            }
                            // Let loop counter i catch up with the inner loop but keep in
                            // mind it will still be incremented at the end of the outer loop.
                            i = j - 1;
                            break;
                        }
                    }
                } else {
                    // For non-whitespace characters.
                    buffer.append(c1);
                }
            }
        }
        // Don't forget to add the last token.
        values.add(buffer.toString());
    }

    /**
     * Return values as Vector.
     *
     * @return
     */
    public Collection<String> getValues() {
        return Collections.unmodifiableList(values);
    }

    /**
     * Return values as String[] or null.
     *
     * @return
     */
    public String[] getValuesAsArray() {
        if (values.isEmpty()) {
            return null;
        }
        return values.toArray(new String[values.size()]);
    }

    /**
     * Return values as String
     *
     * @return
     */
    public String getValuesAsString() {
        if (values.isEmpty()) {
            return null;
        }
        if (values.size() == 1) {
            return values.get(0);
        }
        StringBuffer buffer = new StringBuffer(values.get(0));
        for (int i = 1; i < values.size(); i++) {
            buffer.append(',');
            buffer.append(values.get(i));
        }
        return buffer.toString();
    }

    /**
     * Validate Tad
     *
     * @param ad
     * @return
     */
    public String validate(KapuaTad ad) {
        // An empty list means the original value was null. Null is never valid.
        if (values.isEmpty()) {
            return NULL_VALUE_INVALID;
        }
        try {
            // A value must match the cardinality.
            int cardinality = Math.abs(ad.getCardinality());
            // If the cardinality is zero, the value must contain one and only one token.
            if (cardinality == 0) {
                if (values.size() != 1) {
                    return MessageFormat.format(CARDINALITY_VIOLATION, new Object[] { getValuesAsString(), values.size(), 1, 1 });
                }
            }
            // Otherwise, the number of tokens must be between 0 and cardinality, inclusive.
            else if (values.size() > cardinality) {
                return MessageFormat.format(CARDINALITY_VIOLATION, new Object[] { getValuesAsString(), values.size(), 0, cardinality });
            }
            // Now inspect each token.
            for (Iterator<String> i = values.iterator(); i.hasNext(); ) {
                String s = i.next();
                // If options were declared and the value does not match one of them, the value is not valid.
                if (!ad.getOption().isEmpty() && !ad.getOption().contains(s)) {
                    return MessageFormat.format(VALUE_OUT_OF_OPTION, s);
                }
                // Check the type. Also check the range if min or max were declared.
                boolean rangeError = false;
                Object minVal = null;
                Object maxVal = null;
                TscalarImpl adScalarType = TscalarImpl.fromValue(ad.getType().value());
                switch (adScalarType) {
                case PASSWORD:
                case STRING:
                    minVal = ad.getMin() == null ? null : new Integer(ad.getMin());
                    maxVal = ad.getMax() == null ? null : new Integer(ad.getMax());
                    if (minVal != null && s.length() < (Integer) maxVal) {
                        rangeError = true;
                    } else if (maxVal != null && s.length() > (Integer) maxVal) {
                        rangeError = true;
                    }
                    break;
                case INTEGER:
                    minVal = ad.getMin() == null ? null : new Integer(ad.getMin());
                    maxVal = ad.getMax() == null ? null : new Integer(ad.getMax());
                    Integer intVal = new Integer(s);
                    if (minVal != null && intVal.compareTo((Integer) minVal) < 0) {
                        rangeError = true;
                    } else if (maxVal != null && intVal.compareTo((Integer) maxVal) > 0) {
                        rangeError = true;
                    }
                    break;
                case LONG:
                    minVal = ad.getMin() == null ? null : new Long(ad.getMin());
                    maxVal = ad.getMax() == null ? null : new Long(ad.getMax());
                    Long longVal = new Long(s);
                    if (ad.getMin() != null && longVal.compareTo((Long) minVal) < 0) {
                        rangeError = true;
                    } else if (maxVal != null && longVal.compareTo((Long) maxVal) > 0) {
                        rangeError = true;
                    }
                    break;
                case DOUBLE:
                    minVal = ad.getMin() == null ? null : new Double(ad.getMin());
                    maxVal = ad.getMax() == null ? null : new Double(ad.getMax());
                    Double doubleVal = new Double(s);
                    if (minVal != null && doubleVal.compareTo((Double) minVal) < 0) {
                        rangeError = true;
                    } else if (maxVal != null && doubleVal.compareTo((Double) maxVal) > 0) {
                        rangeError = true;
                    }
                    break;
                case BOOLEAN:
                    // Any string can be converted into a boolean via Boolean.valueOf(String).
                    // Seems unnecessary to impose any further restrictions.
                    break;
                case CHAR:
                    minVal = ad.getMin() == null ? null : new Character(ad.getMin().charAt(0));
                    maxVal = ad.getMax() == null ? null : new Character(ad.getMax().charAt(0));
                    Character charVal = new Character(s.charAt(0));
                    if (minVal != null && charVal.compareTo((Character) minVal) < 0) {
                        rangeError = true;
                    } else if (maxVal != null && charVal.compareTo((Character) maxVal) > 0) {
                        rangeError = true;
                    }
                    break;
                case FLOAT:
                    minVal = ad.getMin() == null ? null : new Float(ad.getMin());
                    maxVal = ad.getMax() == null ? null : new Float(ad.getMax());
                    Float floatVal = new Float(s);
                    if (minVal != null && floatVal.compareTo((Float) minVal) < 0) {
                        rangeError = true;
                    } else if (maxVal != null && floatVal.compareTo((Float) maxVal) > 0) {
                        rangeError = true;
                    }
                    break;
                case SHORT:
                    minVal = ad.getMin() == null ? null : new Short(ad.getMin());
                    maxVal = ad.getMax() == null ? null : new Short(ad.getMax());
                    Short shortVal = new Short(s);
                    if (minVal != null && shortVal.compareTo((Short) minVal) < 0) {
                        rangeError = true;
                    } else if (maxVal != null && shortVal.compareTo((Short) maxVal) > 0) {
                        rangeError = true;
                    }
                    break;
                case BYTE:
                    minVal = ad.getMin() == null ? null : new Byte(ad.getMin());
                    maxVal = ad.getMax() == null ? null : new Byte(ad.getMax());
                    Byte byteVal = new Byte(s);
                    if (minVal != null && byteVal.compareTo((Byte) minVal) < 0) {
                        rangeError = true;
                    } else if (maxVal != null && byteVal.compareTo((Byte) maxVal) > 0) {
                        rangeError = true;
                    }
                    break;
                default:
                    throw new IllegalStateException();
                }
                if (rangeError) {
                    return (MessageFormat.format(VALUE_OUT_OF_RANGE, s));
                }
            }
            // No problems detected
            return ""; //$NON-NLS-1$
        } catch (Throwable t) {
            String message = MessageFormat.format(INTERNAL_ERROR, t.getMessage());
            logger.debug(message, t);
            return message;
        }
    }
}
