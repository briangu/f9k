package f9k.ops;


import java.util.List;


public class QueryElement
{
  public String Type;
  public List<QueryPair> QueryPairs;

  public QueryElement(String type, List<QueryPair> queryPairs)
  {
    Type = type;
    QueryPairs = queryPairs;
  }
}
