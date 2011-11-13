package f9k.ops;


import f9k.ops.commands.Command;
import f9k.ops.commands.write;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;


public class TestCommands extends TestCase
{
  public void testBind()
  {
    TestContext testContext = createContext();


  }

  public void testWrite()
  {
    TestContext testContext = createContext();

    testContext.OPS.addRule(createWriteHelloWorldRule());

    testContext.OPS.insert(new MemoryElement("start"));

    testContext.OPS.run();
  }

  private TestContext createContext()
  {
    TestContext testContext = new TestContext();


    return testContext;
  }

  private Rule createWriteHelloWorldRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("start", Collections.<QueryPair>emptyList()));

    List<Command> production = new ArrayList<Command>();
    production.add(new write("hello, world!"));

    return new Rule("write_hello_world", query, production);
  }

  private class TestContext
  {
    OPS OPS = new OPS();
  }
}
