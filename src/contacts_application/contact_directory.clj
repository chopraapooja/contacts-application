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

(defn add-contact [directory contact]
  (if (valid-contact? contact)
    (let [{:keys [first-name last-name] :as contact} (sanatize-contact contact)
          directory (update directory :contacts #(concat % [contact]))
          contact-index (-> directory :contacts count dec)] 
      (update directory :trie (fn [trie]
                                (-> trie
                                    (index-name-in-trie first-name contact-index)
                                    (index-name-in-trie last-name contact-index)))))
    directory))

(defn search-contact [directory search-text]
  (let [contact-indices (distinct (trie/search-tree (:trie directory) 
                                            (-> search-text str/trim str/lower-case)))]
    (utils/pick (:contacts directory) contact-indices)))
