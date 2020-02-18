(ns queueue.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :queue
 (fn [db]
   (:queue db)))

(re-frame/reg-sub
  :db
  (fn [db] db))
