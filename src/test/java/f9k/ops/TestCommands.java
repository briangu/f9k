package f9k.ops;


import f9k.ops.commands.Command;
import f9k.ops.commands.remove;
import f9k.ops.commands.write;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;


public class TestCommands extends TestCase
{
  public void testBind()
  {
    TestContext testContext = createContext();


  }

  public void testNoValueMatchWrite()
  {
    TestContext testContext = createContext();

    testContext.OPS.addRule(createWriteHelloWorldRule());

    testContext.OPS.insert(new MemoryElement("start"));

    testContext.OPS.run(1);
  }

  public void testValueMatchWrite()
  {
    TestContext testContext = createContext();

    Map<String, Object> values = new HashMap<String, Object>();
    values.put("type", null);
    values.put("status", null);
    MemoryElement goal = new MemoryElement("goal", values);
    testContext.OPS.literalize(goal);

    testContext.OPS.make(new MemoryElement("goal", new HashMap<String, Object>() {{ put("type", "remove"); }}));

    testContext.OPS.addRule(createGoalRule());

    testContext.OPS.run(1);
  }

/*
  public void testMake()
  {
    TestContext testContext = createContext();

    Map<String, Object> values = new HashMap<String, Object>();
    values.put("type", null);
    values.put("status", null);
    MemoryElement goal = new MemoryElement("goal", values);
    testContext.OPS.literalize(goal);

    testContext.OPS.addRule(createWriteHelloWorldRule());

    testContext.OPS.insert(new MemoryElement("start"));

    testContext.OPS.run(1);
  }
*/

  private TestContext createContext()
  {
    TestContext testContext = new TestContext();


    return testContext;
  }

  private Rule createGoalRule()
  {
    List<QueryPair> queryPairs = new ArrayList<QueryPair>();
    queryPairs.add(new QueryPair("type", "remove"));
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", queryPairs));

    List<Command> production = new ArrayList<Command>();
    production.add(new remove(0));

    return new Rule("goal_remove", query, production);
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
