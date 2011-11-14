package f9k.ops.commands;


import simplenlg.features.LexicalFeature;
import simplenlg.framework.NLGElement;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;


public class NLGUtil
{
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
