package f9k.ops.commands;


public class ProductionSpec
{
  public Command Command;
  public Object[] Params;

  public ProductionSpec(Command command, Object... params)
  {
    Command = command;
    Params = params;
  }
}
