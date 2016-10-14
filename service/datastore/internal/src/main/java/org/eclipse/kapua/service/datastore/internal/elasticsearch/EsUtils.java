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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EsUtils {
	
	private static final Logger s_logger = LoggerFactory.getLogger(EsUtils.class);

	private static final char SPECIAL_DOT = '.';
	private static final String SPECIAL_DOT_ESC = "$2e";

	private static final char SPECIAL_DOLLAR = '$';
	private static final String SPECIAL_DOLLAR_ESC = "$24";
	
	public static CharSequence ILLEGAL_CHARS = "\"\\/*?<>|,. ";
	
	public static final String ES_TYPE_STRING = "string";
	public static final String ES_TYPE_INTEGER = "integer";
	public static final String ES_TYPE_LONG = "long";
	public static final String ES_TYPE_FLOAT = "float";
	public static final String ES_TYPE_DOUBLE = "double";
	public static final String ES_TYPE_DATE = "date";
	public static final String ES_TYPE_BOOL = "boolean";
	public static final String ES_TYPE_BINARY = "binary";
	
	public static final String ES_TYPE_SHORT_STRING = "str";
	public static final String ES_TYPE_SHORT_INTEGER = "int";
	public static final String ES_TYPE_SHORT_LONG = "lng";
	public static final String ES_TYPE_SHORT_FLOAT = "flt";
	public static final String ES_TYPE_SHORT_DOUBLE = "dbl";
	public static final String ES_TYPE_SHORT_DATE = "dte";
	public static final String ES_TYPE_SHORT_BOOL = "bln";
	public static final String ES_TYPE_SHORT_BINARY = "bin";
	
	private static String normalizeIndexName(String name) {
		String normName = null;
		try {
			EsUtils.checkIdxAliasName(name);
			normName = name;
		} catch (IllegalArgumentException exc) {
			s_logger.trace(exc.getMessage());
			normName = name.toLowerCase().replace(ILLEGAL_CHARS, "_");
			EsUtils.checkIdxAliasName(normName);
		}
		
		return normName;
	}
	
	public static String normalizeMetricName(String name) {
		String newName = name;
		if (newName.contains(".")) {
			newName = newName.replace(String.valueOf(SPECIAL_DOLLAR), SPECIAL_DOLLAR_ESC);
			newName = newName.replace(String.valueOf(SPECIAL_DOT), SPECIAL_DOT_ESC);
			s_logger.trace(String.format("Metric %s contains a special char '%s' that will be replaced with '%s'"
					, name, String.valueOf(SPECIAL_DOT), SPECIAL_DOT_ESC));
		}
		
		return newName;
	}

	public static String restoreMetricName(String normalizedName) {
		String oldName = normalizedName;
		String[] split = oldName.split(Pattern.quote("."));
		oldName = split[0];
		oldName = oldName.replace(SPECIAL_DOT_ESC, String.valueOf(SPECIAL_DOT));
		oldName = oldName.replace(SPECIAL_DOLLAR_ESC, String.valueOf(SPECIAL_DOLLAR));
		return oldName;
	}
	
	public static String[] getMetricParts(String fullName) {
		return fullName == null ? null : fullName.split(Pattern.quote("."));
	}
	
	public static Client getEsClient(String hostname, int port) throws UnknownHostException {
		return getEsClient(hostname, port, "elasticsearch");
	}

	public static Client getEsClient(String hostname, int port, String clustername) throws UnknownHostException {
		
		Settings settings = Settings.settingsBuilder()
		        .put("cluster.name", clustername).build();
		
		InetSocketTransportAddress ita = 
				new InetSocketTransportAddress(InetAddress.getByName(hostname), port);
		Client esClient = TransportClient.builder().settings(settings).build().addTransportAddress(ita);
		
		return esClient;
	}
	
	public static void checkIdxAliasName(String alias) {		
		if (alias == null || alias.isEmpty())
			throw new IllegalArgumentException(String.format("Alias name cannot be %s", alias == null ? "null" : "empty"));
		
		if (alias.startsWith("_"))
			throw new IllegalArgumentException(String.format("Alias name cannot start with _"));
			
		for(int i = 0; i < alias.length(); i++) {
			if (Character.isUpperCase(alias.charAt(i)))
				throw new IllegalArgumentException(String.format("Alias name cannot contain uppercase chars [found %s]", alias.charAt(i)));
		}
		
		if (alias.contains(ILLEGAL_CHARS))
			throw new IllegalArgumentException(String.format("Alias name cannot contain special chars [found oneof %s]", ILLEGAL_CHARS));
	}
	
	public static void checkIdxName(String index) {
		EsUtils.checkIdxAliasName(index);
	}
	
	public static String normalizedIndexAliasName(String alias) {
		String aliasName = normalizeIndexName(alias);
		aliasName = aliasName.replace("-", "_");
		return aliasName;
	}
	
	public static String getAnyIndexName(String accountName) {
		String actualName = EsUtils.normalizedIndexName(accountName);
		actualName = String.format("%s-*", actualName);
		return actualName;
	}
	
	public static String getActualIndexName(String accountName, long timestamp) {
		String actualName = EsUtils.normalizedIndexName(accountName);
		Calendar cal = KapuaDateUtils.getKapuaCalendar();
		cal.setTimeInMillis(timestamp);
		int year = cal.get(Calendar.YEAR);
		int weekOfTheYear = cal.get(Calendar.WEEK_OF_YEAR);
		actualName = String.format("%s-%04d-%02d", actualName, year, weekOfTheYear);
		return actualName;
	}

	public static String getActualKapuaIndexName(String accountName, long timestamp) {
		String actualName = EsUtils.normalizedIndexName(accountName);
		actualName = String.format(".%s", actualName);
		return actualName;
	}
	
	public static String normalizedIndexName(String index) {
		
		String indexName = normalizeIndexName(index);	
		return indexName;
	}

	/**
	 * @param name metric name in es format
	 * @param type metric type in es type
	 * @return
	 * @throws ParseException
	 */
	public static String getMetricValueQualifier(String name, String type) throws ParseException {
		String shortType = EsUtils.getEsTypeAcronym(type);
		return String.format("%s.%s", name, shortType);
	}

	public static String getEsTypeFromValue(Object value) {
		assert value != null : "Metric value must not be null";
		
		if (value instanceof String)
			return ES_TYPE_STRING;
		
		if (value instanceof Integer)
			return ES_TYPE_INTEGER;
		
		if (value instanceof Long)
			return ES_TYPE_LONG;
		
		if (value instanceof Float)
			return ES_TYPE_FLOAT;
		
		if (value instanceof Double)
			return ES_TYPE_DOUBLE;
		
		if (value instanceof Date)
			return ES_TYPE_DATE;
		
		if (value instanceof Byte[])
			return ES_TYPE_BINARY;
		
		if (value instanceof Boolean)
			return ES_TYPE_BOOL;
		
		throw new IllegalArgumentException(String.format("Metric value type for "));
	}
	
	public static String getEsTypeAcronym(String esType) throws ParseException {
		
		if (esType.equals("string"))
			return ES_TYPE_SHORT_STRING;
		
		if (esType.equals("integer"))
			return ES_TYPE_SHORT_INTEGER;
		
		if (esType.equals("long"))
			return ES_TYPE_SHORT_LONG;
		
		if (esType.equals("float"))
			return ES_TYPE_SHORT_FLOAT;
		
		if (esType.equals("double"))
			return ES_TYPE_SHORT_DOUBLE;
		
		if (esType.equals("boolean"))
			return ES_TYPE_SHORT_BOOL;
		
		if (esType.equals("date")) 
			return ES_TYPE_SHORT_DATE;
		
		if (esType.equals("binary")) {
			return ES_TYPE_SHORT_BINARY;
		}
		
		throw new IllegalArgumentException(String.format("Unknown type [%s]", esType));
	}
	
	public static void main (String[] args) {
		System.out.println((new byte[]{}).getClass().getName());
	}
	
	public static <T> String convertToEsType(Class<T> aClass) {
		
		if (aClass == String.class)
			return ES_TYPE_STRING;
		
		if (aClass == Integer.class)
			return ES_TYPE_INTEGER;
		
		if (aClass == Long.class)
			return ES_TYPE_LONG;
		
		if (aClass == Float.class)
			return ES_TYPE_FLOAT;
		
		if (aClass == Double.class)
			return ES_TYPE_DOUBLE;
		
		if (aClass == Boolean.class)
			return ES_TYPE_BOOL;
		
		if (aClass == Date.class) 
			return ES_TYPE_DATE;
		
		if (aClass == byte[].class) {
			return ES_TYPE_BINARY;
		}
		
		throw new IllegalArgumentException(String.format("Unknown type [%s]", aClass.getName()));
	}
	
	public static String convertToEsType(String kapuaType) {
		
		if (kapuaType.equals("string") || kapuaType.equals("String"))
			return ES_TYPE_STRING;
		
		if (kapuaType.equals("int") || kapuaType.equals("Integer"))
			return ES_TYPE_INTEGER;
		
		if (kapuaType.equals("long") || kapuaType.equals("Long"))
			return ES_TYPE_LONG;
		
		if (kapuaType.equals("float") || kapuaType.equals("Float"))
			return ES_TYPE_FLOAT;
		
		if (kapuaType.equals("double") || kapuaType.equals("Double"))
			return ES_TYPE_DOUBLE;
		
		if (kapuaType.equals("boolean") || kapuaType.equals("Boolean"))
			return ES_TYPE_BOOL;
		
		if (kapuaType.equals("date") || kapuaType.equals("Date")) 
			return ES_TYPE_DATE;
		
		if (kapuaType.equals("base64Binary")) {
			return ES_TYPE_BINARY;
		}
		
		throw new IllegalArgumentException(String.format("Unknown type [%s]", kapuaType));
	}
    
    public static String convertToKapuaType(String esType) {
        
        if (esType.equals(ES_TYPE_STRING))
            return "string";
        
        if (esType.equals(ES_TYPE_INTEGER))
            return "int";
        
        if (esType.equals(ES_TYPE_LONG))
            return "long";
        
        if (esType.equals(ES_TYPE_FLOAT))
            return "float";
        
        if (esType.equals(ES_TYPE_DOUBLE))
            return "double";
        
        if (esType.equals(ES_TYPE_BOOL))
            return "boolean";
        
        if (esType.equals(ES_TYPE_DATE)) 
            return "date";
        
        if (esType.equals(ES_TYPE_BINARY)) {
            return "base64Binary";
        }
        
        throw new IllegalArgumentException(String.format("Unknown type [%s]", esType));
    }

	public static Object convertToKapuaObject(String type, String value) throws ParseException {
		
		if (type.equals("string"))
			return value;
		
		if (type.equals("int"))
			return value == null ? null : Integer.parseInt(value);
		
		if (type.equals("long"))
			return value == null ? null : Long.parseLong(value);
		
		if (type.equals("float"))
			return value == null ? null : Float.parseFloat(value);
		
		if (type.equals("double"))
			return value == null ? null : Double.parseDouble(value);
		
		if (type.equals("boolean"))
			return value == null ? null : Boolean.parseBoolean(value);
		
		if (type.equals("date")) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			simpleDateFormat.setTimeZone(KapuaDateUtils.getKapuaTimeZone());
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			simpleDateFormat2.setTimeZone(KapuaDateUtils.getKapuaTimeZone());
			try {
				return value == null ? null : simpleDateFormat.parse(value);
			} catch (ParseException exc) {
				return value == null ? null : simpleDateFormat2.parse(value);
			}
		}
		
		if (type.equals("base64Binary")) {
			return value;
		}
		
		throw new IllegalArgumentException(String.format("Unknown type [%s]", type));
	}

	public static Object convertToEsObject(String type, String value) throws ParseException {
		
		if (type.equals("string") || type.equals("str"))
			return value;
		
		if (type.equals("int") || type.equals("int"))
			return value == null ? null : Integer.parseInt(value);
		
		if (type.equals("long") || type.equals("lng"))
			return value == null ? null : Long.parseLong(value);
		
		if (type.equals("float") || type.equals("flt"))
			return value == null ? null : Float.parseFloat(value);
		
		if (type.equals("double") || type.equals("dbl"))
			return value == null ? null : Double.parseDouble(value);
		
		if (type.equals("boolean") || type.equals("bln"))
			return value == null ? null : Boolean.parseBoolean(value);
		
		if (type.equals("date") || type.equals("dte")) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
			return value == null ? null : simpleDateFormat.parse(value);
		}
		
		if (type.equals("binary") || type.equals("bin")) {
			return value;
		}
		
		throw new IllegalArgumentException(String.format("Unknown type [%s]", type));
	}
	
	public static long getQueryTimeout() {
		return 15000;
	}
	
	public static long getScrollTimeout() {
		return 60000;
	}
}
