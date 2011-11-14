package f9k;


import f9k.ops.MemoryElement;
import f9k.ops.OPS;
import f9k.ops.QueryElement;
import f9k.ops.Rule;
import f9k.ops.commands.Command;
import f9k.ops.commands.ProductionSpec;
import f9k.ops.commands.nlg;
import f9k.ops.commands.nlg_agg;
import f9k.ops.commands.remove;
import f9k.ops.commands.write;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.NIHDBLexicon;
import simplenlg.realiser.english.Realiser;


public class Main
{
  static String DB_FILENAME = "/lexicon/lexAccess2011lite/data/HSqlDb/lexAccess2011.data";

  static Command _nlg;
  static Command _nlgAgg;
  static Command _remove;

  public static String getPwd()
  {
    File file = new File(".");
    try
    {
      return file.getCanonicalPath();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return "";
  }

  public static void main(String[] args)
  {
    Lexicon lexicon = new NIHDBLexicon(getPwd() + DB_FILENAME);
    NLGFactory nlgFactory = new NLGFactory(lexicon);
    Realiser realiser = new Realiser(lexicon);

    _nlg = new nlg(nlgFactory, realiser, lexicon);
    _nlgAgg = new nlg_agg(nlgFactory, realiser, lexicon);
    _remove = new remove();

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

    ops.addRule(createGenerateRule(nlgFactory, realiser, lexicon));
    ops.addRule(createAggregateCommonVerbObjectRule(nlgFactory, realiser, lexicon));
    ops.addRule(createGenerateStopRule());

    ops.run();
  }

  private static Rule createGenerateRule(NLGFactory nlgFactory, Realiser realiser, Lexicon lexicon)
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));
    query.add(new QueryElement("sphrase", "actor", "$actor", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(new write("actor: {0} verb: {1} object: {2}"), new Object[] { "$actor", "$verb", "$object" }));
    productions.add(new ProductionSpec(_nlg, new Object[] { "$actor", "$verb", "$verb.tense", "$object" }));
    productions.add(new ProductionSpec(_remove, new Object[] { 1 }));

    return new Rule("generate", query, productions);
  }

  private static Rule createAggregateCommonVerbObjectRule(NLGFactory nlgFactory, Realiser realiser, Lexicon lexicon)
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));
    query.add(new QueryElement("sphrase", "actor", "$actor1", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));
    query.add(new QueryElement("sphrase", "actor", "$actor2", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(_nlgAgg, new Object[] { "$actor1", "$actor2", "$verb", "$verb.tense", "$object" }));
    productions.add(new ProductionSpec(_remove, new Object[] { 1 }));
    productions.add(new ProductionSpec(_remove, new Object[] { 2 }));

    return new Rule("generate", query, productions);
  }

  private static Rule createGenerateStopRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(_remove, new Object[] { 0 }));

    return new Rule("generate_stop", query, productions);
  }
}
