(ns queueue.application
  (:gen-class)
  (:require [com.stuartsierra.component :as component]
            [queueue.components.server-info :refer [server-info]]
            [queueue.components.queue-container :refer [new-queue]]
            [queueue.components.websockets :refer [new-pool]]
            [system.components.endpoint :refer [new-endpoint]]
            [system.components.handler :refer [new-handler]]
            [system.components.middleware :refer [new-middleware]]
            [system.components.aleph :refer [new-web-server]]
            [queueue.config :refer [config]]
            [queueue.routes :refer [home-routes]]))

(defn app-system [config]
  (component/system-map
   :routes     (-> (new-endpoint home-routes)
                   (component/using [:queue :websockets]))
   :middleware (new-middleware {:middleware (:middleware config)})
   :queue      (new-queue)
   :websockets (-> (new-pool)
                   (component/using [:queue]))
   :handler    (-> (new-handler)
                   (component/using [:routes :middleware]))
   :http       (-> (new-web-server (:http-port config))
                   (component/using [:handler]))
   :server-info (server-info (:http-port config))))

(defn -main [& _]
  (let [config (config)]
    (-> config
        app-system
        component/start)))
