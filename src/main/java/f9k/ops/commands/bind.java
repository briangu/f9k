package f9k.ops.commands;


import f9k.ops.Command;
import f9k.ops.CommandContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class bind implements Command
{
  @Override
  public void exec(CommandContext context, Object[] args)
  {
    if (args.length < 1)
    {
      throw new IllegalArgumentException("usage: bind <var> <value>");
    }

    String name = args[0].toString();
    Object value = args[1];

    if (value.equals("@STDIN"))
    {
      value = readline();
    }
    else if (name.equals("@SOCKET"))
    {
      // TODO:
      throw new NotImplementedException();
    }

    context.setVar("$"+name, value);
  }

  private String readline()
  {
    try
    {
      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
      return stdin.readLine();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return "";
  }
}
