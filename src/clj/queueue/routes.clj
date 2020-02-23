(ns queueue.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE routes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [response] :as resp]
            [manifold.stream :as stream]
            [aleph.http :as http]))

(defn home-routes [endpoint]
 (let [queue-atom (-> endpoint :queue :container)
       ws-atom (-> endpoint :websockets :pool)]
   (routes
    (GET "/" _
      (-> "public/index.html"
          io/resource
          io/input-stream
          response
          (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
    (POST "/queue" _ (response ""))
    (GET "/events" req
      (let [s @(http/websocket-connection req)]
        (stream/on-closed s #(swap! ws-atom disj s))
        (swap! ws-atom conj s) ; Much of this shouldn't be here
        (swap! queue-atom identity))) ; no-op, to send out initial state
    (GET "/wipe" _ (do (reset! queue-atom []) "Wiped!"))
    (GET "/w00t" _ (do (swap! queue-atom conj "W00t") "Added!"))
    (resources "/"))))
