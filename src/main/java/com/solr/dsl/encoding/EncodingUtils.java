package com.solr.dsl.encoding;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncodingUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncodingUtils.class);

    public static List<String> decode(List<String> values) {
	return values.stream().map(val -> decode(val)).collect(Collectors.toList());
    }

    public static String decode(String content) {
	if (content == null)
	    return content;
	String decode = content;
	try {
	    decode = URLDecoder.decode(content, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    LOGGER.error("Error to decode query: {}", decode, e);
	}
	return decode;
    }

    public static String encode(String content) {
	if (content == null)
	    return content;
	String decode = content;
	try {
	    decode = URLEncoder.encode(content, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    LOGGER.error("Error to decode query: {}", decode, e);
	}
	return decode;
    }

    public static List<String> encode(List<String> values) {
	return values.stream().map(val -> encode(val)).collect(Collectors.toList());
    }
}