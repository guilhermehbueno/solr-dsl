package com.solr.dsl.scaffold;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.solr.dsl.encoding.EncodingUtils;
import com.solr.dsl.views.build.BuilderToString;

/**
 * The most important class
 * 
 * @author guilhermehbueno
 *
 */
public class QueryScaffold implements BuilderToString {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryScaffold.class);

    private final List<ScaffoldField> fields = new ArrayList<>();

    public boolean hasAnyFacet() {
	return this.fields.stream().filter(field -> field.getName().contains("facet")).count() > 0;
    }

    public boolean isFacetDisabled() {
	LOGGER.debug("Invoking isFacetDisabled(), values({})", StringUtils.join(new Object[] {}, ", "));

	ScaffoldField field = getByName("facet");
	if (field == null || field.getValue() == null)
	    return false;
	boolean is = Boolean.parseBoolean(getByName("facet").getValue());
	return is;
    }

    public boolean hasField(String fieldName) {
	LOGGER.debug("Invoking hasField(fieldName), values({})", StringUtils.join(new Object[] { fieldName }, ", "));

	return this.fields.stream().filter(field -> field.getName().contains(fieldName)).count() > 0;
    }

    public boolean change(String name, String value) {
	LOGGER.debug("Invoking change(name, value), values({})", StringUtils.join(new Object[] { name, value }, ", "));

	ScaffoldField field = getByName(name);
	if (field == null) {
	    field = new ScaffoldField(name, value, name);
	    fields.add(field);
	} else {
	    field.setValue(value);
	}
	return true;
    }

    public boolean change(ScaffoldField field) {
	LOGGER.debug("Invoking change(field), values({})", StringUtils.join(new Object[] { field }, ", "));
	return change(field.getName(), field.getValue());
    }

    public boolean add(ScaffoldField field) {
	LOGGER.debug("Invoking add(field), values({})", StringUtils.join(new Object[] { field }, ", "));

	return fields.add(field);
    }

    public boolean remove(ScaffoldField field) {
	LOGGER.debug("Invoking remove(field), values({})", StringUtils.join(new Object[] { field }, ", "));
	return fields.remove(field);
    }

    public boolean removeByName(String name) {
	LOGGER.debug("Invoking removeByName(name), values({})", StringUtils.join(new Object[] { name }, ", "));
	List<ScaffoldField> results = getMultiByName(name);
	results.forEach(field -> remove(field));
	return true;
    }

    public ScaffoldField getByName(String name) {
	LOGGER.debug("Invoking getByName(name), values({})", StringUtils.join(new Object[] { name }, ", "));

	ScaffoldField result = null;
	for (ScaffoldField field : this.fields) {
	    if (field.getName().equalsIgnoreCase(name)) {
		result = field;
	    }
	}
	return result;
    }

    public List<ScaffoldField> getMultiByName(String name) {
	LOGGER.debug("Invoking getMultiByName(name), values({})", StringUtils.join(new Object[] { name }, ", "));
	return this.fields.stream().filter(field -> field.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
    }

    public String getValueByName(String name) {
	LOGGER.debug("Invoking getValueByName(name), values({})", StringUtils.join(new Object[] { name }, ", "));
	ScaffoldField result = null;
	for (ScaffoldField field : this.fields) {
	    if (field.getName().equalsIgnoreCase(name)) {
		result = field;
	    }
	}
	if (result == null)
	    return null;
	return result.getValue();
    }

    @Override
    public String toString() {
	LOGGER.debug("Invoking toString(), values({})", StringUtils.join(new Object[] {}, ", "));
	List<String> params = fields.stream().map(field -> {
	    return field.getName() + "=" + field.getValue();
	}).collect(Collectors.toList());
	if (hasAnyFacet() && !isFacetDisabled() && !hasField("facet")) {
	    params.add("facet=true");
	}

	String query = StringUtils.join(params, "&");
	LOGGER.debug("Query: {}", query);
	return query;
    }

    public List<ScaffoldField> getFields() {
	LOGGER.debug("Invoking getFields(), values({})", StringUtils.join(new Object[] {}, ", "));
	return Collections.unmodifiableList(fields);
    }

    public boolean addAll(String group, Collection<? extends ScaffoldField> collection) {
	LOGGER.debug("Invoking addAll(group, collection), values({})", StringUtils.join(new Object[] { group, collection }, ", "));
	this.fields.forEach(field -> field.setGroup(group));
	return this.fields.addAll(collection);
    }

    public List<ScaffoldField> getByGroupName(String groupName) {
	LOGGER.debug("Invoking getByGroupName(groupName), values({})", StringUtils.join(new Object[] { groupName }, ", "));
	if (groupName == null)
	    return null;
	return this.fields.stream().filter(field -> groupName != null && groupName.equalsIgnoreCase(field.getGroup())).collect(Collectors.toList());
    }

    @Override
    public String build() {
	LOGGER.debug("Invoking build(), values({})", StringUtils.join(new Object[] {}, ", "));
	return toString();
    }

    @Override
    public String buildToJson() {
	LOGGER.debug("Invoking buildToJson(), values({})", StringUtils.join(new Object[] {}, ", "));

	Map<String, List<ScaffoldField>> groupedFields = fields.stream().collect(Collectors.groupingBy(field -> field.getName()));
	Map<String, Object> params = new HashMap<>();

	LOGGER.debug("Grouped fields: {}", groupedFields.keySet());

	groupedFields.forEach((key, value) -> {
	    List<String> values = value.stream().map(field -> field.getValue()).collect(Collectors.toList());
	    if (values.size() == 1) {
		params.put(key, values.get(0));
	    } else {
		params.put(key, values);
	    }
	});

	Map<String, Object> paramsWrapper = new HashMap<>();
	paramsWrapper.put("params", params);

	Gson gson = new Gson();
	String result = gson.toJson(paramsWrapper, HashMap.class);
	return result;
    }

   

    @Override
    public String buildEncoded() {
	LOGGER.debug("Invoking buildEncoded(), values({})", StringUtils.join(new Object[] {}, ", "));
	List<String> params = fields.stream().map(field -> {
	    return field.getName() + "=" + EncodingUtils.encode(field.getValue());
	}).collect(Collectors.toList());
	if (hasAnyFacet() && !isFacetDisabled() && !hasField("facet")) {
	    params.add("facet=true");
	}

	String query = StringUtils.join(params, "&");
	LOGGER.debug("Query: {}", query);
	return query;
    }
}