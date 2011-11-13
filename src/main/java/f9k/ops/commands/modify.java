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

  public modify(int idx, Object... values)
  {
    _idx = idx;

    _values = new HashMap<String, Object>();
    if (values.length > 0)
    {
      if (values.length % 2 != 0)
      {
        throw new IllegalArgumentException("values must be in the form: key, value, key, value, ...");
      }

      for (int i = 0; i < values.length; i += 2)
      {
        String key = values[i].toString();
        Object val = values[i+1];
        _values.put(key, val);
      }
    }
  }

  @Override
  public void exec(CommandContext context)
  {
    Map<String, Object> values = new HashMap<String, Object>(_values);
    context.resolveValues(values);
    context.modify(_idx, values);
  }
}
