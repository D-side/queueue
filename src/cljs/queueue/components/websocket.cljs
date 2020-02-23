(ns queueue.components.websocket
  (:require [com.stuartsierra.component :as component]
            [re-frame.core :as re-frame]
            [queueue.config :refer [debug?]]))

; Horrible coupling, dispatching needs to happen in the app, not here
; Possibly use fx/cofx
(defrecord WebSocket [ws]
  component/Lifecycle
  (start [component]
    (let [protocol (if debug? "ws" "wss") ; TLS is required on production deployment
          ws-url (str protocol "://" (.-host (.-location js/window)) "/events")
          ws (js/WebSocket. ws-url)]
      (.addEventListener ws "message" #(re-frame/dispatch [:ws-message %]))
      (assoc component :ws ws)))
  (stop [component]
    (.close (:ws component))
    (dissoc component :ws)))

(defn new-websocket []
  (map->WebSocket {}))
