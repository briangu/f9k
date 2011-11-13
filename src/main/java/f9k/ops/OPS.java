package f9k.ops;


import f9k.ops.commands.Command;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OPS
{
  private List<MemoryElement> _wm = new ArrayList<MemoryElement>();

  private List<Rule> _rules = new ArrayList<Rule>();
  private List<PreparedRule> _preparedRules = new ArrayList<PreparedRule>();

  private Map<String, MemoryElement> _templates = new HashMap<String, MemoryElement>();

  private boolean _halt = false;

  public void reset()
  {
    _halt = false;
    _rules.clear();
    _templates.clear();
    _wm.clear();
  }

  public void halt()
  {
    _halt = true;
  }

  public void literalize(MemoryElement template)
  {
    _templates.put(template.Type, template);
  }

  public void insert(MemoryElement element)
  {
    _wm.add(element);
  }

  public void remove(MemoryElement element)
  {
    _wm.remove(element);
  }

  public MemoryElement make(MemoryElement element)
  {
    if (!_templates.containsKey(element.Type))
    {
      throw new IllegalArgumentException(String.format("memory element type %s not literalized", element.Type));
    }

    MemoryElement newElement = _templates.get(element.Type).make(element.Values);
    _wm.add(newElement);

    return newElement;
  }

  public void run()
  {
    run(Integer.MAX_VALUE);
  }

  public void run(int steps)
  {
    while (steps-- > 0 && !_halt)
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

  public void addRules(List<Rule> rules)
  {
    _rules.addAll(rules);
    prepareQueries();
  }

  public void addRule(Rule rule)
  {
    _rules.add(rule);
    prepareQueries();
  }

  private void prepareQueries()
  {
    _preparedRules.clear();

    for (Rule rule : _rules)
    {
      PreparedRule preparedRule = new PreparedRule();
      preparedRule.Rule = rule;
      preparedRule.Specificity = computeSpecificity(rule);
    }

    Collections.sort(_preparedRules, new Comparator<PreparedRule>()
    {
      @Override
      public int compare(PreparedRule preparedRule, PreparedRule preparedRule1)
      {
        return preparedRule.Specificity.compareTo(preparedRule1.Specificity);
      }
    });
  }

  private Integer computeSpecificity(Rule rule)
  {
    Integer specificity = 0;

    for (QueryElement element : rule.Query)
    {
      Integer elementSpecificity = 0;

      for (QueryPair queryPair : element.QueryPairs)
      {
        if (!(queryPair.Value instanceof String)) continue;
        String strVal = (String) queryPair.Value;
        if (!strVal.startsWith("$")) continue;
        elementSpecificity++;
      }

      specificity += elementSpecificity;
    }

    return specificity;
  }

  private class PreparedRule
  {
    Rule Rule;
    Integer Specificity;
  }

  private Match match()
  {
    Match match = null;

    for (Rule rule : _rules)
    {

    }

    return match;
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
