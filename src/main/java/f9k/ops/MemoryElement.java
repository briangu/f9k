package f9k.ops;


import java.util.HashMap;
import java.util.Map;


public class MemoryElement
{
  public String Type;
  public Map<String, Object> Values;

  public MemoryElement(String type, Map<String, Object> values)
  {
    Type = type;
    Values = new HashMap<String, Object>(values);
  }

  public MemoryElement clone()
  {
    return new MemoryElement(Type, Values);
  }

  public MemoryElement make(Map<String, Object> values)
  {
    MemoryElement clone = clone();
    for (String key : values.keySet())
    {
      clone.Values.put(key, values.get(key));
    }
    return clone;
  }
}
