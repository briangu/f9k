package f9k.ops;


import gov.nih.nlm.nls.lexCheck.Lib.LexRecord;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import simplenlg.aggregation.Aggregator;
import simplenlg.aggregation.BackwardConjunctionReductionRule;
import simplenlg.aggregation.ClauseCoordinationRule;
import simplenlg.aggregation.ForwardConjunctionReductionRule;
import simplenlg.features.Feature;
import simplenlg.features.Gender;
import simplenlg.features.InterrogativeType;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.StringElement;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.NIHDBLexicon;
import simplenlg.phrasespec.AdjPhraseSpec;
import simplenlg.phrasespec.AdvPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;


public class TestSimpleNLG
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
    Lexicon lexicon = Lexicon.getDefaultLexicon();

    lexicon = new NIHDBLexicon(getPwd() + DB_FILENAME);
    NLGFactory nlgFactory = new NLGFactory(lexicon);
    Realiser realiser = new Realiser(lexicon);

    LexRecord rec = new LexRecord();
    rec.SetBase("cat");
    WordElement cat = lexicon.getWord("running");
    System.out.println(realiser.realise(cat).getRealisation());

    standAloneTest(nlgFactory, realiser);

    load(nlgFactory, realiser);

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

    SPhraseSpec maryHungry = nlgFactory.createClause(mary, "be", "hungry");
    maryHungry.setFeature(Feature.TENSE, Tense.PAST);
    System.out.println(realiser.realiseSentence(new ClauseCoordinationRule().apply(p1, maryHungry)));
    System.out.println(realiser.realiseSentence(new BackwardConjunctionReductionRule().apply(p1, maryHungry)));

    chase.setFeature(Feature.NEGATED, false);
    p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
    p.setFeature(Feature.TENSE, Tense.PAST);

    System.out.println(realiser.realiseSentence(p));

    testAgg(nlgFactory, realiser);
    testAgg2(nlgFactory, realiser);
    testMe(nlgFactory, realiser);

