package f9k.ops.commands;


public class remove implements Command
{
  int _idx;

  public remove(int idx)
  {
    _idx = idx;
  }

  @Override
  public void exec(CommandContext context)
  {
    context.remove(_idx);
  }
}
