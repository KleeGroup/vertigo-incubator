package io.vertigo.knock.impl.namedentity;

import io.vertigo.knock.namedentity.NamedEntity;
import io.vertigo.lang.Plugin;

import java.util.List;
import java.util.Set;

/**
 * Created by sbernard on 10/12/2014.
 */
public interface RecognizerPlugin extends Plugin {
	Set<NamedEntity> recognizeNamedEntities(final List<String> tokens);
}
