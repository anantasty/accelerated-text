(ns api.graphql.queries
  (:require [clojure.test :refer [is]]))

(defn validate-resp
  [resp]
  (let [errors (or (get resp :errors) (get resp "errors"))]
    (is (nil? errors)))
  resp)

(defn create-dict-item
  [k pos]
  {:query     "mutation CreateDictionaryItem($name: String!, $partOfSpeech: PartOfSpeech){createDictionaryItem(name: $name, partOfSpeech: $partOfSpeech){name}}"
   :variables {:name k, :partOfSpeech pos}})

(defn delete-dict-item
  [k]
  {:query     "mutation DeleteDictionaryItem($id: ID!){deleteDictionaryItem(id: $id)}"
   :variables {:id k}})

(defn get-dict-item
  [k]
  {:query (format "{dictionaryItem(id: \"%s\"){name}}" k)})

(defn create-phrase
  [parent text default-usage]
  {:query     "mutation CreatePhrase($dictionaryItemId: ID!, $text: String!, $defaultUsage: DefaultUsage){\n  createPhrase(dictionaryItemId: $dictionaryItemId, text: $text, defaultUsage: $defaultUsage){phrases{id text}}\n}"
   :variables {:dictionaryItemId parent
               :text             text
               :defaultUsage     default-usage}})

(defn update-phrase
  [id text]
  {:query     "mutation UpdatePhrase($id: ID!, $text: String!){\n  updatePhrase(id: $id, text: $text){text defaultUsage}\n}"
   :variables {:id   id
               :text text}})

(defn update-phrase-default-usage
  [id default-usage]
  {:query     "mutation UpdatePhraseDefaultUsage($id: ID!, $defaultUsage: DefaultUsage!){\n  updatePhraseDefaultUsage(id: $id, defaultUsage: $defaultUsage){text defaultUsage}\n}"
   :variables {:id           id
               :defaultUsage default-usage}})

(defn update-reader-flag-usage
  [id usage]
  {:query     "mutation UpdateReaderFlagUsage($id: ID!, $usage: Usage!){\n  updateReaderFlagUsage(id: $id, usage: $usage){id flag{id name} usage}\n}"
   :variables {:id    id
               :usage usage}})

(defn list-verbclasses
  []
  {:query "{concepts{id concepts{id label roles{id fieldType fieldLabel} dictionaryItem{name phrases{text}} helpText}}}"})

(defn list-data-files
  [offset limit record-offset record-limit]
  {:query (format "{listDataFiles(offset: %s limit: %s recordOffset: %s recordLimit: %s) { offset limit totalCount dataFiles { id fileName fieldNames }}}" offset limit record-offset record-limit)})

(defn get-data-file
  [id record-offset record-limit]
  {:query (format "{getDataFile(id: \"%s\" recordOffset: %s recordLimit: %s) { id fileName fieldNames records { id fields { id fieldName value } } recordOffset recordLimit recordCount }}" id record-offset record-limit)})

(defn search-thesaurus
  [query part-of-speech]
  {:query (format "{searchThesaurus(query: \"%s\" partOfSpeech: %s){ words{ id partOfSpeech text concept { id label } } offset limit totalCount }}" query part-of-speech)})

(defn synonyms
  [word-id]
  {:query (format "{synonyms(wordId: \"%s\"){ rootWord { id partOfSpeech text concept { id label } } synonyms{ id partOfSpeech text concept { id label } }}}" word-id)})

(defn document-plans [offset limit]
  {:query (format "{documentPlans(offset: %s limit: %s) { items { id uid name blocklyXml documentPlan dataSampleId dataSampleRow createdAt updatedAt updateCount } offset limit totalCount}}" offset limit)})

(defn document-plan [id]
  {:query (format "{documentPlan(id: \"%s\") { id uid name blocklyXml documentPlan dataSampleId dataSampleRow createdAt updatedAt updateCount }}" id)})

(defn create-document-plan [document-plan]
  {:query     "mutation createDocumentPlan($uid: ID! $name: String! $blocklyXml: String! $documentPlan: String! $dataSampleRow: Int){createDocumentPlan(uid: $uid name: $name blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
   :variables document-plan})

(defn update-document-plan [document-plan]
  {:query     "mutation updateDocumentPlan($id: ID! $uid: ID $name: String $blocklyXml: String $documentPlan: String $dataSampleRow: Int){updateDocumentPlan(id: $id uid: $uid name: $name blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
   :variables document-plan})

(defn delete-document-plan [id]
  {:query     "mutation deleteDocumentPlan($id: ID!){deleteDocumentPlan(id: $id)}"
   :variables {:id id}})
