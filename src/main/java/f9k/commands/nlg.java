package f9k.ops.commands;


import f9k.ops.Command;
import f9k.ops.CommandContext;
import f9k.ops.NLGUtil;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;


public class nlg implements Command
{
  NLGFactory _nlgFactory;
  Realiser _realiser;
  Lexicon _lexicon;

  public nlg(NLGFactory nlgFactory, Realiser realiser, Lexicon lexicon)
  {
    _nlgFactory = nlgFactory;
    _realiser = realiser;
    _lexicon = lexicon;
  }

  @Override
  public void exec(CommandContext context, Object[] args)
  {
    if (args.length != 4)
    {
      throw new IllegalArgumentException("argument count is incorrect: " + args.length);
    }

    NPPhraseSpec actor = _nlgFactory.createNounPhrase(args[0]);
    VPPhraseSpec verb = _nlgFactory.createVerbPhrase(args[1]);
    NPPhraseSpec object = _nlgFactory.createNounPhrase(args[3]);
    Tense tense = NLGUtil.getTense(args[2].toString());

    boolean isProper = NLGUtil.IsProper(_lexicon, args[3]);
    if (!isProper) object.setSpecifier("the");

    SPhraseSpec clause = _nlgFactory.createClause();
    clause.setSubject(actor);
    clause.setVerb(verb);
    clause.setObject(object);
    clause.setFeature(Feature.TENSE, tense);

    System.out.println(_realiser.realise(clause).getRealisation());
  }
}
