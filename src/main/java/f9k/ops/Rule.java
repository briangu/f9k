package f9k.ops;


import f9k.ops.commands.Command;
import java.util.List;


public class Rule
{
  String Name;
  List<MemoryElement> Query;
  List<Command> Production;

  public Rule(String name, List<MemoryElement> query, List<Command> production)
  {
    Name = name;
    Query = query;
    Production = production;
  }
}
