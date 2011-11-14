package f9k.ops;


import f9k.ops.commands.ProductionSpec;
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
