package io.vertigo.knock.crawler;

import io.vertigo.AbstractTestCaseJU4;
import io.vertigo.folio.crawler.CrawlerManager;
import io.vertigo.folio.document.model.DocumentVersion;

import org.junit.Test;

import javax.inject.Inject;

/**
 * Test de l'impl�mentation standard.
 *
 * @author pchretien
 * @version $Id: MetaDataManagerTest.java,v 1.5 2014/07/16 13:26:33 pchretien Exp $
 */
public final class CrawlerManagerTest extends AbstractTestCaseJU4 {
    @Inject
    private CrawlerManager crawlerManager;

    @Test
    public void testDiskC() {
        int i = 0;
        for (final DocumentVersion documentVersion : crawlerManager.getCrawler("myFS").crawl("")) {
            System.out.println("doc[" + i + "]: " + documentVersion.getUrl());
            i++;
            if (i > 1000) {
                break;
            }
        }
    }
}
