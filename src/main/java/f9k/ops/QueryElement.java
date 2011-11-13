package f9k.ops;


import java.util.ArrayList;
import java.util.HashMap;
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

  public QueryElement(String type, Object... values)
  {
    Type = type;

    QueryPairs = new ArrayList<QueryPair>();
    if (values.length > 0)
    {
      if (values.length % 2 != 0)
      {
        throw new IllegalArgumentException("pairs must be in the form: key, value, key, value, ...");
      }

      for (int i = 0; i < values.length; i += 2)
      {
        String key = values[i].toString();
        Object val = values[i+1];
        QueryPair qp = new QueryPair(key, val);
        QueryPairs.add(qp);
      }
    }
  }

}
