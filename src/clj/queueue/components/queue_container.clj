(ns queueue.components.queue-container
  (:require [com.stuartsierra.component :as component]))

(defn new-queue
  ([] (new-queue []))
  ([initial-state] {:container (atom initial-state)}))
