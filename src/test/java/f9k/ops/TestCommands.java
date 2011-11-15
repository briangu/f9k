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
import simplenlg.features.Tense;


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

/*
  private static void runSample()
  {
    OPS ops = new OPS();

    ops.literalize(new MemoryElement("sphrase", "actor", null, "verb", null, "verb.tense", Tense.PRESENT, "object", null));
    ops.literalize(new MemoryElement("goal", "type", null));

    ops.make(new MemoryElement("goal", "type", "generate"));

    ops.make(new MemoryElement("sphrase", "actor", "Brian", "verb", "share", "verb.tense", Tense.PAST, "object", "article"));
    ops.make(new MemoryElement("sphrase", "actor", "Joe", "verb", "comment on", "verb.tense", Tense.PAST, "object", "post"));

    ops.make(new MemoryElement("sphrase", "actor", "John", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Joe"));
    ops.make(new MemoryElement("sphrase", "actor", "Larry", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Joe"));
    ops.make(new MemoryElement("sphrase", "actor", "Sally", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Joe"));

//    ops.make(new MemoryElement("sphrase", "actor", "Robert", "verb", "connect to", "verb.tense", Tense.PRESENT, "object", "Joe"));

//    ops.make(new MemoryElement("sphrase", "actor", "Robert", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Mary"));
//    ops.make(new MemoryElement("sphrase", "actor", "Brian", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Mary"));

    ops.addRule(createGenerateRule());
    ops.addRule(createAggregateCommonVerbObjectRule());
    ops.addRule(createGenerateStopRule());

    ops.run();
  }

  private static Rule createGenerateRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));
    query.add(new QueryElement("sphrase", "actor", "$actor", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(new write("actor: {0} verb: {1} object: {2}"), new Object[] { "$actor", "$verb", "$object" }));
    productions.add(new ProductionSpec(getCommand("nlg"), new Object[] { "$actor", "$verb", "$verb.tense", "$object" }));
    productions.add(new ProductionSpec(getCommand("remove"), new Object[] { 1 }));

    return new Rule("generate", query, productions);
  }

  private static Rule createAggregateCommonVerbObjectRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));
    query.add(new QueryElement("sphrase", "actor", "$actor1", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));
    query.add(new QueryElement("sphrase", "actor", "$actor2", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(getCommand("nlgAgg"), new Object[] { "$actor1", "$actor2", "$verb", "$verb.tense", "$object" }));
    productions.add(new ProductionSpec(getCommand("remove"), new Object[] { 1 }));
    productions.add(new ProductionSpec(getCommand("remove"), new Object[] { 2 }));

    return new Rule("generate", query, productions);
  }

  private static Rule createGenerateStopRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(getCommand("remove"), new Object[] { 0 }));

    return new Rule("generate_stop", query, productions);
  }
*/
}
