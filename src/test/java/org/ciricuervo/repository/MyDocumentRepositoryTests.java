/**
 * Spring Data MongoDB multi-language documents example.
 */
package org.ciricuervo.repository;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

import java.util.List;

import org.ciricuervo.domain.MyDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {@link MyDocumentRepository} tests.
 * 
 * @author ciri-cuervo
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@DataMongoTest
@ActiveProfiles("test")
public class MyDocumentRepositoryTests {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MyDocumentRepository repository;

	private static final String DOCUMENT_EN_ID = "en";
	private static final String DOCUMENT_ES_ID = "es";
	private static final String DOCUMENT_DE_ID = "de";

	@Before
	public void setUp() {
		mongoTemplate.remove(query(where(null)), MyDocument.class);

		{
			MyDocument document = new MyDocument();
			document.setId(DOCUMENT_EN_ID);
			document.setLanguage("en");
			document.setDescription("This is the English description. Abracadabra!");
			mongoTemplate.insert(document);
		}
		{
			MyDocument document = new MyDocument();
			document.setId(DOCUMENT_ES_ID);
			document.setLanguage("es");
			document.setDescription("Esta es la descripción en español. Abracadabra!");
			mongoTemplate.insert(document);
		}
		{
			MyDocument document = new MyDocument();
			document.setId(DOCUMENT_DE_ID);
			document.setLanguage("de");
			document.setDescription("Dies ist die Deutsche Beschreibung. Abracadabra!");
			mongoTemplate.insert(document);
		}
	}

	@Test
	public void findByTermEnLanguageAbracadabra() {
		List<MyDocument> documents = repository.findMyDocument("en", "abracadabra").collect(toList());
		assertThat(documents).extracting(MyDocument::getId).containsExactly(DOCUMENT_EN_ID);
	}

	@Test
	public void findByTermEsLanguageAbracadabra() {
		List<MyDocument> documents = repository.findMyDocument("es", "abracadabra").collect(toList());
		assertThat(documents).extracting(MyDocument::getId).containsExactly(DOCUMENT_ES_ID);
	}

	@Test
	public void findByTermDeLanguageAbracadabra() {
		List<MyDocument> documents = repository.findMyDocument("de", "abracadabra").collect(toList());
		assertThat(documents).extracting(MyDocument::getId).containsExactly(DOCUMENT_DE_ID);
	}

	@Test
	public void findByPhraseEnLanguageDeutsche() {
		List<MyDocument> documents = repository.findMyDocument("en", "\"deutsche Beschreibung\"").collect(toList());
		assertThat(documents).isEmpty();
	}

	@Test
	public void findByPhraseEsLanguageEnglish() {
		List<MyDocument> documents = repository.findMyDocument("es", "\"english description\"").collect(toList());
		assertThat(documents).isEmpty();
	}

	@Test
	public void findByPhraseDeLanguageEspanol() {
		List<MyDocument> documents = repository.findMyDocument("de", "\"descripción en español\"").collect(toList());
		assertThat(documents).isEmpty();
	}

	@Test
	public void findByTermDifferentLanguagesSameWord() {
		List<MyDocument> documents;

		documents = repository.findMyDocument("en", "Beschreibungen").collect(toList());
		assertThat(documents).isEmpty();

		documents = repository.findMyDocument("es", "Beschreibungen").collect(toList());
		assertThat(documents).isEmpty();

		documents = repository.findMyDocument("de", "Beschreibungen").collect(toList());
		assertThat(documents).extracting(MyDocument::getId).containsExactly(DOCUMENT_DE_ID);
	}

	@Test
	public void findByTermNoLanguageAbracadabra() {
		List<MyDocument> documents = repository.findMyDocument("", "abracadabra").collect(toList());
		assertThat(documents).isEmpty();
	}

}
