(ns queueue.components.websockets
  (:require [com.stuartsierra.component :as component]
            [manifold.stream :as stream]
            [clojure.data.json :as json]))

(defn- broadcast [connections message]
  (doseq [conn connections]
    (if-not (stream/closed? conn)
      (stream/put! conn (json/write-str message)))))

(defrecord WebsocketsComponent [pool queue]
  component/Lifecycle
  (start [this]
    (add-watch
      (:container queue)
      :ws
      (fn [_ _ _ state] (broadcast @pool state)))
    this)
  (stop [this] this
    (doseq [conn @pool] (stream/close! conn))
    (reset! pool #{})))

(defn new-pool []
  (map->WebsocketsComponent {:pool (atom #{})}))
