package f9k.ops.commands;


import java.util.HashMap;
import java.util.Map;


public class modify implements Command
{
  Map<String, Object> _values;
  int _idx;

  public modify(int idx, Map<String, Object> values)
  {
    _idx = idx;
    _values = values;
  }

  @Override
  public void exec(MatchContext context)
  {
    Map<String, Object> values = new HashMap<String, Object>(_values);
    context.resolveValues(values);
    context.modify(_idx, values);
  }
}
