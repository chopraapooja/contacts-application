(ns contacts-application.trie)

(def new-node {:childrens {}
               :is-word-completed false})

(def new (constantly {}))

(defn- add-word-reducer [{:keys [prev-node-path] :as tree} ch]
  (let [prev-node (get-in tree prev-node-path)
        kee (-> ch str keyword)
        this-node-path (concat prev-node-path [:childrens kee])
        tree (assoc tree :prev-node-path this-node-path)]
    (if (-> prev-node :childrens kee)
      tree
      (-> tree
          (assoc-in this-node-path new-node)))))

(defn add-word 
  ([tree word]
   (add-word tree word word))
  ([tree word data]
   (let [{:keys [prev-node-path] :as tree} 
         (reduce add-word-reducer 
                 (assoc tree  :prev-node-path [])
                 word)]
     (when (not-empty prev-node-path)
       (-> tree
           (assoc-in (concat prev-node-path [:is-word-completed]) true)
           (update-in (concat prev-node-path [:data]) #(concat (or % [])
                                                               [data])))))))

(defn get-all-data-nodes
  ([tree]
   (get-all-data-nodes tree []))
  ([tree result]
   (if (and tree (empty? tree))
     result
     (let [result (if (:is-word-completed tree) 
                    (concat result (:data tree))
                    result)]
       (reduce (fn [acc [k v]]
                 (concat acc (get-all-data-nodes v []))) 
               result 
               (:childrens tree))))))
;; FIXME:
;; Due to lack of time not able to write tests in the format of clojure.test
;; (find-complete-words {} [])
;; (find-complete-words (add-word {} "a") [])
;; (find-complete-words (-> {} (add-word "a") (add-word "ant") (add-word "antique")) [])
;; (find-complete-words (-> {} (add-word "chris") (add-word "chrisharris")) [])

(defn traverse [tree path]
    (loop [tree tree
         path path]
    (let [kee (-> path first str keyword)]
     (cond
      (empty? path) tree
      (empty? tree) nil
      (-> tree :childrens kee)
      (recur (-> tree :childrens kee) (subs path 1))
      :else nil))))

(defn search-tree [tree search-text]
  (let [tree-node (traverse tree search-text)]
    (get-all-data-nodes tree-node)))
