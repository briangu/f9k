package f9k.ops;


import simplenlg.features.LexicalFeature;
import simplenlg.features.Tense;
import simplenlg.framework.NLGElement;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;


public class NLGUtil
{
  public static Tense getTense(String tense)
  {
    return Tense.valueOf(tense.toUpperCase());
  }

  public static boolean IsProper(Lexicon lexicon, Object obj)
  {
    boolean isProper;

    if (obj instanceof String)
    {
      WordElement objWord = lexicon.getWord(obj.toString());
      Object feature = objWord.getFeature(LexicalFeature.PROPER);
      isProper = feature == null ? false : (Boolean)feature;
    }
    else if (obj instanceof NLGElement)
    {
      NLGElement element = (NLGElement) obj;
      Object feature = element.getFeature(LexicalFeature.PROPER);
      isProper = feature == null ? false : (Boolean)feature;
    }
    else
    {
      isProper = false;
    }

    return isProper;
  }
}
