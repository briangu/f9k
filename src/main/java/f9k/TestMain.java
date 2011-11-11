package f9k;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.NIHDBLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;


public class TestMain
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

  public static void main(String[] args)
  {
    Lexicon lexicon = Lexicon.getDefaultLexicon();

    lexicon = new NIHDBLexicon(getPwd() + DB_FILENAME);
    NLGFactory nlgFactory = new NLGFactory(lexicon);
    Realiser realiser = new Realiser(lexicon);

    SPhraseSpec p1 = nlgFactory.createClause("Mary", "chase", "the monkey");
    SPhraseSpec p2 = nlgFactory.createClause("The monkey", "fight back");
    SPhraseSpec p3 = nlgFactory.createClause("Mary", "be", "nervous");

    DocumentElement s1 = nlgFactory.createSentence(p1);
    DocumentElement s2 = nlgFactory.createSentence(p2);
    DocumentElement s3 = nlgFactory.createSentence(p3);

    DocumentElement par1 = nlgFactory.createParagraph(Arrays.asList(s1, s2, s3));

    String output = realiser.realise(par1).getRealisation();
    System.out.println(output);

    SPhraseSpec p = nlgFactory.createClause("Mary", "chase", "the monkey");
    NPPhraseSpec place = nlgFactory.createNounPhrase("park");
    place.setSpecifier("the");
    PPPhraseSpec pp = nlgFactory.createPrepositionPhrase();
    pp.addComplement(place);
    pp.setPreposition("in");
    p.addComplement(pp);
    p.addComplement("very quickly");
    p.addComplement("despite her exhaustion");
    p.setFeature(Feature.NEGATED, "not");
    output = realiser.realiseSentence(p);
    System.out.println(output);

    test1();
  }

  private static void test1()
  {
    Lexicon lexicon = new NIHDBLexicon(getPwd() + DB_FILENAME);
    NLGFactory nlgFactory = new NLGFactory(lexicon);
    Realiser realiser = new Realiser(lexicon);

    SPhraseSpec s1 = nlgFactory.createClause("my cat", "like", "fish");
    SPhraseSpec s2 = nlgFactory.createClause("my dog", "like", "big bones");
    SPhraseSpec s3 = nlgFactory.createClause("my horse", "like", "grass");

    CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
    // may revert to nlgFactory.createCoordinatedPhrase( ) ;
    c.addCoordinate(s1);
    c.addCoordinate(s2);
    c.addCoordinate(s3);

    String output = realiser.realiseSentence(c);
    System.out.println(output);


    SPhraseSpec p = nlgFactory.createClause("I", "be", "happy");
    SPhraseSpec q = nlgFactory.createClause("I", "eat", "fish");

    q.setFeature(Feature.COMPLEMENTISER, "because");
    q.setFeature(Feature.TENSE, Tense.PAST);
    p.addComplement(q);

    String output4 = realiser.realiseSentence(p);  //Realiser created earlier
    System.out.println(output4);
  }
}
