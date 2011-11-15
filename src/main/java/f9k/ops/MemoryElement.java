package f9k.ops;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MemoryElement
{
  public String Type;
  public Map<String, Object> Values;

  public MemoryElement(String type)
  {
    this(type, Collections.<String, Object>emptyMap());
  }

  public MemoryElement(String type, Map<String, Object> values)
  {
    Type = type;
    Values = new HashMap<String, Object>(values);
  }

  public MemoryElement(String type, Object... values)
  {
    Type = type;

    Values = new HashMap<String, Object>();
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
        Values.put(key, val);
      }
    }
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

  public String toString()
  {
    StringBuilder sb = new StringBuilder();

    sb.append(String.format("%s ", Type));
    for (String key : Values.keySet())
    {
      sb.append(String.format("%s => %s\n", key, Values.get(key)));
    }

    return sb.toString();
  }
}
