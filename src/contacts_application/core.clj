(ns contacts-application.core
  (require [contacts-application.contact-directory :as contact-directory]
           [contacts-application.contact :as contact]
           [clojure.string :as str])
  (:gen-class))

(def directory (atom (contact-directory/new)))

(defn add-contact []
  (println "Enter name:")
  (swap! directory 
         contact-directory/add-contact 
         (contact/to-contact (read-line))))

(defn search-contact []
  (println "Enter name:")
  (->> (contact-directory/search-contact @directory (str/trim (read-line)))
       (map contact/to-full-name)
       (str/join "\n")
       println))

(defn exit []
  (println "Happy Searching")
  (System/exit 0))

(def commands {"1" #'add-contact "2" #'search-contact "3" #'exit})

(defn -main
  [& args]
  (while true
    (println "1) Add Contact 2) Search 3) Exit")
    (let [command (or (get commands (str/trim (read-line)))
                      #(println "Invalid Command"))]
      (command))))


