package f9k;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.NIHDBLexicon;
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
  }
}
