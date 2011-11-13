package f9k.ops.commands;


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
  NLGFactory NlgFactory;
  Realiser Realiser;
  Lexicon Lexicon;

  public nlg(NLGFactory nlgFactory, Realiser realiser, Lexicon lexicon)
  {
    NlgFactory = nlgFactory;
    Realiser = realiser;
    Lexicon = lexicon;
  }

  @Override
  public void exec(CommandContext context)
  {
    NPPhraseSpec actor = NlgFactory.createNounPhrase(context.getVar("$actor"));
    VPPhraseSpec verb = NlgFactory.createVerbPhrase(context.getVar("$verb"));
    NPPhraseSpec object = NlgFactory.createNounPhrase(context.getVar("$object"));
    object.setSpecifier("the");

    SPhraseSpec clause = NlgFactory.createClause();
    clause.setSubject(actor);
    clause.setVerb(verb);
    clause.setObject(object);
    clause.setFeature(Feature.TENSE, context.getVar("$verb.tense"));

    System.out.println(Realiser.realise(clause).getRealisation());
  }
}
