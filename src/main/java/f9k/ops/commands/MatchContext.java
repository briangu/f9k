package f9k.ops.commands;


import f9k.ops.MemoryElement;
import f9k.ops.OPS;
import java.util.List;
import java.util.Map;


public class MatchContext
{
  Map<String, Object> _vars;

  List<MemoryElement> _elements;

  OPS _ops;

  public MatchContext(OPS ops, List<MemoryElement> elements, Map<String, Object> vars)
  {
    _ops = ops;
    _elements = elements;
    _vars = vars;
  }

  public void remove(int idx)
  {
    if (idx > _elements.size())
    {
      throw new IllegalArgumentException(String.format("idx %d > match set", idx));
    }
    MemoryElement element = _elements.get(idx);
    _ops._wm.remove(element);
  }

  public boolean hasVar(String name)
  {
    return _vars.containsKey(name);
  }

  public void setVar(String name, Object val)
  {
    _vars.put(name, val);
  }

  public Object getVar(String name)
  {
    return _vars.get(name);
  }
}
