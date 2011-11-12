package f9k.ops;


import f9k.ops.commands.Command;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OPS
{
  public enum Strategy
  {
    Lex
  }

  public List<MemoryElement> _wm = new ArrayList<MemoryElement>();

  private List<Rule> _rules = new ArrayList<Rule>();

  private Map<String, MemoryElement> _templates = new HashMap<String, MemoryElement>();

  public OPS create(
    Strategy strategy,
    List<Map<String, Object>> templates
  )
  {
    return new OPS();
  }

  public void literalize(MemoryElement template)
  {
    _templates.put(template.Type, template);
  }

  public void make(MemoryElement element)
  {
    if (!_templates.containsKey(element.Type))
    {
      throw new IllegalArgumentException(String.format("memory element type %s not literalized", element.Type));
    }

    _wm.add(_templates.get(element.Type).make(element.Values));
  }

  public void run()
  {
    run(Integer.MAX_VALUE);
  }

  public void run(int steps)
  {
    while (steps-- > 0)
    {
      Match match = match();
      if (match == null)
      {
        break;
      }

      for (Command cmd : match.Production)
      {
        cmd.exec(null);
      }
    }
  }

  public void addRule(Rule rule)
  {
    _rules.add(rule);
  }

  private Match match()
  {
    return null;
  }

  private class Match
  {
    public List<MemoryElement> Elements;
    public List<Command> Production;
    public Match(List<MemoryElement> elements, List<Command> production)
    {
      Elements = elements;
      Production = production;
    }
  }
}
