(ns contacts-application.utility)

(defn pick [coll indices]
  (map #(nth coll %) indices))
