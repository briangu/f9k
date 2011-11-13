package f9k.ops.commands;


public class bind implements Command
{
  String _name;
  Object _value;

  @Override
  public void exec(MatchContext context)
  {
    Object value = (_value instanceof String) ? context.resolveValue((String)_value) : _value;
    context.setVar(_name, value);
  }
}
