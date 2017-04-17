# spring-data-mongodb-multi-language
Spring Data MongoDB multi-language documents example.

---
### The problem of the unique text-index

The **text-index** takes into account the **language** to determine the list of stop words for the search and the rules for the stemmer and tokenizer.

Given the following collection:

    {"_id": "doc1", "language": "en", "description": "English abracadabra!"}
    {"_id": "doc2", "language": "es", "description": "Español abracadabra!"}
    {"_id": "doc3", "language": "de", "description": "Deutsch abracadabra!"}

If we search for the word '*abracadabra*' in any of the languages, we will get **the three documents** as result.

---
### What we want: filter results by language

Only two simple changes are needed to filter the search by language.

**1. Compound index between the language field and the text index**

    @Document
    @CompoundIndex(def = "{'language': 1, 'description': 'text'}")

> Remember to remove the ``@TextIndexed`` annotations.

**2. Search using the compound index**

    @Query("{'language': ?0, $text: {$search: ?1, $language: ?0}}")
    Stream<MyDocument> findMyDocument(String language, String criteria);

---
### The new search result

    findMyDocument("en", "abracadabra") -> MyDocument(doc1, English abracadabra!)
    findMyDocument("es", "abracadabra") -> MyDocument(doc2, Español abracadabra!)
    findMyDocument("de", "abracadabra") -> MyDocument(doc3, Deutsch abracadabra!)
