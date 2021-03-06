(ns queueue.components.shell-component
  (:require [com.stuartsierra.component :as component]
            [clojure.string :as str]
            [clojure.java.shell]))

(defrecord ShellComponent [command]
  component/Lifecycle
  (start [this]
    (when-not (:running this)
      (println "Shell command:" (str/join " " command))
      (future (apply clojure.java.shell/sh command)))
    (assoc this :running true))
  (stop [this] this))

(defn shell-component [& cmd]
  (->ShellComponent cmd))
