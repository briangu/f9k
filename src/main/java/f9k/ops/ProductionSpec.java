package f9k.ops;


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
