package f9k.ops.commands;


public class remove implements Command
{
  @Override
  public void exec(CommandContext context, Object[] args)
  {
    if (args.length == 0)
    {
      throw new IllegalArgumentException("not enough arguments specified");
    }
    if (!(args[0] instanceof Integer))
    {
      throw new IllegalArgumentException("argument should be an Integer");
    }
    int idx = (Integer)args[0];
    context.remove(idx);
  }
}
