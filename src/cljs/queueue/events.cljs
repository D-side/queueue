(ns queueue.events
  (:require [re-frame.core :as re-frame]
            [queueue.db :as db]
            [akiroz.re-frame.storage :as storage]))

(storage/reg-co-fx!
  ::saved-name
  {:fx   :save-name
   :cofx :load-name})

(re-frame/reg-event-fx
 :initialize-db
 [(re-frame/inject-cofx :load-name)]
 (fn  [{saved-name :load-name} _]
   {:db (assoc db/default-db :name saved-name)}))

(re-frame/reg-event-fx
 :name-changed
 (fn [cofx event]
   (let [[_ new-name] event
         db (:db cofx)] 
     {:db (assoc db :name new-name)
      :save-name new-name})))

;; TODO: Convert this into fx/cofx setup
(re-frame/reg-event-db
  :ws-message
  (fn [db [_ msg]]
    (let [json (.-data msg)
          js (.parse js/JSON json)
          clj (js->clj js)]
     (assoc db :queue clj))))

(re-frame/reg-event-db
  :the-button-pressed
  (fn [db _]
    (js/fetch "/act"
      (clj->js {:method "POST"
                :body (.stringify js/JSON (clj->js {:name (db :name)}))}))
    db))
