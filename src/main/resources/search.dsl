[condition][]Quando o termo buscado for {termo}=$query: SolrQueryBuilder(getQuery() == "q={termo}")
[consequence][]Imprimir a query=System.out.println("Passou pelo Drools:"+$query.getQuery());