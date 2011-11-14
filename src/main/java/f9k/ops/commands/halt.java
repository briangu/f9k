package f9k.ops.commands;


public class halt implements Command
{
  @Override
  public void exec(CommandContext context, Object[] args)
  {
    context.halt();
  }
}
