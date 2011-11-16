package f9k.ops;


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

  private boolean _debug = true;
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

      CommandContext context = new CommandContext(this, match.Elements, match.Vars);

      for (ProductionSpec production : match.Rule.Productions)
      {
        Object[] args = new Object[production.Params.length];

        for (int i = 0; i < args.length; i++)
        {
          args[i] = context.resolveValue(production.Params[i]);
        }

        production.Command.exec(context, args);
      }

//      if (_debug) dumpWorkingMemory();
    }
  }

  private void dumpWorkingMemory()
  {
    for (MemoryElement me : _wm)
    {
      System.out.println(me.toString());
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
      _preparedRules.add(preparedRule);
    }

    Collections.sort(_preparedRules, new Comparator<PreparedRule>()
    {
      @Override
      public int compare(PreparedRule preparedRule, PreparedRule preparedRule1)
      {
        return preparedRule1.Specificity.compareTo(preparedRule.Specificity);
      }
    });

    _rules.clear();
    for (PreparedRule preparedRule : _preparedRules)
    {
      _rules.add(preparedRule.Rule);
    }
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
      List<MemoryElement> elements = new ArrayList<MemoryElement>();
      Map<String, Object> vars = new HashMap<String, Object>();

      for (QueryElement qe : rule.Query)
      {
        boolean haveMatch = false;

        for (MemoryElement me : _wm)
        {
          if (elements.contains(me)) continue;
          Map<String, Object> tmpVars = new HashMap<String, Object>(vars);
          haveMatch = compare(qe, me, tmpVars);
          if (haveMatch)
          {
            vars = tmpVars;
            elements.add(me);
            break;
          }
        }

        if (!haveMatch)
        {
          break;
        }
      }

      if (elements.size() == rule.Query.size())
      {
        match = new Match(rule, elements, vars);
        break;
      }
    }

    return match;
  }

  private boolean compare(QueryElement qe, MemoryElement me, Map<String, Object> vars)
  {
    if (!(me.Type.equals(qe.Type))) return false;

    for (QueryPair qp : qe.QueryPairs)
    {
      Object val = me.Values.containsKey(qp.Key) ? me.Values.get(qp.Key) : null;

      if (qp.Value == null)
      {
        if (val == null)
        {
          // match
        }
        else
        {
          return false;
        }
      }
      else
      {
        if (val == null)
        {
          return false;
        }
        else
        {
          if (qp.Value instanceof String)
          {
            String strQpVal = (String)qp.Value;
            if (strQpVal.startsWith("$"))
            {
              if (vars.containsKey(strQpVal))
              {
                if (!val.equals(vars.get(strQpVal)))
                {
                  return false;
                }
              }
              else
              {
                // variable matches everything
                vars.put(strQpVal, val);
              }
            }
            else
            {
              String strVal = (String)val;
              if (!strQpVal.equals(strVal))
              {
                return false;
              }
            }
          }
          else
          {
            if (!qp.Value.equals(val))
            {
              return false;
            }
          }
          // match
        }
      }
    }

    return true;
  }

  private class Match
  {
    public Rule Rule;
    public List<MemoryElement> Elements;
    public Map<String, Object> Vars;

    public Match(Rule rule, List<MemoryElement> elements, Map<String, Object> vars)
    {
      Rule = rule;
      Elements = elements;
      Vars = vars;
    }
  }
}
