package f9k.ops.commands;


public class bind implements Command
{
  String _name;
  Object _value;

  @Override
  public void exec(MatchContext context)
  {
    Object value = context.resolve(_value);
    context.setVar(_name, value);
  }
}
