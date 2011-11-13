package f9k.ops;


import f9k.ops.commands.Command;
import f9k.ops.commands.remove;
import f9k.ops.commands.write;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    testContext.OPS.literalize(new MemoryElement("goal", "type", null, "status", null));
    testContext.OPS.make(new MemoryElement("goal", "type", "remove"));
    testContext.OPS.addRule(createGoalRule());

    testContext.OPS.run(1);
  }

  public void testVars()
  {
    TestContext testContext = createContext();
    testContext.OPS.literalize(new MemoryElement("goal", "type", null, "status", null));
    testContext.OPS.make(new MemoryElement("goal", "type", "remove"));
    testContext.OPS.addRule(createGoalRuleWithVar());
    testContext.OPS.run(1);
  }

  private TestContext createContext()
  {
    TestContext testContext = new TestContext();


    return testContext;
  }

  private Rule createGoalRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "remove"));

    List<Command> production = new ArrayList<Command>();
    production.add(new remove(0));

    return new Rule("goal_remove", query, production);
  }

  private Rule createGoalRuleWithVar()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "$type"));

    List<Command> production = new ArrayList<Command>();
    production.add(new remove(0));
    production.add(new write("hello, {0}", "$type"));

    return new Rule("goal_remove_with_var", query, production);
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
