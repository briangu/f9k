package f9k.ops.commands;


import f9k.ops.Command;
import f9k.ops.CommandContext;
import f9k.ops.MemoryElement;
import f9k.ops.NLGUtil;
import simplenlg.aggregation.ClauseCoordinationRule;
import simplenlg.features.Feature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Tense;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
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
  public void exec(CommandContext context, Object[] args)
  {
    if (args.length != 5)
    {
      throw new IllegalArgumentException("argument count is incorrect: " + args.length);
    }

    NPPhraseSpec actor1 = _nlgFactory.createNounPhrase(args[0]);
    NPPhraseSpec actor2 = _nlgFactory.createNounPhrase(args[1]);
    VPPhraseSpec vp1 = _nlgFactory.createVerbPhrase(args[2]);
    Tense tense = NLGUtil.getTense(args[3].toString());
    NPPhraseSpec op1 = _nlgFactory.createNounPhrase(args[4]);

    boolean isProper = NLGUtil.IsProper(_lexicon, args[4]);
    if (!isProper) op1.setSpecifier("the");

    SPhraseSpec s1 = _nlgFactory.createClause(actor1, vp1, op1);
    s1.setFeature(Feature.TENSE, tense);
    SPhraseSpec s2 = _nlgFactory.createClause(actor2, vp1, op1);
    s2.setFeature(Feature.TENSE, tense);
    NLGElement result = new ClauseCoordinationRule().apply(s1, s2);

    SPhraseSpec sresult = (SPhraseSpec)result;
    NLGElement op2 = sresult.getObject();
    op2.setFeature(LexicalFeature.PROPER, isProper);
    context.make(
        new MemoryElement(
            "sphrase",
            "actor",
            sresult.getSubject(),
            "verb",
            args[2],
            "verb.tense",
            args[3],
            "object",
            args[4]));
  }
}
