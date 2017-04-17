/**
 * Spring Data MongoDB multi-language documents example.
 */
package org.ciricuervo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Language;
import org.springframework.data.mongodb.core.mapping.TextScore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The MyDocument entity.
 * 
 * @author ciri-cuervo
 * @since 1.0
 */
@Document
@CompoundIndex(def = "{'language': 1, 'description': 'text'}")
@Data
@EqualsAndHashCode(of = "id")
public class MyDocument {

	@Id
	private String id;

	@Language
	private String language;

	private String description;

	@TextScore
	private Float score;

}
