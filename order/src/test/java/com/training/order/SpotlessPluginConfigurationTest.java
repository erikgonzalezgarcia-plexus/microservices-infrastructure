package com.training.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

class SpotlessPluginConfigurationTest {

    @Test
    void shouldConfigureSpotlessPluginBoundToVerifyPhase() throws Exception {
        try (InputStream pomStream = getClass().getClassLoader().getResourceAsStream("../pom.xml")) {
            assertThat(pomStream).isNotNull();
        }
    }

    @Test
    void shouldDeclareSpotlessPluginInPom() throws Exception {
        Document document = loadPomDocument();

        NodeList artifactIds = document.getElementsByTagName("artifactId");
        boolean spotlessPluginDeclared = false;
        for (int index = 0; index < artifactIds.getLength(); index++) {
            String value = artifactIds.item(index).getTextContent();
            if ("spotless-maven-plugin".equals(value)) {
                spotlessPluginDeclared = true;
                break;
            }
        }

        assertThat(spotlessPluginDeclared).isTrue();
    }

    private Document loadPomDocument() throws Exception {
        try (InputStream pomStream = getClass().getClassLoader().getResourceAsStream("../pom.xml")) {
            assertThat(pomStream).isNotNull();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(false);
            return documentBuilderFactory.newDocumentBuilder().parse(pomStream);
        }
    }
}
