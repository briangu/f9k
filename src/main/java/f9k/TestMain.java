package f9k;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.NIHDBLexicon;
import simplenlg.phrasespec.AdvPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
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

    NPPhraseSpec mary = nlgFactory.createNounPhrase("Mary");
    VPPhraseSpec chase = nlgFactory.createVerbPhrase("chase");
    NPPhraseSpec monkey = nlgFactory.createNounPhrase("monkey");
    monkey.setSpecifier("the");

    SPhraseSpec p1 = nlgFactory.createClause();
    p1.setSubject(mary);
    p1.setVerb(chase);
    p1.setObject(monkey);
    p1.setFeature(Feature.TENSE, Tense.PAST);
    SPhraseSpec p2 = nlgFactory.createClause();
    p2.setSubject(monkey);
    p2.setVerb("fight back");
    p2.setFeature(Feature.TENSE, Tense.PAST);
    SPhraseSpec p3 = nlgFactory.createClause();
    p3.setSubject(mary);
    p3.setVerb("be");
    p3.setObject("nervous");
    p3.setFeature(Feature.TENSE, Tense.PAST);

    DocumentElement s1 = nlgFactory.createSentence(p1);
    DocumentElement s2 = nlgFactory.createSentence(p2);
    DocumentElement s3 = nlgFactory.createSentence(p3);

    DocumentElement par1 = nlgFactory.createParagraph();
    par1.addComponent(s1);
    par1.addComponent(s2);
    par1.addComponent(s3);

    DocumentElement section = nlgFactory.createSection("The Trials and Tribulations of Mary and the Monkey");
    section.addComponent(par1);

    String output = realiser.realise(section).getRealisation();
    System.out.println(output);

    AdvPhraseSpec quickly = nlgFactory.createAdverbPhrase("quickly");
    quickly.addModifier("very");
//    chase.addPreModifier(quickly);
//    chase.setFeature(Feature.NEGATED, true);

    SPhraseSpec p = nlgFactory.createClause();
    p.setSubject(mary);
    p.setVerb(chase);
    p.setObject(monkey);

    NPPhraseSpec place = nlgFactory.createNounPhrase("park");
    place.setSpecifier("the");

    PPPhraseSpec pp = nlgFactory.createPrepositionPhrase();
    pp.addComplement(place);
    pp.setPreposition("in");

    p.addComplement(pp);
    p.addFrontModifier("despite her exhaustion");
    p.setFeature(Feature.TENSE, Tense.PAST);

    System.out.println(realiser.realiseSentence(p));

    chase.setFeature(Feature.NEGATED, false);
    p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
    p.setFeature(Feature.TENSE, Tense.PAST);

    System.out.println(realiser.realiseSentence(p));

//    test1(nlgFactory, realiser);
  }

  private static void test1(NLGFactory nlgFactory, Realiser realiser)
  {
    NPPhraseSpec myCat = nlgFactory.createNounPhrase("my cat");
    NPPhraseSpec myDog = nlgFactory.createNounPhrase("my dog");
    NPPhraseSpec myHorse = nlgFactory.createNounPhrase("my horse");
    VPPhraseSpec like = nlgFactory.createVerbPhrase("like");
    NPPhraseSpec fish = nlgFactory.createNounPhrase("fish");
    NPPhraseSpec bigBones = nlgFactory.createNounPhrase("big bones");
    NPPhraseSpec grass = nlgFactory.createNounPhrase("grass");

    SPhraseSpec s1 = nlgFactory.createClause();
    SPhraseSpec s2 = nlgFactory.createClause();
    SPhraseSpec s3 = nlgFactory.createClause();

    s1.setSubject(myCat);
    s1.setVerb(like);
    s1.setObject(fish);

    s2.setSubject(myDog);
    s2.setVerb(like);
    s2.setObject(bigBones);

    s3.setSubject(myHorse);
    s3.setVerb(like);
    s3.setObject(grass);

    CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
    c.addCoordinate(s1);
    c.addCoordinate(s2);
    c.addCoordinate(s3);

    String output = realiser.realiseSentence(c);
    System.out.println(output);

    SPhraseSpec p = nlgFactory.createClause("my cat", "be", "happy");
    SPhraseSpec q = nlgFactory.createClause("my cat", "eat", "fish");

    q.setFeature(Feature.COMPLEMENTISER, "because");
    q.setFeature(Feature.TENSE, Tense.PAST);
    p.addComplement(q);

    String output4 = realiser.realiseSentence(p);  //Realiser created earlier
    System.out.println(output4);
  }
}
