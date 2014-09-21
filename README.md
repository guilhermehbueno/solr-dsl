Simple DSL to build complex queries in Solr

Examples:

SolrQueryBuilder.newQuery("iphone").build(); 
//"q=iphone" 

SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").build(); 
//"q=iphone&fq=name:teste"

SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").filterBy("category:categoryName").build(); 
//"q=iphone&fq=name:teste&fq=category:categoryName"

SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").sortBy("popularity").build(); 
//"q=iphone&fq=name:teste&sort=popularity"

SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").sortBy("popularity").and().listBy("id,name").build(); 
//"q=iphone&fq=name:teste&sort=popularity&fl=id,name"

SolrQueryBuilder.newQuery("iphone")
					.filterBy("name:teste")
					.sortBy("popularity").and()
					.listBy("id,name").and()
					.facetByField("category")
					.build(); 
//"q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.field=category"

SolrQueryBuilder.newQuery("iphone")
					.filterBy("name:teste")
					.sortBy("popularity").and()
					.listBy("id,name").and()
					.facetByField("category")
					.facetByQuery("teste")
					.build();
//"q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.query=teste&facet.field=category"


Interfaces

FirstCommandAggregation || SecondCommandAggregation    || ThirdCommandAggregation
QueryCommandAggregation || ConfigureCommandAggregation || FacetCommandAggregation