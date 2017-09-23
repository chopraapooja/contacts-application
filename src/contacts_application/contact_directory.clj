(ns contacts-application.contact-directory
  (:require [contacts-application.contact :as contact]
            [contacts-application.trie :as trie]
            [contacts-application.utility :as u]
            [clojure.string :as str]))

(def new (constantly {:contacts []
                      :trie trie/new-node}))

(defn- index-name-in-trie [trie name data]
  (if (u/not-empty-string? name)
    (trie/add-word trie (str/lower-case name) data)
    trie))

(defn- add-contact-to-directory [directory contact]
  (update directory :contacts #(concat % [contact])))

(defn- index-contact [directory {:keys [first-name last-name]}]
  (let [contact-index (-> directory :contacts count dec)]
    (update directory :trie (fn [trie]
                              (-> trie
                                  (index-name-in-trie first-name contact-index)
                                  (index-name-in-trie last-name contact-index))))))

(defn add-contact [directory contact]
  (if (contact/valid? contact)
    (let [_contact (contact/sanatize contact)]
      (-> directory 
          (add-contact-to-directory _contact)
          (index-contact _contact)))
    directory))

(defn search-contact [directory search-text]
  (let [_search-text (-> search-text str/trim str/lower-case)
        contact-indices (distinct (trie/search-tree (:trie directory) 
                                                    _search-text))]
    (u/pick (:contacts directory) contact-indices)))
