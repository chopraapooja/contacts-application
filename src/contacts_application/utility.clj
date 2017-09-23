(ns contacts-application.utility
  (:require [clojure.string :as str]))

(defn pick [coll indices]
  (map #(nth coll %) indices))

(defn not-empty-string? [s]
  (and (string? s) (not (str/blank? s))))

(defn trim [s]
  (if (string? s) (str/trim s) s))
