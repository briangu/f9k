package f9k.ops;


import f9k.ops.commands.Command;
import java.util.List;


public class Rule
{
  String Name;
  List<QueryElement> Query;
  List<Command> Production;

  public Rule(String name, List<QueryElement> query, List<Command> production)
  {
    Name = name;
    Query = query;
    Production = production;
  }
}
