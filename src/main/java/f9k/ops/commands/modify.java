package f9k.ops.commands;


import f9k.ops.Command;
import f9k.ops.CommandContext;
import java.util.HashMap;
import java.util.Map;


public class modify implements Command
{
  @Override
  public void exec(CommandContext context, Object[] args)
  {
    if (((args.length - 1) % 2) != 0)
    {
      throw new IllegalArgumentException("values must be in the form: idx key value key value ...");
    }
    if (!(args[0] instanceof Integer))
    {
      throw new IllegalArgumentException("idx must be an integer");
    }

    int idx = (Integer)args[0];

    Map<String, Object> values = new HashMap<String, Object>();

    for (int i = 1; i < args.length; i += 2)
    {
      String key = args[i].toString();
      Object val = args[i+1];
      values.put(key, val);
    }

    context.modify(idx, values);
  }
}
