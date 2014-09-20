Simple DSL to build complex queries in Solr


Examples:

SolrQueryDsl dsl = QueryBuilder.newQuery();

dsl.setQuery("iphone");

SolrQuery solrQuery = dsl.build(); //Return: ?q=iphone



SolrQuery solrQuery = QueryBuilder.newQuery("sony vaio").filterBy(field("brandName").withValue("sony")).and().sortBy(field("popularity")).build();


SQB.newQuery("car").filterBy("brandName:sony").sortBy("popularity");



Interfaces


FirstCommandAggregation || SecondCommandAggregation    || ThirdCommandAggregation
QueryCommandAggregation || ConfigureCommandAggregation || FacetCommandAggregation