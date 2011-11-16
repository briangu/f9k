package f9k.ops;


import java.util.List;


public class Rule
{
  String Name;
  List<QueryElement> Query;
  List<ProductionSpec> Productions;

  public Rule(String name, List<QueryElement> query, List<ProductionSpec> productions)
  {
    Name = name;
    Query = query;
    Productions = productions;
  }
}
