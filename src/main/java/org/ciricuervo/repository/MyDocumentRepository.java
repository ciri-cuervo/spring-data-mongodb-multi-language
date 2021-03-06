/**
 * Spring Data MongoDB multi-language documents example.
 */
package org.ciricuervo.repository;

import java.util.stream.Stream;

import org.ciricuervo.domain.MyDocument;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * {@link MyDocument} repository.
 * 
 * @author ciri-cuervo
 * @since 1.0
 */
@Repository
public interface MyDocumentRepository extends MongoRepository<MyDocument, String> {

	Sort TEXT_SCORE_SORT = new Sort(Direction.DESC, "score");

	@Query("{'language': ?0, $text: {$search: ?1, $language: ?0}}")
	Stream<MyDocument> findMyDocument(String language, String criteria);

	@Query("{'language': ?0, $text: {$search: ?1, $language: ?0}}")
	Stream<MyDocument> findMyDocument(String language, String criteria, Sort sort);

}
