/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package phoneticsearch.lucene;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.ElisionFilter;
import org.apache.lucene.util.Version;

/**
 * Classe d'analyse des chaïnes de caractères.
 * Gestion des mots vides en français et en anglais.
 * @author  pchretien
 */
final class DefaultAnalyzer extends Analyzer implements Serializable {
	private static final long serialVersionUID = -653059693798148193L;
	private CharArraySet stopWords;
	private final boolean withFrPhonetic;
	private final boolean withMetaphone;

	/**
	 * Constructeur.
	 * @param useStopWord utilise les stopWord ou non
	 */
	DefaultAnalyzer(final boolean withFrPhonetic, final boolean withMetaphone, final boolean useStopWord) {
		this(withFrPhonetic, withMetaphone, useStopWord ? LuceneConstants.OUR_STOP_WORDS : new String[0]);
	}

	/** Builds an analyzer with the given stop words. */
	private DefaultAnalyzer(final boolean withFrPhonetic, final boolean withMetaphone, final String[] stopWords) {
		this.stopWords = StopFilter.makeStopSet(stopWords);
		this.withFrPhonetic = withFrPhonetic;
		this.withMetaphone = withMetaphone;
	}

	/**
	   * Creates a TokenStream which tokenizes all the text in the provided Reader.
	   *
	   * @return A TokenStream build from a StandardTokenizer filtered with
	   *         StandardFilter, StopFilter, FrenchStemFilter and LowerCaseFilter
	   */
	@Override
	protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
		/* initialisation du token */
		final Tokenizer source = new StandardTokenizer(reader);
		//final Tokenizer source = new NGramTokenizer(reader, 2, 12);
		//---------------------------------------------------------------------
		/* on retire les élisions*/
		final CharArraySet elisionSet = new CharArraySet(Arrays.asList(LuceneConstants.ELISION_ARTICLES), true);
		TokenStream filter = new ElisionFilter(source, elisionSet);
		/* on retire article adjectif */
		filter = new StopFilter(filter, stopWords);
		/* on retire les accents */
		filter = new ASCIIFoldingFilter(filter);
		/* on met en minuscule */
		filter = new LowerCaseFilter(filter);

		if (withFrPhonetic || withMetaphone) {
			//final LanguageSet languages = LanguageSet.from(new HashSet(Arrays.asList("any")));
			//filter = new BeiderMorseFilter(filter, new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, true), languages);
			//filter = new DoubleMetaphoneFilter(filter, 8, true);
			filter = new FrDoubleMetaphoneFilter(filter, 8, true, withFrPhonetic, withMetaphone);
		}
		filter = new PrefixTokenFilter(filter, 6);
		return new TokenStreamComponents(source, filter);
	}

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		final boolean useStopWord = !stopWords.isEmpty();
		out.writeBoolean(useStopWord);
	}

	private void readObject(final java.io.ObjectInputStream in) throws IOException {
		final boolean useStopWord = in.readBoolean();
		stopWords = StopFilter.makeStopSet(Version.LUCENE_4_10_1, useStopWord ? LuceneConstants.OUR_STOP_WORDS : new String[0]);
	}

}
