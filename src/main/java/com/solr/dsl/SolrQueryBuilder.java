package com.solr.dsl;

import static com.solr.dsl.scaffold.FieldBuilder.field;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.solr.dsl.raw.SolrQueryRawExtractor;
import com.solr.dsl.scaffold.QueryScaffold;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.QueryParamHandler;
import com.solr.dsl.views.SecondCommandAggregation;
import com.solr.dsl.views.SmartQuery;
import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.impl.QueryParamHandlerImpl;
import com.solr.dsl.views.info.QueryInfo;

public class SolrQueryBuilder implements SmartQuery, QueryInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrQueryBuilder.class);

    private final QueryScaffold scaffold;
    // private final SecondSolrQuery secondSolrQuery;
    private final PrimarySolrQuery primarySolrQuery;
    private final QueryParamHandler queryParamHandler;
    private static final List<String> recognizedQueryParams = new ArrayList<>();

    static {
	String[] recoFields = new String[] { "q", "fq", "bf", "fl", "facet", "facet.field", "facet.query", "facet.prefix", "bq", "sort" };
	recognizedQueryParams.addAll(Arrays.asList(recoFields));
    }

    private SolrQueryBuilder(String query) {
	super();
	if (query == null) {
	    query = "*:*";
	}
	this.scaffold = new QueryScaffold();
	this.primarySolrQuery = new PrimarySolrQuery(scaffold);
	this.queryParamHandler = new QueryParamHandlerImpl(scaffold, this);
	this.primarySolrQuery.setQuery(field("q").value(query));
	// this.secondSolrQuery = new SecondSolrQuery(this.scaffold);
    }

    private SolrQueryBuilder(String query, List<ScaffoldField> unacknowledgeFields) {
	this(query);
	this.primarySolrQuery.setQuery(field("q").value(query));
	this.primarySolrQuery.addAllUnacknowledgeFields(unacknowledgeFields);
	// this.secondSolrQuery = new SecondSolrQuery(this.scaffold);
    }

    @Deprecated
    SolrQueryBuilder(PrimarySolrQuery primarySolrQuery, SecondSolrQuery secondSolrQuery) {
	this.scaffold = primarySolrQuery.getScaffold();
	this.queryParamHandler = new QueryParamHandlerImpl(scaffold, this);
	this.primarySolrQuery = primarySolrQuery;
	// this.secondSolrQuery = secondSolrQuery;
	this.primarySolrQuery.build();
	// this.secondSolrQuery.build();
    }

    public PrimarySolrQuery getPrimarySolrQuery() {
	return primarySolrQuery;
    }

    @Override
    public QueryInfo info() {
	return this;
    }

    @Override
    public QueryParamHandler change() {
	return this.queryParamHandler;
    }

    @Override
    public String getFacetQueries() {
	return this.scaffold.getByName("facet.query").toString();
    }

    @Override
    public List<String> getFacetFields() {
	return this.scaffold.getMultiByName("facet.field").stream().map(field -> field.toString()).collect(Collectors.toList());
    }

    @Override
    public List<ScaffoldField> getFacetFieldsStructure() {
	return this.scaffold.getMultiByName("facet.field");
    }

    @Override
    public String getFacetPrefixes() {
	scaffold.change("facet", "true");
	return this.scaffold.getByName("facet.prefix").getValue();
    }

    @Override
    public String getFieldList() {
	return this.scaffold.getByName("fl").toString();
    }

    @Override
    public List<String> getFilterQueries() {
	List<String> filters = transform(this.primarySolrQuery.getFilters());
	return filters;
    }

    private static List<String> transform(List<? extends ScaffoldField> c) {
	List<String> fields = Lists.transform(c, new Function<ScaffoldField, String>() {
	    @Override
	    public String apply(ScaffoldField field) {
		return field.toString();
	    }
	});
	return fields;
    }

    @Override
    public String getQuery() {
	return this.primarySolrQuery.getQuery();
    }

    @Override
    public String getSortBy() {
	return this.primarySolrQuery.getSortByScaffoldField().toString();
    }

    public static SmartQuery fromRawQuery(String rawQuery) {
	// return fromRawQuery(rawQuery, query ->
	// SolrQueryRawExtractor.parseQueryString(query));
	return fromRawQuery(rawQuery, defaultQueryParser());
    }

    public static SmartQuery fromRawQuery(String rawQuery, java.util.function.Function<String, List<NameValuePair>> parser) {
	if (parser == null)
	    throw new IllegalArgumentException("parser cannot be null");
	List<NameValuePair> parsedQuery = parser.apply(rawQuery);
	String query = SolrQueryRawExtractor.getSingleQueryParamValue("q", parsedQuery);
	List<ScaffoldField> unacknowledgedQueryParams = SolrQueryRawExtractor.getUnacknowledgedQueryParams(recognizedQueryParams, parsedQuery);
	SmartQuery SQB = new SolrQueryBuilder(query, unacknowledgedQueryParams);

	List<NameValuePair> paramValues = SolrQueryRawExtractor.getMultiQueryParamValue("fq", parsedQuery);
	for (NameValuePair nameValuePair : paramValues) {
	    SQB.filterBy(nameValuePair.getValue());
	}

	// TODO: EXTRAIR FACET=TRUE
	SQB.sortBy(SolrQueryRawExtractor.getSingleQueryParamValue("sort", parsedQuery)).boostBy(SolrQueryRawExtractor.getSingleQueryParamValue("bq", parsedQuery)).and()
		.listBy(SolrQueryRawExtractor.getSingleQueryParamValue("fl", parsedQuery)).and().facetByField(SolrQueryRawExtractor.getMultiQueryParamValue("facet.field", parsedQuery))
		.facetByQuery(SolrQueryRawExtractor.getSingleQueryParamValue("facet.query", parsedQuery)).facetByPrefix(SolrQueryRawExtractor.getSingleQueryParamValue("facet.prefix", parsedQuery));
	return SQB;
    }

    public static SmartQuery newQuery(String query) {
	if (query == null) {
	    return new SolrQueryBuilder("*:*");
	}
	return new SolrQueryBuilder(query);
    }

    @Override
    public SmartQuery boostBy(String command) {
	if (StringUtils.isEmpty(command)) {
	    return this;
	}

	this.primarySolrQuery.addBoostQuery(field("bq").value(command));
	return this;
    }

    @Override
    public SmartQuery filterBy(String command) {
	if (StringUtils.isEmpty(command)) {
	    return this;
	}

	this.primarySolrQuery.addFilter(field("fq").value(command));
	return this;
    }

    @Override
    public SmartQuery sortBy(String command) {
	if (StringUtils.isEmpty(command)) {
	    return this;
	}

	ScaffoldField sort = field("sort").value(command);
	this.primarySolrQuery.setSortBy(sort);
	return this;
    }

    @Override
    @Deprecated
    public SecondCommandAggregation and() {
	return new QueryConfigureCommand(this.scaffold);
    }

    @Override
    public String build() {
	return this.scaffold.build();
    }

    static class PrimarySolrQuery implements BuilderToString {

	private QueryScaffold scaffold;

	public QueryScaffold getScaffold() {
	    return scaffold;
	}

	public PrimarySolrQuery(QueryScaffold scaffold) {
	    super();
	    this.scaffold = scaffold;
	}

	public void setScaffold(QueryScaffold scaffold) {
	    this.scaffold = scaffold;
	}

	public boolean addBoostQuery(ScaffoldField field) {
	    field.setGroup("bq");
	    return scaffold.add(field);
	}

	public boolean addAllBoostQuery(Collection<? extends ScaffoldField> c) {
	    return scaffold.addAll("bq", c);
	}

	public boolean addFilter(ScaffoldField fq) {
	    fq.setGroup("fq");
	    return scaffold.add(fq);
	}

	public boolean addAllFilters(Collection<? extends ScaffoldField> c) {
	    return scaffold.addAll("fq", c);
	}

	public boolean addAllUnacknowledgeFields(Collection<? extends ScaffoldField> fields) {
	    String group = "unack";
	    fields.forEach(field -> this.scaffold.add(new ScaffoldField(field.getName(), field.getValue(), null)));
	    return true;
	}

	public String getQuery() {
	    return this.scaffold.getValueByName("q");
	}

	public void setQuery(ScaffoldField query) {
	    String group = "query";
	    query.setGroup(group);
	    this.scaffold.change(query);
	}

	public ScaffoldField getSortByScaffoldField() {
	    ScaffoldField field = this.scaffold.getByName("sort");
	    return field;
	}

	public void setSortBy(ScaffoldField sortBy) {
	    if (this.scaffold.hasField("sort")) {
		this.scaffold.change("sort", sortBy.getValue());
	    } else {
		String group = "sort";
		sortBy.setGroup(group);
		this.scaffold.add(sortBy);
	    }
	}

	public List<ScaffoldField> getFilters() {
	    return this.scaffold.getByGroupName("fq");
	}

	public List<ScaffoldField> getUnacknowledgeFields() {
	    return this.scaffold.getByGroupName("unack");
	}

	List<ScaffoldField> getBoostQuery() {
	    return this.scaffold.getByGroupName("bq");
	}

	@Override
	public String build() {
	    return scaffold.build();
	}

	@Override
	public String buildToJson() {
	    return this.scaffold.buildToJson();
	}

	@Override
	public String buildEncoded() {
	    return this.scaffold.buildEncoded();
	}
    }

    @Override
    public <T> T getFieldValue(String fieldName) {
	return (T) this.scaffold.getValueByName(fieldName);
    }

    @Override
    public String buildToJson() {
	return this.primarySolrQuery.buildToJson();
    }
    
    @Override
    public String buildEncoded() {
	return this.scaffold.buildEncoded();
    }

    @Override
    @Deprecated
    /**
     * Será removido em breve.
     */
    public void flush() {
    }

    public static java.util.function.Function<String, List<NameValuePair>> defaultQueryParser() {
	return query -> {
	    String[] params = query.split("&");
	    List<NameValuePair> pairs = Arrays.asList(params).stream().map(param -> {
		String[] val = param.split("=");
		if (val.length > 1)
		    return new BasicNameValuePair(decode(val[0]), decode(val[1]));
		return new BasicNameValuePair(val[0], null);
	    }).collect(Collectors.toList());
	    return pairs;
	};
    }
    
    private static String decode(String content){
	String decoded = content;
	try {
	    decoded = URLDecoder.decode(content, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    LOGGER.error("Problem to decode param: {}", content, e);
	}
	return decoded;
    }
    
    public static java.util.function.Function<String, List<NameValuePair>> queryParserWithoutDecode() {
	return query -> {
	    String[] params = query.split("&");
	    List<NameValuePair> pairs = Arrays.asList(params).stream().map(param -> {
		String[] val = param.split("=");
		if (val.length > 1)
		    return new BasicNameValuePair(val[0], val[1]);
		return new BasicNameValuePair(val[0], null);
	    }).collect(Collectors.toList());
	    return pairs;
	};
    }
}