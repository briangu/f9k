package f9k;


import f9k.ops.MemoryElement;
import f9k.ops.OPS;
import f9k.ops.QueryElement;
import f9k.ops.Rule;
import f9k.ops.commands.Command;
import f9k.ops.commands.nlg;
import f9k.ops.commands.nlg_agg;
import f9k.ops.commands.remove;
import f9k.ops.commands.write;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import simplenlg.aggregation.Aggregator;
import simplenlg.aggregation.BackwardConjunctionReductionRule;
import simplenlg.aggregation.ClauseCoordinationRule;
import simplenlg.aggregation.ForwardConjunctionReductionRule;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.NIHDBLexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;


public class Main
{
  static String DB_FILENAME = "/lexicon/lexAccess2011lite/data/HSqlDb/lexAccess2011.data";

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

  static PhraseElement man, woman, dog, boy, np4, np5, np6, proTest1, proTest2;

  /** The salacious. */
  static PhraseElement beautiful, stunning, salacious;

  /** The under the table. */
  static PhraseElement onTheRock, behindTheCurtain, inTheRoom, underTheTable;

  /** The say. */
  static VPPhraseSpec kick, kiss, walk, talk, getUp, fallDown, give, say;

  static SPhraseSpec s1, s2, s3, s4, s5, s6;
  static Aggregator aggregator;
  static ClauseCoordinationRule coord;
  static ForwardConjunctionReductionRule fcr;
  static BackwardConjunctionReductionRule bcr;

  public static void main(String[] args)
  {
    Lexicon lexicon = new NIHDBLexicon(getPwd() + DB_FILENAME);
    NLGFactory nlgFactory = new NLGFactory(lexicon);
    Realiser realiser = new Realiser(lexicon);

    OPS ops = new OPS();

    ops.literalize(new MemoryElement("sphrase", "actor", null, "verb", null, "verb.tense", Tense.PRESENT, "object", null));
    ops.literalize(new MemoryElement("goal", "type", null));

    ops.make(new MemoryElement("goal", "type", "generate"));
    ops.make(new MemoryElement("sphrase", "actor", "Brian", "verb", "share", "verb.tense", Tense.PAST, "object", "article"));
    ops.make(new MemoryElement("sphrase", "actor", "Joe", "verb", "comment on", "verb.tense", Tense.PAST, "object", "post"));
    ops.make(new MemoryElement("sphrase", "actor", "John", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Joe"));
    ops.make(new MemoryElement("sphrase", "actor", "Larry", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Joe"));

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

    List<Command> production = new ArrayList<Command>();
//    production.add(new write("actor: {0} verb: {1} object: {2}", "$actor", "$verb", "$object"));
    production.add(new nlg(nlgFactory, realiser, lexicon));
    production.add(new remove(1));

    return new Rule("generate", query, production);
  }

  private static Rule createAggregateCommonVerbObjectRule(NLGFactory nlgFactory, Realiser realiser, Lexicon lexicon)
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));
    query.add(new QueryElement("sphrase", "actor", "$actor1", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));
    query.add(new QueryElement("sphrase", "actor", "$actor2", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));

    List<Command> production = new ArrayList<Command>();
    production.add(new nlg_agg(nlgFactory, realiser, lexicon));
    production.add(new remove(1));
    production.add(new remove(2));

    return new Rule("generate", query, production);
  }

  private static Rule createGenerateStopRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));

    List<Command> production = new ArrayList<Command>();
    production.add(new remove(0));

    return new Rule("generate_stop", query, production);
  }
}
