package f9k.ops.commands;


import f9k.ops.MemoryElement;
import java.util.List;
import simplenlg.aggregation.ClauseCoordinationRule;
import simplenlg.features.Feature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;


public class nlg_agg implements Command
{
  NLGFactory _nlgFactory;
  Realiser _realiser;
  Lexicon _lexicon;

  public nlg_agg(NLGFactory nlgFactory, Realiser realiser, Lexicon lexicon)
  {
    _nlgFactory = nlgFactory;
    _realiser = realiser;
    _lexicon = lexicon;
  }

  @Override
  public void exec(CommandContext context)
  {
    NPPhraseSpec actor1 = _nlgFactory.createNounPhrase(context.getVar("$actor1"));
    NPPhraseSpec actor2 = _nlgFactory.createNounPhrase(context.getVar("$actor2"));
    VPPhraseSpec vp1 = _nlgFactory.createVerbPhrase(context.getVar("$verb"));
    NPPhraseSpec op1 = _nlgFactory.createNounPhrase(context.getVar("$object"));
    Tense tense = (Tense) context.getVar("$verb.tense");

    WordElement objWord = _lexicon.getWord(context.getVar("$object").toString());
    Object feature = objWord.getFeature(LexicalFeature.PROPER);
    boolean isProper = feature == null ? false : (Boolean)feature;
    if (!isProper)
    {
      op1.setSpecifier("the");
    }

    SPhraseSpec s1 = _nlgFactory.createClause(actor1, vp1, op1);
    s1.setFeature(Feature.TENSE, tense);
    SPhraseSpec s2 = _nlgFactory.createClause(actor2, vp1, op1);
    s2.setFeature(Feature.TENSE, tense);
    NLGElement result = new ClauseCoordinationRule().apply(s1, s2);

    SPhraseSpec sresult = (SPhraseSpec)result;
    NLGElement np2 = sresult.getSubject();
    context.make(
        new MemoryElement(
            "sphrase",
            "actor", np2,
            "verb", context.getVar("$verb"),
            "verb.tense", tense,
            "object", context.getVar("$object")));
  }
}
