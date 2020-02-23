(ns queueue.queue) ; huehuehuehuehue

(defn act
  "Returns the updated queue after it's been
acted upon by the person with a given name"
  [queue name]
  (cond
    (some #{name} queue)
    (into [] (remove #{name} queue))
    :else
    (conj queue name)))

(defn advance
  "Adds/advances the given name to the 2nd
place in the queue, if possible"
  [queue name]
  (if (= name (first queue))
    queue
    (let [[head & tail] (remove #{name} queue)]
      (if head
        (into [head name] tail)
        [name]))))
