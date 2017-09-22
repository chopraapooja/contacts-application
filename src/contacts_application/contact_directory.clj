(ns contacts-application.contact-directory
  (:require [contacts-application.trie :as trie]))

(def new (constantly {:contacts []
                      :trie trie/new-node}))

(defn- valid-contact? [{:keys [first-name last-name]}]
  (not (nil? first-name)))

(defn add-contact [directory {:keys [first-name last-name] :as contact}]
  (if (valid-contact? contact)
    (let [directory (update directory :contacts #(concat % [contact]))
          contact-index (-> directory :contacts count dec)] 
      (update directory :trie (fn [trie]
                                (-> trie
                                    (trie/add-word first-name contact-index)
                                    (trie/add-word last-name contact-index)))))
    directory))
