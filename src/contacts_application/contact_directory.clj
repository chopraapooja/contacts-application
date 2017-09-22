(ns contacts-application.contact-directory
  (:require [contacts-application.trie :as trie]
            [contacts-application.utility :as utils]
            [clojure.string :as str]))

(def new (constantly {:contacts []
                      :trie trie/new-node}))

(defn- valid-contact? [{:keys [first-name last-name]}]
  (not (nil? first-name)))

(defn- index-name-in-trie [trie name data]
  (if (not-empty name) 
    (trie/add-word trie (str/lower-case name) data)
    trie))

(defn- sanatize-contact [contact]
  (cond-> contact
          (not-empty (:last-name contact))(update :last-name str/trim) 
          :default                        (update :first-name str/trim)))

(defn- add-contact-to-directory [directory contact]
  (update directory :contacts #(concat % [contact])))

(defn- index-contact [directory {:keys [first-name last-name]}]
  (let [contact-index (-> directory :contacts count dec)]
    (update directory :trie (fn [trie]
                              (-> trie
                                  (index-name-in-trie first-name contact-index)
                                  (index-name-in-trie last-name contact-index))))))

(defn add-contact [directory contact]
  (if (valid-contact? contact)
    (let [_contact (sanatize-contact contact)]
      (-> directory 
          (add-contact-to-directory _contact)
          (index-contact _contact)))
    directory))

(defn search-contact [directory search-text]
  (let [_search-text (-> search-text str/trim str/lower-case)
        contact-indices (distinct (trie/search-tree (:trie directory) 
                                                    _search-text))]
    (utils/pick (:contacts directory) contact-indices)))
