package f9k.ops.commands;


import java.util.Collections;
import java.util.List;


public class write implements Command
{
  String _fmt;
  String[] _vars;

  public write(String fmt)
  {
    this(fmt, new String[0]);
  }

  public write(String fmt, String... vars)
  {
    _fmt = fmt;
    _vars = vars;
  }

  @Override
  public void exec(CommandContext context)
  {
    String s = new String(_fmt);

    for (int i = 0; i < _vars.length; i++)
    {
      String name = _vars[i];
      if (!context.hasVar(name))
      {
        throw new IllegalArgumentException("unknown var name: " + name);
      }
      String val = context.getVar(name).toString();
      s = s.replace(String.format("{%d}", i), val);
    }

    System.out.println(s);
  }
}
