package f9k.ops.commands;


public class write implements Command
{
  String _fmt;

  public write(String fmt)
  {
    _fmt = fmt;
  }

  @Override
  public void exec(CommandContext context, Object[] args)
  {
    String s = new String(_fmt);

    for (int i = 0; i < args.length; i++)
    {
      String val = args[i].toString();
      s = s.replace(String.format("{%d}", i), val);
    }

    System.out.println(s);
  }
}
