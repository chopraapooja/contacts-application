(ns contacts-application.trie)

;; (add-word tree word)
;;           {}   "a"   =>  {:a {:is-word-completed true 
;;                               :children {}
;;                               :data ["a"]}
;;                               :current_node }

(def new-node {:childrens {}
               :is-word-completed false})

(defn- add-word-reducer [{:keys [prev-node-path] :as tree} ch]
  (let [prev-node (get-in tree prev-node-path)
        kee (-> ch str keyword)
        this-node-path (concat prev-node-path [:childrens kee])
        tree (assoc tree :prev-node-path this-node-path)]
    (if (-> prev-node :childrens kee)
      tree
      (-> tree
          (assoc-in this-node-path new-node)))))

(defn add-word [tree word]
  (let [{:keys [prev-node-path] :as tree} 
        (reduce add-word-reducer 
                (assoc tree  :prev-node-path [])
                word)]
    (when (not-empty prev-node-path)
      (-> tree
          (assoc-in (concat prev-node-path [:is-word-completed]) true)
          (update-in (concat prev-node-path [:data]) #(concat (or % [])
                                                              [word]))))))
