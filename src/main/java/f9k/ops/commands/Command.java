package f9k.ops.commands;


import f9k.ops.MemoryElement;
import f9k.ops.OPS;
import java.util.List;


public interface Command
{
  void exec(MatchContext context);
}