//    test1(nlgFactory, realiser);

    testRun(nlgFactory, realiser);
    testConnect(nlgFactory, realiser);
    test1(nlgFactory, realiser);
  }

  private static void testRun(NLGFactory nlgFactory, Realiser realiser)
  {
    SPhraseSpec p = nlgFactory.createClause();
    p.setSubject(nlgFactory.createNounPhrase("a", "run"));
    p.setVerb("is");
    p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
    System.out.println(realiser.realiseSentence(p));
  }

  private static void testConnect(NLGFactory nlgFactory, Realiser realiser)
  {
    SPhraseSpec p = nlgFactory.createClause();
    p.setSubject(nlgFactory.createNounPhrase("Mary"));
    VPPhraseSpec verb = nlgFactory.createVerbPhrase("connect");
    verb.addModifier("to");
    p.setVerb(verb);
    p.setObject(nlgFactory.createNounPhrase("Bob"));
    p.setFeature(Feature.TENSE, Tense.PAST);
    System.out.println(realiser.realiseSentence(p));
  }

  // http://en.wikipedia.org/wiki/Aggregation_(linguistics)?oldid=0
  private static void testAgg(NLGFactory nlgFactory, Realiser realiser)
  {
    SPhraseSpec s1 = nlgFactory.createClause("the man", "be", "hungry");
    s1.setFeature(Feature.TENSE, Tense.PAST);
    SPhraseSpec s2 = nlgFactory.createClause("the man", "buy", "an apple");
    s2.setFeature(Feature.TENSE, Tense.PAST);
    NLGElement result = new ClauseCoordinationRule().apply(s1, s2);
    System.out.println(realiser.realiseSentence(result));
  }

  private static void testAgg2(NLGFactory nlgFactory, Realiser realiser)
  {
    SPhraseSpec s1 = nlgFactory.createClause("John", "connected to", "Joe");
    s1.setFeature(Feature.TENSE, Tense.PAST);
    SPhraseSpec s2 = nlgFactory.createClause("Larry", "connected to", "Joe");
    s2.setFeature(Feature.TENSE, Tense.PAST);
    NLGElement result = new ClauseCoordinationRule().apply(s1, s2);

    SPhraseSpec sresult = (SPhraseSpec)result;
    CoordinatedPhraseElement np2 = (CoordinatedPhraseElement) sresult.getSubject();
    VPPhraseSpec vp2 = (VPPhraseSpec) sresult.getVerbPhrase();
    NPPhraseSpec op2 = (NPPhraseSpec) sresult.getObject();

    SPhraseSpec s3 = nlgFactory.createClause(np2, vp2, op2);

    System.out.println(realiser.realiseSentence(s3));
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

    myCat.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
    SPhraseSpec p = nlgFactory.createClause(myCat, "be", "happy");
    SPhraseSpec q = nlgFactory.createClause(myCat, "eat", "fish");

    q.setFeature(Feature.COMPLEMENTISER, "because");
    q.setFeature(Feature.TENSE, Tense.PAST);
    p.addComplement(q);

    String output4 = realiser.realiseSentence(p);  //Realiser created earlier
    System.out.println(output4);
  }

  private static void load(NLGFactory phraseFactory, Realiser realiser)
  {
    aggregator = new Aggregator();
    aggregator.initialise();
    coord = new ClauseCoordinationRule();
    fcr = new ForwardConjunctionReductionRule();
    bcr = new BackwardConjunctionReductionRule();

    man = phraseFactory.createNounPhrase("the", "man"); //$NON-NLS-1$ //$NON-NLS-2$
    woman = phraseFactory.createNounPhrase("the", "woman");  //$NON-NLS-1$//$NON-NLS-2$
    dog = phraseFactory.createNounPhrase("the", "dog"); //$NON-NLS-1$ //$NON-NLS-2$
    boy = phraseFactory.createNounPhrase("the", "boy"); //$NON-NLS-1$ //$NON-NLS-2$

    beautiful = phraseFactory.createAdjectivePhrase("beautiful"); //$NON-NLS-1$
    stunning = phraseFactory.createAdjectivePhrase("stunning"); //$NON-NLS-1$
    salacious = phraseFactory.createAdjectivePhrase("salacious"); //$NON-NLS-1$

    onTheRock = phraseFactory.createPrepositionPhrase("on"); //$NON-NLS-1$
    np4 = phraseFactory.createNounPhrase("the", "rock"); //$NON-NLS-1$ //$NON-NLS-2$
    onTheRock.addComplement(np4);

    behindTheCurtain = phraseFactory.createPrepositionPhrase("behind"); //$NON-NLS-1$
    np5 = phraseFactory.createNounPhrase("the", "curtain"); //$NON-NLS-1$ //$NON-NLS-2$
    behindTheCurtain.addComplement(np5);

    inTheRoom = phraseFactory.createPrepositionPhrase("in"); //$NON-NLS-1$
    np6 = phraseFactory.createNounPhrase("the", "room"); //$NON-NLS-1$ //$NON-NLS-2$
    inTheRoom.addComplement(np6);

    underTheTable = phraseFactory.createPrepositionPhrase("under"); //$NON-NLS-1$
    underTheTable.addComplement(phraseFactory.createNounPhrase("the", "table")); //$NON-NLS-1$ //$NON-NLS-2$

    proTest1 = phraseFactory.createNounPhrase("the", "singer"); //$NON-NLS-1$ //$NON-NLS-2$
    proTest2 = phraseFactory.createNounPhrase("some", "person"); //$NON-NLS-1$ //$NON-NLS-2$

    kick = phraseFactory.createVerbPhrase("kick"); //$NON-NLS-1$
    kiss = phraseFactory.createVerbPhrase("kiss"); //$NON-NLS-1$
    walk = phraseFactory.createVerbPhrase("walk"); //$NON-NLS-1$
    talk = phraseFactory.createVerbPhrase("talk"); //$NON-NLS-1$
    getUp = phraseFactory.createVerbPhrase("get up"); //$NON-NLS-1$
    fallDown = phraseFactory.createVerbPhrase("fall down"); //$NON-NLS-1$
    give = phraseFactory.createVerbPhrase("give"); //$NON-NLS-1$
    say = phraseFactory.createVerbPhrase("say"); //$NON-NLS-1$
  }

  private static void testMe(NLGFactory phraseFactory, Realiser realiser)
  {
    woman.setFeature(LexicalFeature.GENDER, Gender.FEMININE);

    // the woman kissed the man behind the curtain
    s1 = phraseFactory.createClause();
    s1.setSubject(woman);
    s1.setVerbPhrase(phraseFactory.createVerbPhrase("kiss"));
    s1.setObject(man);
    s1.addPostModifier(phraseFactory.createPrepositionPhrase("behind", phraseFactory.createNounPhrase("the", "curtain")));

    // the woman kicked the dog on the rock
    s2 = phraseFactory.createClause();
    s2.setSubject(phraseFactory.createNounPhrase("the", "woman")); //$NON-NLS-1$
    s2.setVerbPhrase(phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
    s2.setObject(phraseFactory.createNounPhrase("the", "dog"));
    s2.addPostModifier(onTheRock);

    // the woman kicked the dog behind the curtain
    s3 = phraseFactory.createClause();
    s3.setSubject(phraseFactory.createNounPhrase("the", "woman")); //$NON-NLS-1$
    s3.setVerbPhrase(phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
    s3.setObject(phraseFactory.createNounPhrase("the", "dog"));
    s3.addPostModifier(phraseFactory.createPrepositionPhrase("behind", phraseFactory.createNounPhrase("the", "curtain")));

    // the man kicked the dog behind the curtain
    s4 = phraseFactory.createClause();
    s4.setSubject(man); //$NON-NLS-1$
    s4.setVerbPhrase(phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
    s4.setObject(phraseFactory.createNounPhrase("the", "dog"));
    s4.addPostModifier(behindTheCurtain);

    // the girl kicked the dog behind the curtain
    s5 = phraseFactory.createClause();
    s5.setSubject(phraseFactory.createNounPhrase("the", "girl")); //$NON-NLS-1$
    s5.setVerbPhrase(phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
    s5.setObject(phraseFactory.createNounPhrase("the", "dog"));
    s5.addPostModifier(behindTheCurtain);

    // the woman kissed the dog behind the curtain
    s6 = phraseFactory.createClause();
    s6.setSubject(phraseFactory.createNounPhrase("the", "woman")); //$NON-NLS-1$
    s6.setVerbPhrase(phraseFactory.createVerbPhrase("kiss")); //$NON-NLS-1$
    s6.setObject(phraseFactory.createNounPhrase("the", "dog"));
    s6.addPostModifier(phraseFactory.createPrepositionPhrase("behind", phraseFactory.createNounPhrase("the", "curtain")));

    List<NLGElement> elements = Arrays.asList((NLGElement)s3, s4);
    List<NLGElement> result = coord.apply(elements);
    System.out.println(realiser.realise(result.get(0)).getRealisation());

    System.out.println(realiser.realise(bcr.apply(s3, s6)).getRealisation());
    System.out.println(realiser.realise(fcr.apply(s3, s6)).getRealisation());
  }

  private static void standAloneTest(NLGFactory nlgFactory, Realiser realiser)
  {
    // below is a simple complete example of using simplenlg V4
    // afterwards is an example of using simplenlg just for morphology

    // set up

    // create sentences
    // 	"John did not go to the bigger park. He played football there."
    NPPhraseSpec thePark = nlgFactory.createNounPhrase("the", "park");   // create an NP
    AdjPhraseSpec bigp = nlgFactory.createAdjectivePhrase("big");        // create AdjP
    bigp.setFeature(Feature.IS_COMPARATIVE, true);                       // use comparative form ("bigger")
    thePark.addModifier(bigp);                                        // add adj as modifier in NP
    // above relies on default placement rules.  You can force placement as a premodifier
    // (before head) by using addPreModifier
    PPPhraseSpec toThePark = nlgFactory.createPrepositionPhrase("to");    // create a PP
    toThePark.setObject(thePark);                                     // set PP object
    // could also just say nlgFactory.createPrepositionPhrase("to", the Park);

    SPhraseSpec johnGoToThePark = nlgFactory.createClause("John",      // create sentence
        "go", toThePark);

    johnGoToThePark.setFeature(Feature.TENSE,Tense.PAST);              // set tense
    johnGoToThePark.setFeature(Feature.NEGATED, true);                 // set negated

    // note that constituents (such as subject and object) are set with setXXX methods
    // while features are set with setFeature

    DocumentElement sentence = nlgFactory							// create a sentence DocumentElement from SPhraseSpec
        .createSentence(johnGoToThePark);

    // below creates a sentence DocumentElement by concatenating strings
    StringElement hePlayed = new StringElement("he played");
    StringElement there = new StringElement("there");
    WordElement football = new WordElement("football");

    DocumentElement sentence2 = nlgFactory.createSentence();
    sentence2.addComponent(hePlayed);
    sentence2.addComponent(football);
    sentence2.addComponent(there);

    // now create a paragraph which contains these sentences
    DocumentElement paragraph = nlgFactory.createParagraph();
    paragraph.addComponent(sentence);
    paragraph.addComponent(sentence2);

    // create a realiser.  Note that a lexicon is specified, this should be
    // the same one used by the NLGFactory

    // realiser.setDebugMode(true);     // uncomment this to print out debug info during realisation
    NLGElement realised = realiser.realise(paragraph);

    System.out.println(realised.getRealisation());

    // end of main example

    // second example - using simplenlg just for morphology
    // below is clumsy as direct access to morphology isn't properly supported in V4.2
    // hopefully will be better supported in later versions

    // get word element for "child"
    WordElement word = (WordElement) nlgFactory.createWord("child", LexicalCategory.NOUN);
    // create InflectedWordElement from word element
    InflectedWordElement inflectedWord = new InflectedWordElement(word);
    // set the inflected word to plural
    inflectedWord.setPlural(true);
    // realise the inflected word
    String result = realiser.realise(inflectedWord).getRealisation();

    System.out.println(result);
  }
}
