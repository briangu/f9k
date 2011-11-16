package f9k.ops.commands;


import f9k.ops.Command;
import f9k.ops.CommandContext;


public class write implements Command
{
  @Override
  public void exec(CommandContext context, Object[] args)
  {
    String fmt = args[0].toString();

    String s = new String(fmt);

    for (int i = 1; i < args.length; i++)
    {
      String val = args[i].toString();
      s = s.replace(String.format("{%d}", i-1), val);
    }

    System.out.println(s);
  }
}
