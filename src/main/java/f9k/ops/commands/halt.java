package f9k.ops.commands;


import f9k.ops.Command;
import f9k.ops.CommandContext;


public class halt implements Command
{
  @Override
  public void exec(CommandContext context, Object[] args)
  {
    context.halt();
  }
}
