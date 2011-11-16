package f9k.ops;


public interface Command
{
  void exec(CommandContext context, Object[] args);
}
