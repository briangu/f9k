package f9k.ops.commands;


import java.util.List;
import simplenlg.features.Feature;
import simplenlg.features.LexicalFeature;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
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
  public void exec(CommandContext context)
  {
    NPPhraseSpec actor = _nlgFactory.createNounPhrase(context.getVar("$actor"));
    VPPhraseSpec verb = _nlgFactory.createVerbPhrase(context.getVar("$verb"));
    NPPhraseSpec object = _nlgFactory.createNounPhrase(context.getVar("$object"));

    boolean isProper = NLGUtil.IsProper(_lexicon, context.getVar("$object"));
    if (!isProper) object.setSpecifier("the");

    SPhraseSpec clause = _nlgFactory.createClause();
    clause.setSubject(actor);
    clause.setVerb(verb);
    clause.setObject(object);
    clause.setFeature(Feature.TENSE, context.getVar("$verb.tense"));

    System.out.println(_realiser.realise(clause).getRealisation());
  }
}
