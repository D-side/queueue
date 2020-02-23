(ns queueue.queue-test
  (:require [clojure.test :refer :all]
            [queueue.queue :refer :all]))

(deftest acting
  (is (= (reductions act [] ["1" "2" "1" "1"])
         [[] ; Nobody initially
          ["1"] ; 1 joins
          ["1" "2"] ; 2 joins
          ["2"] ; 1 is done talking and leaves
          ["2" "1"]]) ; 1 joins again, becomes last
      "Example acting sequence"))

(deftest advancing
  (testing "Advancing into a queue"  
    (is (= (advance [] "4"
            ["4"]))
        "when it's empty results in joining")
    (is (= (advance ["4"] "4"
            ["4"]))
        "with only themselves is a no-op")
    (is (= (advance ["4" "2"] "4"
            ["4" "2"]))
        "while being at the front is a no-op")       
    (is (= (advance ["2" "4"] "4"
            ["2" "4"]))
        "while already being in 2nd place is a no-op")
    (is (= (advance ["2" "6" "4"] "4"
            ["2" "4" "6"]))
        "while being farther than 2nd makes you a 2nd")))

(deftest mixed
  (is (= (-> []
             (act "1") ; 1 joins
             (act "2") ; 2 joins, becomes last
             (act "3") ; 3 joins, becomes last
             (act "4") ; 4 joins, realizes he's last
             (advance "4") ; 4 advances to front
             (act "1") ; 1 finishes talking and leaves
             (act "1")) ; 1 joins the queue again
         ["4" "2" "3" "1"]))) ; 4 gets the turn, 1 is last
