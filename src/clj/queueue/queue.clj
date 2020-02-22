(ns queueue.queue) ; huehuehuehuehue

(defn act
  "Returns the updated queue after it's been
acted upon by the person with a given name"
  [queue name]
  (cond
    (some #{name} queue)
    (into [] (remove #{name} queue))
    (not-any? #(= % name) queue)
    (conj queue name)
    :else
    :DAFUQ))

(defn one-mere-question
  "Adds/advances the given name to the 2nd
place in the queue, if possible"
  [queue name])

(comment
  ; Some dumb tests
  (-> []
      (act "1")
      (act "2")
      (act "1")
      (act "1"))

  (reductions act [] ["1" "2" "1"]))
