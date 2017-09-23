(ns contacts-application.contact
  (require [clojure.string :as str]))

(defn to-contact [name]
  (let [[first-name last-name] (-> name str/trim (str/split #"\s+"))]
    {:first-name first-name
     :last-name last-name}))

(defn to-full-name [{:keys [first-name last-name]}]
  (str first-name " " last-name))
