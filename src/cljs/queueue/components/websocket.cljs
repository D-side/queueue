(ns queueue.components.websocket
  (:require [com.stuartsierra.component :as component]
            [re-frame.core :as re-frame]))

; Horrible coupling, dispatching needs to happen in the app, not here
; Possibly use fx/cofx
(defrecord WebSocket [ws]
  component/Lifecycle
  (start [component]
    (let [ws (js/WebSocket. (str "ws://" (.-host (.-location js/window)) "/events"))]
      (.addEventListener ws "message" #(re-frame/dispatch [:ws-message %]))
      (assoc component :ws ws)))
  (stop [component]
    (.close (:ws component))
    (dissoc component :ws)))

(defn new-websocket []
  (map->WebSocket {}))
