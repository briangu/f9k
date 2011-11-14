package f9k.ops;


import f9k.ops.commands.ProductionSpec;
import f9k.ops.commands.halt;
import f9k.ops.commands.modify;
import f9k.ops.commands.remove;
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

  public void testTwoVars()
  {
    TestContext testContext = createContext();
    testContext.OPS.literalize(new MemoryElement("goal", "type", null, "status", null));
    testContext.OPS.literalize(new MemoryElement("monkey", "action", null));
    testContext.OPS.make(new MemoryElement("goal", "type", "remove"));
    testContext.OPS.make(new MemoryElement("monkey", "action", "remove"));
    testContext.OPS.addRule(createGoalRuleWithTwoVar());
    testContext.OPS.run(1);
  }

  public void testTwoVarsAndModify()
  {
    TestContext testContext = createContext();
    testContext.OPS.literalize(new MemoryElement("goal", "type", null, "status", null));
    testContext.OPS.literalize(new MemoryElement("monkey", "action", null));
    testContext.OPS.make(new MemoryElement("goal", "type", "remove"));
    testContext.OPS.make(new MemoryElement("monkey", "action", "remove"));
    testContext.OPS.addRule(createGoalRuleWithTwoVar());
    testContext.OPS.addRule(createModifyGoalRule());
    testContext.OPS.addRule(createModifyMonkeyRule());
    testContext.OPS.run();
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

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(new remove(), 0 ));
    productions.add(new ProductionSpec(new halt()));

    return new Rule("goal_remove", query, productions);
  }

  private Rule createGoalRuleWithVar()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "$type"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(new remove(), 0));
    productions.add(new ProductionSpec(new write("hello, {0}"), "$type"));
    productions.add(new ProductionSpec(new halt()));

    return new Rule("goal_remove_with_var", query, productions);
  }

  private Rule createGoalRuleWithTwoVar()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "$type"));
    query.add(new QueryElement("monkey", "action", "$type"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(new remove(), 0));
    productions.add(new ProductionSpec(new write("the goal is {0} and the monkey action is also {0}"), "$type"));
    productions.add(new ProductionSpec(new halt()));

    return new Rule("goal_remove_with_two_var", query, productions);
  }

  private Rule createWriteHelloWorldRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("start", Collections.<QueryPair>emptyList()));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(new write("hello, world!")));
    productions.add(new ProductionSpec(new halt()));

    return new Rule("write_hello_world", query, productions);
  }

  private Rule createModifyGoalRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "remove"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(new modify(), 0, "type", "eat"));
    productions.add(new ProductionSpec(new write("the goal is remove and changing to eat")));

    return new Rule("goal_modify_goal_remove", query, productions);
  }

  private Rule createModifyMonkeyRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("monkey", "action", "remove"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(new modify(), 0, "action", "eat"));
    productions.add(new ProductionSpec(new write("the monkey action was remove and changing to eat")));

    return new Rule("goal_modify_monkey_action", query, productions);
  }

  private class TestContext
  {
    OPS OPS = new OPS();
  }
}
