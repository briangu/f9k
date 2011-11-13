package f9k.ops.commands;


import java.util.Collections;
import java.util.List;


public class write implements Command
{
  String _fmt;
  List<String> _vars;

  public write(String fmt)
  {
    this(fmt, Collections.<String>emptyList());
  }

  public write(String fmt, List<String> vars)
  {
    _fmt = fmt;
    _vars = vars;
  }

  @Override
  public void exec(CommandContext context)
  {
    String s = new String(_fmt);

    for (int i = 0; i < _vars.size(); i++)
    {
      String name = _vars.get(i);
      if (!context.hasVar(name))
      {
        throw new IllegalArgumentException("unknown var name: " + name);
      }
      String val = context.getVar(name).toString();
      s = s.replace(name, val);
    }

    System.out.println(s);
  }
}
