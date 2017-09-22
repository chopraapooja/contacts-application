(ns contacts-application.contact-directory-test
  (:require [clojure.test :refer :all]
            [contacts-application.contact-directory :as directory]))

(def valid-contact  {:first-name "Donald"
                     :last-name "Trump"})

(def valid-contact-with-first-name {:first-name "Donald"})

(def invalid-contact-without-first-name {})

(deftest add-contact-test
  (testing "with VALID contacts"
    
    (testing "Can add new-contact in directory"
      (let [directory (directory/new)
            contact valid-contact
            updated-directory (directory/add-contact directory contact)]
        (is (= [contact]
               (:contacts updated-directory)))))

    (testing "Can add multiple contacts"
      (let [directory (directory/new)
            contact1 {:first-name "Jim" :last-name "Carrey"}
            contact2 {:first-name "Tim" :last-name "Carrey"}
            contact3 {:first-name "Jim" :last-name "Harrey"}
            contact4 {:first-name "Tim" :last-name "Harrey"}
            updated-directory (-> directory
                                 (directory/add-contact contact1)
                                 (directory/add-contact contact2)
                                 (directory/add-contact contact3)
                                 (directory/add-contact contact4))]
        (is (= [contact1 contact2 contact3 contact4] 
               (:contacts updated-directory)))))

    (testing "Can add contact with only first-name"
      (let [directory (directory/new)
            contact valid-contact-with-first-name
            updated-directory (directory/add-contact directory contact)]
        (is (= [contact]
               (:contacts updated-directory))))))


  (testing "with INVALID contacts"

    (testing "Should not update directory for nil contact"
      (let [directory (directory/new)
            contact nil
            updated-directory (directory/add-contact directory contact)]
        (is (= directory updated-directory))))

    (testing "Should not update directory for contacts without first-name"
      (let [directory (directory/new)
            contact invalid-contact-without-first-name
            updated-directory (directory/add-contact directory contact)]
        (is (= directory updated-directory))))

    (comment
      ;; FIXME:
      (testing "Should not update directory for duplicate contacts"
        (let [directory (directory/new)
              contact valid-contact
              updated-directory (-> directory
                                    (directory/add-contact contact)
                                    (directory/add-contact contact))]
          (is (= directory updated-directory)))))))
