package io.vertigo.folio.plugins.namedentity.tokenizer.stanfordNlp;

import io.vertigo.core.resource.ResourceManager;
import io.vertigo.folio.impl.namedentity.TokenizerPlugin;
import io.vertigo.lang.Assertion;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StanfordTokenizerPlugin implements TokenizerPlugin {
	private final MaxentTagger tagger;

	@Inject
	public StanfordTokenizerPlugin(final ResourceManager resourceManager, final @Named("taggerModel") String modelResource) {
		Assertion.checkNotNull(resourceManager);
		//----
		//        final URL modelURL  = StanfordTokenizerPlugin.class.getClassLoader().getResource("./french.tagger");
		final URL modelURL = resourceManager.resolve(modelResource);
		try {
			tagger = new MaxentTagger(modelURL.getFile());
		} catch (final Exception e) {
			throw new RuntimeException("Failed to load stanford NLP model", e);
		}
	}

	private static void cleanTokens(final List<String> tokens) {
		Assertion.checkNotNull(tokens);
		//----
		final Pattern forbiddenPrefixRegex = Pattern.compile("^(qu[’']|d[’']|l[’'])", Pattern.CASE_INSENSITIVE);
		for (int i = 0; i < tokens.size(); i++) {
			final Matcher matcher = forbiddenPrefixRegex.matcher(tokens.get(i));
			tokens.set(i, matcher.replaceAll(""));
		}
	}

	@Override
	public List<String> tokenize(final String text) {
		Assertion.checkNotNull(text);
		//----
		final List<String> tokens = new ArrayList<>();
		final List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(text));
		for (final List<HasWord> sentence : sentences) {
			final List<TaggedWord> taggedWords = tagger.tagSentence(sentence);
			final Iterator<TaggedWord> wordIterator = taggedWords.iterator();
			String bufferWord = "";
			while (wordIterator.hasNext()) {
				final TaggedWord currentWord = wordIterator.next();
				if ("NPP".equals(currentWord.tag()) || "N".equals(currentWord.tag())) {
					bufferWord = bufferWord + currentWord.word() + " ";
				} else if (!bufferWord.isEmpty()) {
					tokens.add(bufferWord.substring(0, bufferWord.length() - 1));
					bufferWord = "";
				}
			}
		}
		cleanTokens(tokens);
		return tokens;
	}

}
