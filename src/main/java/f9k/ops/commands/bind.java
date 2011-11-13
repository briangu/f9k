package f9k.ops.commands;


public class bind implements Command
{
  String _name;
  Object _value;

  public bind(String name, Object value)
  {
    _name = name;
    _value = value;
  }

  @Override
  public void exec(CommandContext context)
  {
    Object value = (_value instanceof String) ? context.resolveValue((String)_value) : _value;
    context.setVar(_name, value);
  }
}
