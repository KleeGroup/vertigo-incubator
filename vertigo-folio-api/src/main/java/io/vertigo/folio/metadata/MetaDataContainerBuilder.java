package io.vertigo.folio.metadata;

import io.vertigo.lang.Assertion;
import io.vertigo.lang.Builder;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder du conteneur des m�ta-donn�es.
 *
 * @author pchretien
 * @version $Id: MetaDataContainerBuilder.java,v 1.4 2014/02/27 10:21:46 pchretien Exp $
 */
public final class MetaDataContainerBuilder implements Builder<MetaDataContainer> {
	private final Map<MetaData, Object> metadatas = new HashMap<>();

	/**
	 * D�finit la valeur d'une m�tadonn�e.
	 * Si la m�tadonn�e est d�j� d�finie elle est surcharg�e.
	 * @param metaData m�ta-donn�e
	 * @param value Valeur de la m�ta-donn�e
	 */
	public MetaDataContainerBuilder withMetaData(final MetaData metaData, final Object value) {
		Assertion.checkNotNull(metaData);
		metaData.getType().checkValue(value);
		//-----
		metadatas.put(metaData, value);
		return this;
	}

	/**
	 * Ajout d'une liste de meta donn�es d�finies dans un MDC au MDC.
	 * Les pr�c�dentes m�tadonn�es sont possiblement remplac�es par les nouvelles.
	 * @param metaDataContainer MDC � ajouter
	 */
	public MetaDataContainerBuilder withAllMetaDatas(final MetaDataContainer metaDataContainer) {
		Assertion.checkNotNull(metaDataContainer);
		//-----
		for (final MetaData metaData : metaDataContainer.getMetaDataSet()) {
			withMetaData(metaData, metaDataContainer.getValue(metaData));
		}
		return this;
	}

	/**
	 * Cr�ation du MDC.
	 * @return Conteneur des m�tadonn�es
	 */
	@Override
	public MetaDataContainer build() {
		if (metadatas.isEmpty()) {
			return MetaDataContainer.EMPTY_META_DATA_CONTAINER;
		}
		return new MetaDataContainer(metadatas);
	}
}
