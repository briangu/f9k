package f9k.ops.commands;


import f9k.ops.MemoryElement;
import java.util.HashMap;
import java.util.Map;


public class make implements Command
{
  MemoryElement _element;

  public make(MemoryElement element)
  {
    _element = element;
  }

  @Override
  public void exec(MatchContext context)
  {
    Map<String, Object> values = new HashMap<String, Object>(_element.Values);
    context.resolveValues(values);
    context.make(new MemoryElement(_element.Type, values));
  }
}
