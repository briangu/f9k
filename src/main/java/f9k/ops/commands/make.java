package f9k.ops.commands;


import f9k.ops.MemoryElement;
import java.util.HashMap;
import java.util.Map;


public class make implements Command
{
  @Override
  public void exec(CommandContext context, Object[] args)
  {
    if (((args.length - 1) % 2) != 0)
    {
      throw new IllegalArgumentException("values must be in the form: type key value key value ...");
    }
    if (!(args[0] instanceof String))
    {
      throw new IllegalArgumentException("idx must be a String");
    }

    String type = (String)args[0];

    Map<String, Object> values = new HashMap<String, Object>();

    for (int i = 1; i < args.length; i += 2)
    {
      String key = args[i].toString();
      Object val = args[i+1];
      values.put(key, val);
    }

    context.make(new MemoryElement(type, values));
  }
}
