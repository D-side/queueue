(ns queueue.system
  (:require [com.stuartsierra.component :as component]
            [queueue.components.ui :refer [new-ui-component]]
            [queueue.components.websocket :refer [new-websocket]]))

(declare system)

(defn new-system []
  (component/system-map
   :app-root (new-ui-component)
   :websocket (new-websocket)))

(defn init []
  (set! system (new-system)))

(defn start []
  (set! system (component/start system)))

(defn stop []
  (set! system (component/stop system)))

(defn ^:export go []
  (init)
  (start))

(defn reset []
  (stop)
  (go))
