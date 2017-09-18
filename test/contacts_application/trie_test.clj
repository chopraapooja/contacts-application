(ns contacts-application.trie-test
  (:require [clojure.test :refer :all]
            [contacts-application.trie :refer :all]))

(deftest add-word-test
  (testing "with empty trie-tree"
      (testing "Adds a single-letter-word"
        (is (= (add-word {} "a")
               {:prev-node-path '(:childrens :a), 
                :childrens {:a {:childrens {}, :is-word-completed true, :data '("a")}}})))

    (testing "Adds a multi-letter-word"
      (is (= (add-word {} "ab")
             {:prev-node-path '(:childrens :a :childrens :b), 
              :childrens {:a {:childrens {:b {:childrens {}, :is-word-completed true, :data '("ab")}}, :is-word-completed false}}}))))

  (testing "with existing trie-tree"
    (testing "extending a tree "
      (let [tree (add-word {} "a")]
        (is (= (add-word tree "ab")
               {:prev-node-path '(:childrens :a :childrens :b), :childrens {:a {:childrens {:b {:childrens {}, :is-word-completed true, :data '("ab")}}, :is-word-completed true, :data '("a")}}}))))))
