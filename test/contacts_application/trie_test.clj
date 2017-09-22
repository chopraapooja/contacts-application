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
              :childrens {:a {:childrens {:b {:childrens {}, :is-word-completed true, :data '("ab")}}, :is-word-completed false}}})))

    (testing "Adds meta-data in the leaf node if given"
        (is (= (add-word {} "a" {:a "I am a"})
               {:prev-node-path '(:childrens :a), 
                :childrens {:a {:childrens {}, :is-word-completed true, :data '({:a "I am a"})}}}))))

  (testing "Adding empty string should give the same tree back"
    (is (= {}
           (add-word {} "")))

    (let [tree (add-word {} "abc")]
      (is (= tree
             (add-word tree "")))))

  (testing "Adding nil as word should give the same tree back"
    (is (= {}
           (add-word {} nil)))

    (let [tree (add-word {} nil)]
      (is (= tree
             (add-word tree "")))))

  (testing "with existing trie-tree"
    (testing "extending a tree "
      (let [tree (add-word {} "a")]
        (is (= (add-word tree "ab")
               {:prev-node-path '(:childrens :a :childrens :b), :childrens {:a {:childrens {:b {:childrens {}, :is-word-completed true, :data '("ab")}}, :is-word-completed true, :data '("a")}}}))))))


(deftest search-tree-test
  (testing "Cannot find 'abc' in a tree having ('def') "
    (let [tree (add-word {} "def")]
      (is (= (search-tree tree "abc")
             []))))

  (testing "Can find 'a' in a tree having ('a') "
    (let [tree (add-word {} "a")]
      (is (= (search-tree tree "a")
             ["a"]))))

  (testing "Can find 'a' in a tree having ('a') "
    (let [tree (add-word {} "a")]
      (is (= (search-tree tree "a")
             ["a"]))))

  (testing
      (let [tree (-> (add-word {} "pin")
                     (add-word "pig")
                     (add-word "paste")
                     (add-word "poo")
                     (add-word "ant")
                     (add-word "antique"))]

          (testing "Can find ('pin','pig','paste','poo') when searched for 'p') "
            (is (= (search-tree tree "p")
                   ["pin" "pig" "paste" "poo"])))
          
          (testing "Can find ('pin','pig'') when searched for 'pi') "
            (is (= (search-tree tree "pi")
                   ["pin" "pig"])))

          (testing "Can find ('ant', 'antique') when searched for 'ant') "
            (is (= (search-tree tree "ant")
                   ["ant" "antique"]))))))

(deftest traverse-test
  (testing "traverse test"
      (is (= (traverse {} "a") nil))
      (is (= (traverse {} "") {}))
      (is (= (traverse (add-word {} "abc") "a")
             {:childrens {:b {:childrens {:c {:childrens {}, :is-word-completed true, :data '("abc")}}, :is-word-completed false}}, :is-word-completed false}))
))
