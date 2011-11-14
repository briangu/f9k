package f9k.ops.commands;


public class bind implements Command
{
  @Override
  public void exec(CommandContext context, Object[] args)
  {
    String name = args[0].toString();
    Object value = args[1];
    context.setVar(name, value);
  }
}
