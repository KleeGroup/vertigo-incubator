package io.vertigo.knock.impl.metadata;

import io.vertigo.dynamo.file.model.KFile;
import io.vertigo.knock.metadata.MetaDataContainer;
import io.vertigo.lang.Plugin;

/**
 * Cr�ation de la liste des couples (m�tadonn�e, valeurs) partir d'un document physique.
 *
 * @author pchretien
 * @version $Id: MetaDataExtractorPlugin.java,v 1.3 2014/01/28 18:49:34 pchretien Exp $
 */
public interface MetaDataExtractorPlugin extends Plugin {
	/**
	 * R�cup�ration des m�tadon�es d'une ressource.
	 * @param file Ressource � indexer
	 * @return M�tadonn�es.
	 * @throws Exception Erreur
	 */
	MetaDataContainer extractMetaData(KFile file) throws Exception;

	/**
	 * Pr�cise quels sont les fichiers accept�s par cet extracteur.
	 * @param mimeType TypeMime
	 * @return Si le type mime est accept� par l'extracteur
	 */
	boolean accept(KFile file);
}
