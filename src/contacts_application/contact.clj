(ns contacts-application.contact
  (:require [contacts-application.utility :as u]
            [clojure.string :as str]))

(defn to-contact [name]
  (let [[first-name last-name] (-> name str/trim (str/split #"\s+"))]
    {:first-name first-name
     :last-name last-name}))

(defn to-full-name [{:keys [first-name last-name]}]
  (str first-name " " last-name))

(defn sanatize [contact]
  (cond-> contact
          (u/not-empty-string? (:last-name contact)) (update :last-name u/trim) 
          :default                                   (update :first-name u/trim)))

(defn valid? [{:keys [first-name]}]
  (u/not-empty-string? first-name))
