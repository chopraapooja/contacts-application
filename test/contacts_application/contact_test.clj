(ns contacts-application.contact-test
  (:require [clojure.test :refer :all]
            [contacts-application.contact :refer :all]))

(deftest to-contact-test
  (testing "Can convert name into contact"
    (is (= {:first-name "Jim"
            :last-name "Carry"}
           (to-contact "Jim Carry")))

    (is (= {:first-name "Jim"
            :last-name "Carry"}
           (to-contact "  \n  Jim    \n    Carry   \n   ")))

    (is (= {:first-name "Jim"
            :last-name "Carry"}
           (to-contact "Jim Carry Harry")))

    (is (= {:first-name "Jim"
            :last-name nil}
           (to-contact "Jim")))))

(deftest to-full-name-test
  (testing "Can convert contact to full-name"
    (is (= "Jim Carry"
           (to-full-name {:first-name "Jim"
                          :last-name "Carry"})))

    (is (= "Jim "
           (to-full-name {:first-name "Jim"
                          :last-name nil})))))
