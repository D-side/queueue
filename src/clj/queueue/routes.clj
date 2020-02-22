(ns queueue.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE routes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [response] :as resp]
            [ring.sse :as sse]
            [manifold.stream :as stream]
            [aleph.http :as http]))

; (def handler
;   (sse/event-channel-handler
;    (fn [request response raise event-ch]
;      (a/go
;        (dotimes [i 20]
;          (let [event {:id   (java.util.UUID/randomUUID)
;                       :name "foo"
;                       :data (json/generate-string {:foo "bar"})}]
;            (a/>! event-ch event)
;            (a/<! (a/timeout 1000))))
;        (a/close! event-ch)))
;    {:on-client-disconnect #(println :info :sse/on-client-disconnect %)}))

(defn home-routes [endpoint]
  (routes
   (GET "/" _
     (-> "public/index.html"
         io/resource
         io/input-stream
         response
         (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
   (POST "/queue" _ (response ""))
   (GET "/events" req (let [s @(http/websocket-connection req)] (stream/connect s s)))
   (resources "/")))
