(ns contacts-application.contact-directory-test
  (:require [clojure.test :refer :all]
            [contacts-application.contact-directory :as directory]))

(defn dummy-contact []
  {:first-name "Donald"
   :last-name "Trump"})

(deftest add-contact-test
  (testing "Can add new-contact in directory"
    (let [directory (directory/new)
          contact (dummy-contact)
          updated-directory (directory/add-contact directory contact)]
      (is (= [contact]
             (:contacts updated-directory))))))
